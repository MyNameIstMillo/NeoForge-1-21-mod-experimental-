package net.mynameistmillo.experimentalmod.ProjEntity.projectile.multipleInOneStack;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mynameistmillo.experimentalmod.Interface.IMultipleSpells;
import net.mynameistmillo.experimentalmod.items.ModItems;

import java.util.List;

public class DoubleSparkBolt extends Item implements IMultipleSpells {

    public DoubleSparkBolt(Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> addSpells() {
        ItemStack p = new ItemStack(ModItems.SPARK_BOLT.get());
        return List.of(p.copy(), p.copy());
    }
}
