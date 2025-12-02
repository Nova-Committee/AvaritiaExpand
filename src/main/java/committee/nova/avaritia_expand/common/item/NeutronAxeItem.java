package committee.nova.avaritia_expand.common.item;

import committee.nova.mods.avaritia.api.iface.transform.IToolTransform;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class NeutronAxeItem extends AxeItem implements IToolTransform {
    public NeutronAxeItem(Tier p_40521_, Properties p_40524_) {
        super(p_40521_, p_40524_);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        boolean isJumpAttack = this.isJumpAttack(attacker);

        if (!target.level().isClientSide) {
            if (isJumpAttack) {

                DamageSource voidDamage = target.level().damageSources().fellOutOfWorld();

                target.hurt(voidDamage, 50.0F);

                Vec3 pos = target.position();
                target.level().addParticle(net.minecraft.core.particles.ParticleTypes.PORTAL,
                        pos.x, pos.y + target.getBbHeight() / 2, pos.z,
                        (target.level().random.nextDouble() - 0.5) * 2.0,
                        (target.level().random.nextDouble() - 0.5) * 2.0,
                        (target.level().random.nextDouble() - 0.5) * 2.0);
                target.teleportRelative(0, -1, 0);
            }
        }
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
        return super.hurtEnemy(stack, target, attacker);
    }

    private boolean isJumpAttack(LivingEntity attacker) {
        return !attacker.onGround() && attacker.getDeltaMovement().y() < -0.1;
    }
}
