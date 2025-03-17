package dev.fiberoptics.elementology.block.entity;

import dev.fiberoptics.elementology.block.DecompositionTableBlock;
import dev.fiberoptics.elementology.item.ModItems;
import dev.fiberoptics.elementology.screen.DecompositionTableMenu;
import dev.fiberoptics.elementology.util.Utils;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

public class DecompositionTableBlockEntity extends BlockEntity implements MenuProvider{

    protected final ContainerData data;

    private int progress = 0;
    private int maxProgress = 100;


    private final ItemStackHandler itemHandler = new ItemStackHandler(2);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public DecompositionTableBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DECOMPOSITION_TABLE_BE.get(), pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> DecompositionTableBlockEntity.this.progress;
                    case 1 -> DecompositionTableBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> DecompositionTableBlockEntity.this.progress = value;
                    case 1 -> DecompositionTableBlockEntity.this.maxProgress = value;
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

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.elementology.decomposition_table");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new DecompositionTableMenu(containerId,inventory,this,this.data);
    }

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

    private void craftItem() {
        ItemStack result = new ItemStack(ModItems.MIXED_ELEMENT.get());
        result.setTag(Utils.generateElementComposition(0.5f,1,0,0));
        this.itemHandler.extractItem(INPUT_SLOT, 1, false);
        this.itemHandler.setStackInSlot(INPUT_SLOT, new ItemStack(Items.BUCKET));
        this.itemHandler.setStackInSlot(OUTPUT_SLOT, result);
    }

    private boolean hasRecipe() {
        boolean hasCraftingItem = this.itemHandler.getStackInSlot(INPUT_SLOT).is(Items.WATER_BUCKET);
        ItemStack result = new ItemStack(ModItems.MIXED_ELEMENT.get());
        return hasCraftingItem && canInsertAmountIntoOutput(result.getCount()) &&
                canInsertItemIntoOutput(result.getItem());
    }

    private boolean canInsertItemIntoOutput(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item) ||
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty();
    }

    private boolean canInsertAmountIntoOutput(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <=
                        this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    private boolean progressFinished() {
        return this.progress >= this.maxProgress;
    }

    private void resetProgress() {
        this.progress = 0;
    }
}
