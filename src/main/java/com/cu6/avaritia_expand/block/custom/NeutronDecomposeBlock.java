package com.cu6.avaritia_expand.block.custom;

import com.cu6.avaritia_expand.block.entity.ModBlockEntities;
import com.cu6.avaritia_expand.block.entity.NeutronDecomposeBlockEntity;
import com.mojang.serialization.MapCodec;
import committee.nova.mods.avaritia.api.util.NBTUtils;
import committee.nova.mods.avaritia.core.singularity.Singularity;
import committee.nova.mods.avaritia.core.singularity.SingularityReloadListener;
import committee.nova.mods.avaritia.init.registry.ModItems;
import committee.nova.mods.avaritia.util.SingularityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NeutronDecomposeBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public NeutronDecomposeBlock(Properties properties) {
        super(properties);
    }



    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NeutronDecomposeBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.NEUTRON_DECOMPOSE_BE.get(),
                (level1, pos, state1, tile) -> tile.setChanged());
    }

    @Override
    public InteractionResult use(BlockState pState, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (heldStack.is(ModItems.singularity.get())) {
            Singularity singularity = SingularityUtils.getSingularity(heldStack);

            if (singularity != null && singularity.isEnabled()) {

                Ingredient ingredient = singularity.getIngredient();
                int count = singularity.getCount();

                ejectItems(level, pos, ingredient, count, player);
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }else if (heldStack.is(ModItems.eternal_singularity.get())){
            var allSingularities = SingularityReloadListener.INSTANCE.getAllSingularities();

            List<IItemHandler> containers = findAllNearbyContainers(level, pos);

            for (var entry : allSingularities.entrySet()) {
                Singularity singularity = entry.getValue();
                if (singularity.isEnabled()) {
                    ItemStack singularityStack = SingularityUtils.getItemForSingularity(singularity);
                    singularityStack.setCount(1);

                    if (!containers.isEmpty()) {
                        placeItemsInAvailableContainers(containers, level, pos, singularityStack);
                    } else {
                        Containers.dropItemStack(level, pos.getX(), pos.getY() + 1, pos.getZ(), singularityStack);
                    }
                }
            }

            if (!player.isCreative()) {
                heldStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof NeutronDecomposeBlockEntity) {
                player.openMenu((MenuProvider) entity);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void ejectItems(Level level, BlockPos pos, Ingredient ingredient, int count, Player player) {

        List<IItemHandler> containers = findAllNearbyContainers(level, pos);

        ItemStack[] possibleStacks = ingredient.getItems();

        for (ItemStack ingredientStack : possibleStacks) {
            if (!ingredientStack.isEmpty()) {
                int actualCount = count * ingredientStack.getCount();
                ItemStack itemToEject = ingredientStack.copy();
                itemToEject.setCount(actualCount);

                if (!containers.isEmpty()) {
                    // 尝试放入所有可用容器
                    placeItemsInAvailableContainers(containers, level, pos, itemToEject);
                } else {
                    // 没有容器则弹出到世界
                    Containers.dropItemStack(level, pos.getX(), pos.getY() + 1, pos.getZ(), itemToEject);
                }
            }
        }
    }



    private ItemStack insertIntoContainer(IItemHandler container, ItemStack stack) {
        ItemStack remaining = stack.copy();

        for (int slot = 0; slot < container.getSlots(); slot++) {
            remaining = container.insertItem(slot, remaining, false);
            if (remaining.isEmpty()) {
                break; // 所有物品都成功插入
            }
        }

        return remaining;
    }
    private void placeItemsInAvailableContainers(java.util.List<IItemHandler> containers, Level level, BlockPos pos, ItemStack itemStack) {
        ItemStack remaining = itemStack.copy();

        // 遍历所有容器尝试放置
        for (IItemHandler container : containers) {
            remaining = insertIntoContainer(container, remaining);
            if (remaining.isEmpty()) {
                break; // 所有物品都成功放置
            }
        }

        // 如果还有剩余物品，则弹出到世界
        if (!remaining.isEmpty()) {
            Containers.dropItemStack(level, pos.getX(), pos.getY() + 1, pos.getZ(), remaining);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    private List<IItemHandler> findAllNearbyContainers(Level level, BlockPos centerPos) {
        List<IItemHandler> containers = new java.util.ArrayList<>();

        for (Direction direction : Direction.values()) {
            BlockPos offsetPos = centerPos.relative(direction);
            BlockEntity tileEntity = level.getBlockEntity(offsetPos);

            if (tileEntity != null) {
                IItemHandler handler = (IItemHandler) level.getCapability(ForgeCapabilities.ITEM_HANDLER, direction);
                if (handler != null) {
                    containers.add(handler);
                }
            }
        }
        return containers;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

}