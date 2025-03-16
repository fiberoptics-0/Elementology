package dev.fiberoptics.elementology;

import dev.fiberoptics.elementology.block.ModBlocks;
import dev.fiberoptics.elementology.item.ModItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Elementology.MODID)
public class Elementology {
    public static final String MODID = "elementology";

    public Elementology() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        eventBus.addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DECOMPOSITION_TABLE.get(), RenderType.cutout());
    }
}
