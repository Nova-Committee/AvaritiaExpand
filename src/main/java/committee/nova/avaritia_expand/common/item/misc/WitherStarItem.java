package committee.nova.avaritia_expand.common.item.misc;

import committee.nova.avaritia_expand.common.entity.ExtremeWitherEntity;
import committee.nova.mods.avaritia.api.iface.ITooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class WitherStarItem extends Item implements ITooltip {
    public WitherStarItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos().above();
        Player player = context.getPlayer();

        if (level.isClientSide() || player == null) {
            return InteractionResult.SUCCESS;
        }

        if (ExtremeWitherEntity.spawnExtremeWither((ServerLevel) level, pos)) {
            if (!player.isCreative()) {
                context.getItemInHand().shrink(1);
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public boolean hasDescTooltip() {
        return true;
    }
}
