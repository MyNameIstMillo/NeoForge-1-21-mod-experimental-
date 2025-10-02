package net.mynameistmillo.experimentalmod.spellLogic;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IModifier {

    void applyChanges(Level level,
                      ItemStack stack);


}
