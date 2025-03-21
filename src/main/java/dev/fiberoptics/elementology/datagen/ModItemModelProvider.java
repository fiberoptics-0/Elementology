package dev.fiberoptics.elementology.datagen;

import dev.fiberoptics.elementology.Elementology;
import dev.fiberoptics.elementology.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Elementology.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.FIRE_ELEMENT);
        simpleItem(ModItems.WATER_ELEMENT);
        simpleItem(ModItems.AIR_ELEMENT);
        simpleItem(ModItems.EARTH_ELEMENT);
        simpleItem(ModItems.MIXED_ELEMENT);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Elementology.MODID, "item/"+item.getId().getPath()));
    }
}
