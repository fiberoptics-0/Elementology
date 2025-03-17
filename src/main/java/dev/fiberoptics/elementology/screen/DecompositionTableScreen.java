package dev.fiberoptics.elementology.screen;

import com.ibm.icu.impl.Pair;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.fiberoptics.elementology.Elementology;
import dev.fiberoptics.elementology.menu.AbstractCrafterMenu;
import dev.fiberoptics.elementology.menu.DecompositionTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DecompositionTableScreen extends AbstractCrafterScreen<DecompositionTableMenu> {

    public DecompositionTableScreen(DecompositionTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected Pair<Integer, Integer> getProgressBarPosition() {
        return Pair.of(85,30);
    }

    @Override
    protected Pair<Integer, Integer> getProgressBarTexturePosition() {
        return Pair.of(176,0);
    }

    @Override
    protected int getProgressBarWidth() {
        return 8;
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Elementology.MODID, "textures/gui/decomposition_table.png");
    }
}
