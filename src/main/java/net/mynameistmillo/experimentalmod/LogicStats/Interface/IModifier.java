package net.mynameistmillo.experimentalmod.LogicStats.Interface;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IModifier {

    ItemStack applyChanges(Level level,
                      ItemStack stack);


}
