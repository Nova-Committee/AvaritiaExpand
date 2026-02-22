package committee.nova.avaritia_expand.common.item.tool.neutron;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class NeutronMaceItem extends MaceItem {

    public NeutronMaceItem(Properties properties) {
        super(properties);
    }

    public static @NotNull ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 50.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, 50.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(ResourceLocation.withDefaultNamespace("attack_range_modifier"), 2.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        boolean result = super.hurtEnemy(stack, target, attacker);

        if (attacker instanceof Player player && canSmashAttack(player)) {

            float fallDistance = player.fallDistance;

            double downwardDistance = Math.min(1.0 + (fallDistance - 1.5F) * 0.5, 10.0);

            if (!target.level().isClientSide) {

                ServerLevel serverLevel = (ServerLevel) target.level();

                Vec3 targetPos = target.position();
                for (int i = 0; i < 20; i++) {
                    serverLevel.sendParticles(
                            ParticleTypes.PORTAL,
                            targetPos.x + (target.level().random.nextDouble() - 0.5) * 2.0,
                            targetPos.y + target.getBbHeight() / 2 + (target.level().random.nextDouble() - 0.5) * 2.0,
                            targetPos.z + (target.level().random.nextDouble() - 0.5) * 2.0,
                            1, 0, 0, 0, 0
                    );
                }

                target.teleportRelative(0, -downwardDistance, 0);

                serverLevel.playSound(
                        null,
                        target.getX(), target.getY(), target.getZ(),
                        SoundEvents.GENERIC_EXPLODE,
                        SoundSource.PLAYERS,
                        1.0F,
                        0.8F
                );
            }
        }

        return result;
    }

    public static boolean canSmashAttack(LivingEntity entity) {
        return entity.fallDistance > 1.5F && !entity.isFallFlying();
    }
}
