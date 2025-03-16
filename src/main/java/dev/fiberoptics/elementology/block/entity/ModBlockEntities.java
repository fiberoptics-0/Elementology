package dev.fiberoptics.elementology.block.entity;

import dev.fiberoptics.elementology.Elementology;
import dev.fiberoptics.elementology.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
     public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
             DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Elementology.MODID);

     public static final RegistryObject<BlockEntityType<DecompositionTableBlockEntity>> DECOMPOSITION_TABLE_BE =
             BLOCK_ENTITIES.register("decomposition_table_be", () ->
                     BlockEntityType.Builder.of(DecompositionTableBlockEntity::new,
                             ModBlocks.DECOMPOSITION_TABLE.get()).build(null));

     public static void register(IEventBus eventBus) {
         BLOCK_ENTITIES.register(eventBus);
     }
}
