package dev.fiberoptics.elementology.block;

import dev.fiberoptics.elementology.block.entity.DecompositionTableBlockEntity;
import dev.fiberoptics.elementology.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class DecompositionTableBlock extends BaseEntityBlock {

    private final VoxelShape shape =
            Block.box(0,0,0,16,8,16);

    public DecompositionTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if(blockState.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity instanceof DecompositionTableBlockEntity entity) {
                entity.drops();
            }
        }
        super.onRemove(blockState, level, blockPos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult result) {
        if(!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof DecompositionTableBlockEntity entity) {
                NetworkHooks.openScreen((ServerPlayer) player,entity,pos);
            } else {
                throw new IllegalStateException("Container Provider Missing");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DecompositionTableBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
                                                                            BlockEntityType<T> entityType) {
        if(level.isClientSide()) return null;

        return createTickerHelper(entityType, ModBlockEntities.DECOMPOSITION_TABLE_BE.get(),
                (pLevel,pPos,pState,pBlockEntity) ->
                        pBlockEntity.tick(pLevel,pPos,pState));
    }
}
