package committee.nova.avaritia_expand.common.item.tool.neutron;

import committee.nova.mods.avaritia.api.iface.item.ISwitchable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class NeutronMobileItem extends Item implements ISwitchable {

    public static final List<String> MODES = Arrays.asList("neutron_mobile_overworld", "neutron_mobile_nether", "neutron_mobile_theend");

    private static final int MODE_NORMAL = 0;
    private static final int MODE_NETHER = 1;
    private static final int MODE_THEEND = 2;

    public NeutronMobileItem(Properties properties) {
        super(properties);
    }

    private void onUse(Level level, Player player, ItemStack stack, @NotNull InteractionHand hand) {
        if (!level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            int currentMode = ISwitchable.getCurrentMode(stack, MODES);
            ResourceKey<Level> currentPlayerDimension = player.level().dimension();

            boolean shouldReturnToOverworld =
                    (currentMode == MODE_NETHER && currentPlayerDimension == Level.NETHER) ||
                            (currentMode == MODE_THEEND && currentPlayerDimension == Level.END);

            if (shouldReturnToOverworld) {
                teleportToPersonalSpawn(level, serverPlayer, Level.OVERWORLD);
            } else {
                switch (currentMode) {
                    case MODE_NORMAL:
                        teleportToDimension(level, serverPlayer, Level.OVERWORLD);
                        break;
                    case MODE_NETHER:
                        teleportToDimension(level, serverPlayer, Level.NETHER);
                        break;
                    case MODE_THEEND:
                        teleportToDimension(level, serverPlayer, Level.END);
                        break;
                }
            }
            player.swing(hand, true);
        }
    }

    private void teleportToPersonalSpawn(LevelAccessor levelAccessor, ServerPlayer player, ResourceKey<Level> targetDimension) {
        if (player != null && player.isAlive() && !levelAccessor.isClientSide()) {
            if (levelAccessor instanceof ServerLevel serverLevel && !player.isPassenger() && !player.isVehicle()) {
                MinecraftServer server = player.getServer();
                ServerLevel destinationWorld = server != null ? server.getLevel(targetDimension) : null;

                if (destinationWorld != null) {
                    BlockPos spawnPos = player.getRespawnPosition();
                    float spawnAngle = player.getRespawnAngle();

                    if (spawnPos == null) {
                        spawnPos = destinationWorld.getSharedSpawnPos();
                    } else {
                        if (!destinationWorld.isLoaded(spawnPos)) {
                            spawnPos = destinationWorld.getSharedSpawnPos();
                        }
                    }

                    saveCurrentPositionToNBT(player, serverLevel.dimension());

                    DimensionTransition transition = new DimensionTransition(
                            destinationWorld,
                            new Vec3(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5),
                            Vec3.ZERO,
                            spawnAngle,
                            0F,
                            true,
                            DimensionTransition.PLAY_PORTAL_SOUND
                    );

                    player.changeDimension(transition);
                }
            }
        }
    }

    private void teleportToDimension(LevelAccessor levelAccessor, ServerPlayer player, ResourceKey<Level> targetDimension) {
        if (player != null && player.isAlive() && !levelAccessor.isClientSide()) {
            if (levelAccessor instanceof ServerLevel serverLevel && !player.isPassenger() && !player.isVehicle()) {
                MinecraftServer server = player.getServer();
                ServerLevel destinationWorld = server != null ? server.getLevel(targetDimension) : null;


                BlockPos targetPos = getLastKnownPosition(player, targetDimension);
                if (targetPos == null) {
                    if (destinationWorld != null) {
                        targetPos = destinationWorld.getSharedSpawnPos();
                    }
                }

                saveCurrentPositionToNBT(player, serverLevel.dimension());

                DimensionTransition transition = new DimensionTransition(
                        destinationWorld,
                        new Vec3(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5),
                        Vec3.ZERO,
                        player.getYRot(),
                        player.getXRot(),
                        false,
                        DimensionTransition.PLAY_PORTAL_SOUND
                );

                player.changeDimension(transition);
            }
        }
    }

    private BlockPos getLastKnownPosition(ServerPlayer player, ResourceKey<Level> dimension) {
        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains("AvaritiaExpand")) {
            CompoundTag avatitiaData = persistentData.getCompound("AvaritiaExpand");
            String lastDim = avatitiaData.getString("LastDimension");
            if (lastDim.equals(dimension.location().toString())) {
                int x = avatitiaData.getInt("LastX");
                int y = avatitiaData.getInt("LastY");
                int z = avatitiaData.getInt("LastZ");
                return new BlockPos(x, y, z);
            }
        }
        return null;
    }

    private void saveCurrentPositionToNBT(ServerPlayer player, ResourceKey<Level> currentDimension) {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag avatitiaData = persistentData.getCompound("AvaritiaExpand");

        avatitiaData.putString("LastDimension", currentDimension.location().toString());
        BlockPos currentPos = player.blockPosition();
        avatitiaData.putInt("LastX", currentPos.getX());
        avatitiaData.putInt("LastY", currentPos.getY());
        avatitiaData.putInt("LastZ", currentPos.getZ());

        persistentData.put("AvaritiaExpand", avatitiaData);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching()) {
            cycleMode(world, player, hand, MODES);
            return InteractionResultHolder.success(stack);
        }
        onUse(world, player, stack, hand);
        return InteractionResultHolder.pass(stack);
    }
}
