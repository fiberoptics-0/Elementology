package dev.fiberoptics.elementology.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCrafterBlockEntity extends BlockEntity implements MenuProvider {

    protected final ContainerData data;
    protected final ItemStackHandler itemHandler = new ItemStackHandler(this.getContainerSize());
    protected int progress = 0;
    protected int maxProgress = this.getMaxProgress();
    protected LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public AbstractCrafterBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState blockState) {
        super(entityType, pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AbstractCrafterBlockEntity.this.progress;
                    case 1 -> AbstractCrafterBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AbstractCrafterBlockEntity.this.progress = value;
                    case 1 -> AbstractCrafterBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    protected abstract int getMaxProgress();

    protected abstract int getContainerSize();

    @Override
    public abstract Component getDisplayName();

    @Override
    public abstract AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player);

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory",itemHandler.serializeNBT());
        tag.putInt("progress", progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("progress");
        super.load(tag);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if(hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, pos, state);
            if(progressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    protected abstract void increaseCraftingProgress();
    protected abstract void resetProgress();
    protected abstract void craftItem();
    protected abstract boolean progressFinished();
    protected abstract boolean hasRecipe();
}
