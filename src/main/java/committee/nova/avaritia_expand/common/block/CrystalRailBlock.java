package committee.nova.avaritia_expand.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CrystalRailBlock extends RailBlock {

    public CrystalRailBlock(Properties p_55395_) {
        super(p_55395_);
    }
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof AbstractMinecart cart) {

            Vec3 motion = cart.getDeltaMovement();

            double speedMultiplier = 2.0;

            cart.setDeltaMovement(
                    motion.x * speedMultiplier,
                    motion.y,
                    motion.z * speedMultiplier
            );

            double maxSpeed = 15.0;
            if (cart.getDeltaMovement().length() > maxSpeed) {
                cart.setDeltaMovement(cart.getDeltaMovement().normalize().scale(maxSpeed));
            }
        }

        super.entityInside(state, level, pos, entity);
    }
    @Override
    public float getRailMaxSpeed(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        return 15.0F;
    }

}
