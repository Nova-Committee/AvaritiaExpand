package committee.nova.avaritia_expand.common.item.food;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class SingularityStewItem extends Item {

    private static final ResourceLocation MAX_HEALTH_MODIFIER_UUID = ResourceLocation.parse("singularity_stew_health_boost");

    public SingularityStewItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof net.minecraft.world.entity.player.Player player) {

            double currentMaxHealth = player.getAttribute(Attributes.MAX_HEALTH).getBaseValue();

            double newMaxHealth = currentMaxHealth * 1.25;

            AttributeModifier healthBoost = new AttributeModifier(
                    MAX_HEALTH_MODIFIER_UUID,
                    newMaxHealth - currentMaxHealth,
                    AttributeModifier.Operation.ADD_VALUE
            );

            Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).removeModifier(healthBoost);
            Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(healthBoost);

            player.setHealth(player.getMaxHealth());
        }

        return super.finishUsingItem(stack, level, entity);
    }
}
