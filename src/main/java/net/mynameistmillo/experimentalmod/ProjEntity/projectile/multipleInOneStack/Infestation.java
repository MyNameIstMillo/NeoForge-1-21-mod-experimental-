package net.mynameistmillo.experimentalmod.ProjEntity.projectile.multipleInOneStack;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mynameistmillo.experimentalmod.Interface.IMultipleSpells;
import net.mynameistmillo.experimentalmod.items.ModItems;

import java.util.List;

public class Infestation extends Item implements IMultipleSpells {

    public Infestation(Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> addSpells() {
        ItemStack p = new ItemStack(ModItems.INFESTATION_SINGLE.get());
        ItemStack d = new ItemStack(ModItems.TEN_CAST.get());
        return List.of(d.copy(),p.copy(),p.copy(),p.copy(),p.copy(),p.copy(),p.copy(),p.copy(),p.copy(),p.copy(),p.copy());
    }
}
