package committee.nova.avaritia_expand.common.item;

import committee.nova.mods.avaritia.api.iface.transform.IToolTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NeutronHoeItem extends HoeItem implements IToolTransform {
    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 1500;

    public NeutronHoeItem(Tier p_41336_, Properties p_41339_) {
        super(p_41336_, p_41339_);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        InteractionResult hoeResult = super.useOn(context);

        if (hoeResult.consumesAction()) {
            return hoeResult;
        }

        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null) {
            return InteractionResult.PASS;
        }

        UUID playerId = player.getUUID();
        long currentTime = System.currentTimeMillis();
        if (cooldowns.containsKey(playerId)) {
            long lastUseTime = cooldowns.get(playerId);
            if (currentTime - lastUseTime < COOLDOWN_TIME) {
                return InteractionResult.PASS;
            }
        }

        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.getBlock() instanceof BonemealableBlock bonemealableBlock &&
                bonemealableBlock.isValidBonemealTarget(level, blockpos, blockstate) &&
                isCropOrSapling(blockstate)) {

            if (level instanceof ServerLevel serverLevel) {
                if (bonemealableBlock.isBonemealSuccess(serverLevel, serverLevel.random, blockpos, blockstate)) {
                    bonemealableBlock.performBonemeal(serverLevel, serverLevel.random, blockpos, blockstate);

                    cooldowns.put(playerId, currentTime);

                    player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                    level.levelEvent(1505, blockpos, 15);

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }

        return InteractionResult.PASS;
    }

    private boolean isCropOrSapling(BlockState blockState) {
        Block block = blockState.getBlock();

        if (block instanceof CropBlock) {
            return true;
        }

        if (block instanceof SaplingBlock) {
            return true;
        }
        return false;
    }

    public static void cleanupCooldowns() {
        long currentTime = System.currentTimeMillis();
        cooldowns.entrySet().removeIf(entry -> currentTime - entry.getValue() >= COOLDOWN_TIME);
    }
}
