package net.mynameistmillo.experimentalmod.Interface;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IModifier {

    ItemStack applyChanges(Level level,
                      ItemStack stack);


}
