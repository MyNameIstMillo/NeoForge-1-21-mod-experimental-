package net.mynameistmillo.experimentalmod.ProjEntity.projectile.multipleInOneStack;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mynameistmillo.experimentalmod.Interface.IMultipleSpells;
import net.mynameistmillo.experimentalmod.items.ModItems;

import java.util.ArrayList;
import java.util.List;

public class TenBubbleSpark extends Item implements IMultipleSpells {
    public TenBubbleSpark(Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> addSpells() {
        ItemStack p = new ItemStack(ModItems.BUBBLE_SPARK.get());
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(p.copy());
        }
        return list;
    }
}
