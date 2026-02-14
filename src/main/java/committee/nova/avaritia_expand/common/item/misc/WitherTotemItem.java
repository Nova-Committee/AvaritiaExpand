package committee.nova.avaritia_expand.common.item.misc;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WitherTotemItem extends Item {

    public WitherTotemItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide) return;
        if (entity instanceof Player player){
            player.setHealth(player.getMaxHealth() * 2);
            player.heal(player.getMaxHealth() * 2);
            player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 60, 3, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 3, false, false, false));
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.playSound(SoundEvents.WITHER_AMBIENT,1.0f,2.0f);
        return super.use(level, player, usedHand);
    }
}
