package dev.fiberoptics.elementology.item;

import dev.fiberoptics.elementology.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MixedElementItem extends Item {

    public MixedElementItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
        if(stack.getTag()!=null && Utils.deserializeElementComposition(stack.getTag())!=null) {
            List<Float> elements = Utils.deserializeElementComposition(stack.getTag());
            tooltips.add(Component.translatable("element.elementology.earth").append(": "+elements.get(0)));
            tooltips.add(Component.translatable("element.elementology.water").append(": "+elements.get(1)));
            tooltips.add(Component.translatable("element.elementology.fire").append(": "+elements.get(2)));
            tooltips.add(Component.translatable("element.elementology.air").append(": "+elements.get(3)));
        }

        super.appendHoverText(stack, level, tooltips, flag);
    }
}
