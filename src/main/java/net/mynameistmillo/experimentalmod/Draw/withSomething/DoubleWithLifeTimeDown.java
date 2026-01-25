package net.mynameistmillo.experimentalmod.Draw.withSomething;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mynameistmillo.experimentalmod.Interface.IMultipleSpells;
import net.mynameistmillo.experimentalmod.items.ModItems;

import java.util.List;

public class DoubleWithLifeTimeDown extends Item implements IMultipleSpells {


    public DoubleWithLifeTimeDown(Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> addSpells() {
        ItemStack mod = new ItemStack(ModItems.LIFE_TIME_DOWN.get());
        ItemStack draw = new ItemStack(ModItems.DOUBLE.get());
        return List.of(mod, draw);
    }
}