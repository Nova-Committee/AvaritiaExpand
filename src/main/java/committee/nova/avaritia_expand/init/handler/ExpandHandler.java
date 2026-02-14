package committee.nova.avaritia_expand.init.handler;

import committee.nova.avaritia_expand.AvaritiaExpand;
import committee.nova.avaritia_expand.common.item.tool.neutron.NeutronSwordItem;
import committee.nova.avaritia_expand.init.registry.AEBlocks;
import committee.nova.avaritia_expand.init.registry.AEItems;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.Random;

@EventBusSubscriber(modid = AvaritiaExpand.MOD_ID)

public class ExpandHandler {
    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack mainHandItem = player.getMainHandItem();
        if (mainHandItem.getItem() instanceof NeutronSwordItem) {
            Level world = player.level();
            Random random = new Random();

            double offsetX = (random.nextBoolean() ? 1 : -1) * 1.0;
            BlockPos originalPos = player.blockPosition();
            BlockPos offsetPos = originalPos.offset((int) offsetX, 0, 0);

            if (world.isEmptyBlock(offsetPos)) {
                player.teleportTo(offsetPos.getX() + 0.5, offsetPos.getY(), offsetPos.getZ() + 0.5);

            }
        }
    }
    @SubscribeEvent
    public static void onPlayerMine(PlayerInteractEvent.LeftClickBlock event) {
        var item = event.getItemStack();
        var level = event.getLevel();
        var pos = event.getPos();
        var state = level.getBlockState(pos);
        var player= event.getEntity();
        var face = event.getFace();
        if (face == null || level.isClientSide || item.isEmpty() || player.isCreative()) {
            return;
        }

        if (item.is(AEItems.neutron_pickaxe.get())){
            if (state.is(Blocks.BEDROCK)) {
                level.setBlock(pos, ModBlocks.fake_bedrock.get().defaultBlockState(), 2);
            } else if (state.is(Blocks.END_PORTAL_FRAME)) {

                BlockState fakeState = ModBlocks.fake_end_portal_frame.get().defaultBlockState();


                if (fakeState.hasProperty(BlockStateProperties.EYE) && state.hasProperty(BlockStateProperties.EYE)) {
                    fakeState = fakeState.setValue(BlockStateProperties.EYE, state.getValue(BlockStateProperties.EYE));
                }

                if (fakeState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                    fakeState = fakeState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                }

                level.setBlock(pos, fakeState, 2);
            } else if (state.is(Blocks.END_PORTAL)) {
                level.setBlock(pos, ModBlocks.fake_end_portal.get().defaultBlockState(), 2);
            }
        }

        if (!(item.is(AEItems.neutron_pickaxe.get()))){
            if (state.is(ModBlocks.fake_bedrock.get())) {
                level.setBlock(pos, Blocks.BEDROCK.defaultBlockState(), 2);
            } else if (state.is(ModBlocks.fake_end_portal_frame.get())) {

                BlockState originalState = Blocks.END_PORTAL_FRAME.defaultBlockState();


                if (originalState.hasProperty(BlockStateProperties.EYE) && state.hasProperty(BlockStateProperties.EYE)) {
                    originalState = originalState.setValue(BlockStateProperties.EYE, state.getValue(BlockStateProperties.EYE));
                }

                if (originalState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                    originalState = originalState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                }

                level.setBlock(pos, originalState, 2);
            } else if (state.is(ModBlocks.fake_end_portal.get())) {
                level.setBlock(pos, Blocks.END_PORTAL.defaultBlockState(), 2);
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerMine(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) return;
        var level = (ServerLevel) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        if (!event.getPlayer().isCreative()) {
            if (state.is(ModBlocks.fake_bedrock.get())) {
                Block.popResource(level, pos, Blocks.BEDROCK.asItem().getDefaultInstance());
            } else if (state.is(ModBlocks.fake_end_portal_frame.get())) {

                ItemStack frameItem = Blocks.END_PORTAL_FRAME.asItem().getDefaultInstance();

                Block.popResource(level, pos, frameItem);
            } else if (state.is(ModBlocks.fake_end_portal.get())) {
                Block.popResource(level, pos, Blocks.END_PORTAL.asItem().getDefaultInstance());
            } else if (state.is(Blocks.REINFORCED_DEEPSLATE)) {
                Block.popResource(level, pos, Blocks.REINFORCED_DEEPSLATE.asItem().getDefaultInstance());
            }
        }
    }

}
