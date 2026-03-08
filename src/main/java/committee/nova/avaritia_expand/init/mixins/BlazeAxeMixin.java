package committee.nova.avaritia_expand.init.mixins;

import committee.nova.avaritia_expand.common.entity.BlazeVindicatorEntity;
import committee.nova.avaritia_expand.init.registry.AEEntities;
import committee.nova.mods.avaritia.common.item.tools.blaze.BlazeAxeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlazeAxeItem.class)
public class BlazeAxeMixin {

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        ItemStack itemStack = context.getItemInHand();
        if (!level.isClientSide && player != null) {

            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof BannerBlockEntity banner) {
                if (banner.getPatterns().layers().size() > BannerBlockEntity.MAX_PATTERNS) {

                    BlazeVindicatorEntity vindicator = new BlazeVindicatorEntity(AEEntities.BLAZE_VINDICATOR.get(), level);
                    vindicator.moveTo(
                            pos.getX(),
                            pos.getY(),
                            pos.getZ(),
                            player.getYRot(),
                            0
                    );
                    vindicator.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null);
                    level.addFreshEntity(vindicator);
                    if (!player.isCreative()) {
                        itemStack.shrink(1);
                    }

                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    level.levelEvent(1023, pos, 0);
                    ((ServerLevel) level).sendParticles(
                            ParticleTypes.SMOKE,
                            pos.getX(),
                            pos.getY() + 1,
                            pos.getZ(),
                            40,
                            0.3,0.3,0.3,
                            0.02
                    );

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }
}