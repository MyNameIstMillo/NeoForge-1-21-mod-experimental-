package net.mynameistmillo.experimentalmod.Modifiers.multipleInOneStack;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mynameistmillo.experimentalmod.Interface.IMultipleSpells;
import net.mynameistmillo.experimentalmod.items.ModItems;

import java.util.List;

public class LifeTimeDownAndSpeedUp extends Item implements IMultipleSpells {

    public LifeTimeDownAndSpeedUp(Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> addSpells() {
        ItemStack mod1 = new ItemStack(ModItems.LIFE_TIME_DOWN.get());
        ItemStack mod2 = new ItemStack(ModItems.SPEED_UP.get());
        return List.of(mod1, mod2);
    }
}
