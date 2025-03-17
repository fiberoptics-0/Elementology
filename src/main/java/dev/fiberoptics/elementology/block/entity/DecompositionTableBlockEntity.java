package dev.fiberoptics.elementology.block.entity;

import dev.fiberoptics.elementology.item.ModItems;
import dev.fiberoptics.elementology.menu.DecompositionTableMenu;
import dev.fiberoptics.elementology.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DecompositionTableBlockEntity extends AbstractCrafterBlockEntity {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    public DecompositionTableBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DECOMPOSITION_TABLE_BE.get(), pos, blockState);
    }

    @Override
    protected int getMaxProgress() {
        return 100;
    }

    @Override
    protected int getContainerSize() {
        return 2;
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
    protected void craftItem() {
        ItemStack result = new ItemStack(ModItems.MIXED_ELEMENT.get());
        result.setTag(Utils.generateElementComposition(0.5f,1,0,0));
        this.itemHandler.extractItem(INPUT_SLOT, 1, false);
        this.itemHandler.setStackInSlot(INPUT_SLOT, new ItemStack(Items.BUCKET));
        this.itemHandler.setStackInSlot(OUTPUT_SLOT, result);
    }

    @Override
    protected boolean hasRecipe() {
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

    @Override
    protected void increaseCraftingProgress() {
        this.progress++;
    }

    @Override
    protected boolean progressFinished() {
        return this.progress >= this.maxProgress;
    }

    @Override
    protected void resetProgress() {
        this.progress = 0;
    }
}
