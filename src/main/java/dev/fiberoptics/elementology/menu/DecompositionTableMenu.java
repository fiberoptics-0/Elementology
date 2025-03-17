package dev.fiberoptics.elementology.menu;

import com.ibm.icu.impl.Pair;
import dev.fiberoptics.elementology.block.ModBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class DecompositionTableMenu extends AbstractCrafterMenu {
    public DecompositionTableMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId,inv,inv.player.level().getBlockEntity(extraData.readBlockPos()),
                new SimpleContainerData(2));
    }

    public DecompositionTableMenu(int containerId, Inventory inv, BlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.DECOMPOSITION_TABLE_MENU.get(), containerId, inv, blockEntity, data);
    }

    @Override
    protected int getContainerSize() {
        return 2;
    }

    @Override
    protected List<Pair<Integer, Integer>> getSlots() {
        List<Pair<Integer,Integer>> slots = new ArrayList<>();
        slots.add(Pair.of(80, 11));
        slots.add(Pair.of(80, 59));
        return slots;
    }

    @Override
    protected Block getBlock() {
        return ModBlocks.DECOMPOSITION_TABLE.get();
    }

    @Override
    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    @Override
    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressBarSize = 26;
        return maxProgress != 0 ? progressBarSize * progress / maxProgress : 0;
    }
}
