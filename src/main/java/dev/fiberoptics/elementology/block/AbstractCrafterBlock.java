package dev.fiberoptics.elementology.block;

import dev.fiberoptics.elementology.block.entity.AbstractCrafterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
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

public abstract class AbstractCrafterBlock extends BaseEntityBlock {

    public AbstractCrafterBlock(Properties properties) {
        super(properties);
    }

    protected abstract VoxelShape getShape();

    protected abstract BlockEntityType<? extends AbstractCrafterBlockEntity> getEntityType();

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return this.getShape();
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if(blockState.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity instanceof AbstractCrafterBlockEntity entity) {
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
            if(blockEntity instanceof AbstractCrafterBlockEntity entity) {
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
    public abstract BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
                                                                            BlockEntityType<T> entityType) {
        if(level.isClientSide()) return null;

        return createTickerHelper(entityType, this.getEntityType(),
                (pLevel,pPos,pState,pBlockEntity) ->
                        pBlockEntity.tick(pLevel,pPos,pState));
    }
}
