package dev.fiberoptics.elementology.util;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static CompoundTag generateElementComposition(float earth, float water, float fire, float air) {
        CompoundTag tag = new CompoundTag();
        CompoundTag composition = new CompoundTag();
        composition.putFloat("earth", earth);
        composition.putFloat("water", water);
        composition.putFloat("fire", fire);
        composition.putFloat("air", air);
        tag.put("element_composition", composition);
        return tag;
    }
    public static List<Float> deserializeElementComposition(CompoundTag tag) {
        if(!tag.contains("element_composition")) return null;
        List<Float> list = new ArrayList<Float>();
        CompoundTag composition = tag.getCompound("element_composition");
        list.add(composition.getFloat("earth"));
        list.add(composition.getFloat("water"));
        list.add(composition.getFloat("fire"));
        list.add(composition.getFloat("air"));
        return list;
    }
}
