package com.cu6.avaritia_expand.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class NeutronDecomposeBlockEntity extends BlockEntity{

    public NeutronDecomposeBlockEntity( BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.NEUTRON_DECOMPOSE_BE.get(), pPos, pBlockState);
    }
}
