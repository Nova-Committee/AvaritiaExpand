package committee.nova.avaritia_expand.util;

import com.google.common.collect.Sets;
import committee.nova.mods.avaritia.api.iface.transform.IToolTransform;
import committee.nova.mods.avaritia.util.ClustersUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Set;

import static committee.nova.mods.avaritia.util.ToolUtils.canUseTool;

public class AEToolUtils {

    public static void rangeMineBlock(ServerPlayer player, BlockPos startPos, int range, Set<TagKey<Block>> materials) {
        ServerLevel world = player.serverLevel();
        int halfRange = range / 2;
        BlockPos minPos = startPos.offset(-halfRange, -halfRange, -halfRange);
        BlockPos maxPos = startPos.offset(halfRange, halfRange, halfRange);
        Set<ItemStack> drops = Sets.newHashSet();

        for(BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
            BlockPos currentPos = pos.immutable();
            BlockState state = world.getBlockState(currentPos);
            if (canUseTool(state, materials) && state.getBlock().canHarvestBlock(state, world, currentPos, player)) {
                List<ItemStack> blockDrops = Block.getDrops(state, world, currentPos, (BlockEntity)null);
                if (!blockDrops.isEmpty()) {
                    drops.addAll(blockDrops);
                } else {
                    ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(state.getBlock());
                    Item blockItem = (Item)BuiltInRegistries.ITEM.get(blockKey);
                    if (blockItem != Items.AIR) {
                        drops.add(new ItemStack(blockItem));
                    }
                }

                world.destroyBlock(currentPos, false, player);
                world.playSound((Player)null, currentPos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.5F, 1.0F);
                world.levelEvent(2001, currentPos, Block.getId(state));
            }
        }

    }

}
