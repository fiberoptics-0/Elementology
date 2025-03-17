package dev.fiberoptics.elementology.screen;

import com.ibm.icu.impl.Pair;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.fiberoptics.elementology.menu.AbstractCrafterMenu;
import dev.fiberoptics.elementology.menu.DecompositionTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractCrafterScreen<T extends AbstractCrafterMenu> extends AbstractContainerScreen<T> {

    public AbstractCrafterScreen(T menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    protected abstract Pair<Integer,Integer> getProgressBarPosition();

    protected abstract Pair<Integer,Integer> getProgressBarTexturePosition();

    protected abstract int getProgressBarWidth();

    protected abstract ResourceLocation getTexture();

    @Override
    protected void init() {
        super.init();
        this.titleLabelY=Integer.MAX_VALUE;
        this.inventoryLabelY=Integer.MAX_VALUE;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getTexture());
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(getTexture(), x, y, 0, 0, imageWidth, imageHeight);
        renderProgressBar(guiGraphics, x, y);
    }

    private void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(getTexture(),
                    x+this.getProgressBarPosition().first,
                    y+this.getProgressBarPosition().second,
                    this.getProgressBarTexturePosition().first,
                    this.getProgressBarTexturePosition().second,
                    this.getProgressBarWidth(),
                    menu.getScaledProgress());
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
