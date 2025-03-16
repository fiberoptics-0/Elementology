package dev.fiberoptics.elementology.item;

import dev.fiberoptics.elementology.Elementology;
import dev.fiberoptics.elementology.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Elementology.MODID);

    public static RegistryObject<Item> MIXED_ELEMENT = ITEMS.register("mixed_element",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static RegistryObject<Item> FIRE_ELEMENT = ITEMS.register("fire_element",
            () -> new Item(new Item.Properties()));

    public static RegistryObject<Item> WATER_ELEMENT = ITEMS.register("water_element",
            () -> new Item(new Item.Properties()));

    public static RegistryObject<Item> EARTH_ELEMENT = ITEMS.register("earth_element",
            () -> new Item(new Item.Properties()));

    public static RegistryObject<Item> AIR_ELEMENT = ITEMS.register("air_element",
            () -> new Item(new Item.Properties()));

    public static RegistryObject<BlockItem> DECOMPOSTION_TABLE =
            ITEMS.register("decomposition_table",
                    () -> new BlockItem(ModBlocks.DECOMPOSITION_TABLE.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
