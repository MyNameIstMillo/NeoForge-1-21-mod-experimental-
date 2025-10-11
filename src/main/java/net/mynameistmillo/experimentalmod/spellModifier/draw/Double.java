package net.mynameistmillo.experimentalmod.spellModifier.draw;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.spellLogic.IDraw;




public class Double extends Item implements IDraw {
    public Double(Properties properties) {
        super(properties);
    }

    @Override
    public void applyModifiers(Level level, ItemStack proj) {
        if(level.isClientSide()) return;

    }
}
