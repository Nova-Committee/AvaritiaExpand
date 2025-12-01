package committee.nova.avaritia_expand.init.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class AEFoods {
    public static final FoodProperties monster = new FoodProperties.Builder().nutrition(1).saturationModifier(1).effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3000, 1), 0.6F).build();
}
