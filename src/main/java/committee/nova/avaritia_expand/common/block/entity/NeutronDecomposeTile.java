package committee.nova.avaritia_expand.common.block.entity;

import committee.nova.avaritia_expand.init.registry.AEBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NeutronDecomposeTile extends BlockEntity {

    public NeutronDecomposeTile(BlockPos pos, BlockState blockState) {
        super(AEBlockEntities.NEUTRON_DECOMPOSE_BE.get(), pos, blockState);
    }
}
