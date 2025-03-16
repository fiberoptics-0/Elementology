package dev.fiberoptics.elementology.block;

import dev.fiberoptics.elementology.Elementology;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Elementology.MODID);

    public static RegistryObject<Block> DECOMPOSITION_TABLE =
            BLOCKS.register("decomposition_table",
                    () -> new DecompositionTableBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
