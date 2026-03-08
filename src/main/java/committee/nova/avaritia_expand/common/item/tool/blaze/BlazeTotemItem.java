package committee.nova.avaritia_expand.common.item.tool.blaze;

import committee.nova.mods.avaritia.api.iface.ITooltip;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BlazeTotemItem extends Item implements ITooltip {
    public BlazeTotemItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide) return;
        if (entity instanceof Player player){
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 3, false, false, false));
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public boolean hasDescTooltip() {
        return true;
    }
}
