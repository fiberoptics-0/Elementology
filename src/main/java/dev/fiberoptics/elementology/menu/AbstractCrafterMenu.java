package dev.fiberoptics.elementology.menu;

import com.ibm.icu.impl.Pair;
import dev.fiberoptics.elementology.block.ModBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

import java.util.List;

public abstract class AbstractCrafterMenu extends AbstractContainerMenu {

    public final BlockEntity blockEntity;
    public final Level level;
    public final ContainerData data;

    protected abstract int getContainerSize();

    protected abstract List<Pair<Integer,Integer>> getSlots();

    protected abstract Block getBlock();

    public abstract boolean isCrafting();

    public abstract int getScaledProgress();

    public AbstractCrafterMenu(MenuType<?> menuType, int containerId,
                               Inventory inv, BlockEntity entity, ContainerData data) {
        super(menuType, containerId);
        checkContainerSize(inv,this.getContainerSize());
        this.blockEntity = entity;
        this.level=inv.player.level();
        this.data=data;
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            List<Pair<Integer,Integer>> slots = this.getSlots();
            for (int i = 0; i < slots.size(); i++) {
                this.addSlot(new SlotItemHandler(handler,i,slots.get(i).first,slots.get(i).second));
            }
        });

        addDataSlots(data);
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = this.slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if(index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if(!moveItemStackTo(sourceStack,TE_INVENTORY_FIRST_SLOT_INDEX,
                    TE_INVENTORY_FIRST_SLOT_INDEX + this.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + this.getContainerSize()) {
            if(!moveItemStackTo(sourceStack,VANILLA_FIRST_SLOT_INDEX,
                    VANILLA_FIRST_SLOT_INDEX+VANILLA_SLOT_COUNT,false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
        if(sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level,blockEntity.getBlockPos()),
                player, this.getBlock());
    }

    private void addPlayerInventory(Inventory inv) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv,j+i*9+9,8+j*18,84+i*18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inv) {
        for (int i=0; i<9; i++) {
            this.addSlot(new Slot(inv,i,8+i*18,142));
        }
    }
}
