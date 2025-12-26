package committee.nova.avaritia_expand.common.item.tool.infinity;

import committee.nova.avaritia_expand.init.registry.AEBlocks;
import committee.nova.mods.avaritia.api.iface.ITooltip;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.IShearable;
import java.util.List;
import java.util.Random;

public class InfinityShearsItem extends ShearsItem implements ITooltip {
    public InfinityShearsItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, net.minecraft.world.InteractionHand hand) {
        if (entity instanceof IShearable target) {
            BlockPos pos = entity.blockPosition();
            boolean isClient = entity.level().isClientSide();

            if (target.isShearable(player, stack, entity.level(), pos)) {
                List<ItemStack> drops = target.onSheared(player, stack, entity.level(), pos);

                if (!isClient) {
                    for(ItemStack drop : drops) {
                        if (isWoolItem(drop)) {
                            ItemStack jebWool = new ItemStack(AEBlocks.jeb_wool.get());
                            target.spawnShearedDrop(entity.level(), pos, jebWool);
                        } else {
                            target.spawnShearedDrop(entity.level(), pos, drop);
                        }
                    }
                }

                entity.gameEvent(GameEvent.SHEAR, player);

                if (!isClient) {
                    stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                }

                return InteractionResult.sidedSuccess(isClient);
            }
        }
        return super.interactLivingEntity(stack, player, entity, hand);
    }

    private boolean isWoolItem(ItemStack stack) {
        String itemName = stack.getItem().toString();
        return itemName.contains("wool") || itemName.contains("Wool");
    }

    @Override
    public boolean hasDescTooltip() {
        return true;
    }
}
