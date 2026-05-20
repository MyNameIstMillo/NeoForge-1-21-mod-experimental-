package net.mynameistmillo.experimentalmod.ProjEntity.projectile.multipleInOneStack;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mynameistmillo.experimentalmod.Interface.IMultipleSpells;
import net.mynameistmillo.experimentalmod.items.ModItems;

import java.util.ArrayList;
import java.util.List;

public class Infestation extends Item implements IMultipleSpells {

    public Infestation(Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> addSpells() {
        ItemStack p = new ItemStack(ModItems.INFESTATION_SINGLE.get());
        ItemStack d = new ItemStack(ModItems.TEN_CAST.get());
        List<ItemStack> list = new ArrayList<>();

        list.add(d);
        for (int i = 0; i < 10; i++) {
            list.add(p.copy());
        }
        return list;
    }
}
