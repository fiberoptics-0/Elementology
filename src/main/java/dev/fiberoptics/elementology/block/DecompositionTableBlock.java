package dev.fiberoptics.elementology.block;

import dev.fiberoptics.elementology.block.entity.AbstractCrafterBlockEntity;
import dev.fiberoptics.elementology.block.entity.DecompositionTableBlockEntity;
import dev.fiberoptics.elementology.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DecompositionTableBlock extends AbstractCrafterBlock {

    public DecompositionTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape() {
        return Block.box(0,0,0,16,8,16);
    }

    @Override
    protected BlockEntityType<? extends AbstractCrafterBlockEntity> getEntityType() {
        return ModBlockEntities.DECOMPOSITION_TABLE_BE.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DecompositionTableBlockEntity(blockPos, blockState);
    }
}
