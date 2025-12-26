package committee.nova.avaritia_expand.init.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class AEFoods {
    public static double ratio = (double)5.0F;
    public static final FoodProperties star_dessert = new FoodProperties.Builder()
            .nutrition(20).saturationModifier(20.0F)
            .effect(new MobEffectInstance(MobEffects.JUMP, (int)Math.ceil((double)3600.0F * ratio), 2), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, (int)Math.ceil((double)3600.0F * ratio), 2),1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.SATURATION, (int)Math.ceil((double)3600.0F * ratio), 1),1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (int)Math.ceil((double)3600.0F * ratio), 2),1.0F)
            .build();
    public static final FoodProperties singularity_stew = new FoodProperties.Builder()
            .nutrition(999999999).saturationModifier(999999999)
            .effect(() -> new MobEffectInstance(MobEffects.SATURATION, (int)Math.ceil((double)12000.0F * ratio), 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, (int)Math.ceil((double)12000.0F * ratio), 2),1.0F)
            .build();
}
