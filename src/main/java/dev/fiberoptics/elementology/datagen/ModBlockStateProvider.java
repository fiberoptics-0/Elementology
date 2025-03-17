package dev.fiberoptics.elementology.datagen;

import dev.fiberoptics.elementology.Elementology;
import dev.fiberoptics.elementology.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Elementology.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        modelBlockWithItem(ModBlocks.DECOMPOSITION_TABLE);
    }

    private void modelBlockWithItem(RegistryObject<Block> block) {
        simpleBlockWithItem(block.get(),new ModelFile.UncheckedModelFile(new ResourceLocation(Elementology.MODID,
                "block/"+block.getId().getPath())));
    }
}
