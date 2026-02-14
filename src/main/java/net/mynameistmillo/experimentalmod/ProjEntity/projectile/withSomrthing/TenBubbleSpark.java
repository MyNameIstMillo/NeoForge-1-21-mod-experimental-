package net.mynameistmillo.experimentalmod.ProjEntity.projectile.withSomrthing;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mynameistmillo.experimentalmod.Interface.IMultipleSpells;
import net.mynameistmillo.experimentalmod.items.ModItems;

import java.util.List;

public class TenBubbleSpark extends Item implements IMultipleSpells {
    public TenBubbleSpark(Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> addSpells() {
        ItemStack a = new ItemStack(ModItems.BUBBLE_SPARK.get());
        return List.of(a, a, a, a, a, a, a, a, a, a);
    }
}
