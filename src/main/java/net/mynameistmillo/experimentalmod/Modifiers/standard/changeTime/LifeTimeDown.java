package net.mynameistmillo.experimentalmod.Modifiers.standard.changeTime;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Interface.IModifier;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsI;

public class LifeTimeDown extends Item implements IModifier {

    public LifeTimeDown(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack applyChanges(Level level, ItemStack stack) {
        if (level.isClientSide()) return null;
        if (!(stack.getItem() instanceof IProjectile)) return null;

        ProjStatsI stats = ProjStatsI.loadStatsFromProj(stack);

        int lifeTime = stats.get(StatsI.LIFETIME) - 20;

        stats.set(StatsI.LIFETIME, lifeTime);

        return ProjStatsI.saveStatsToProj(stats, stack);
    }
}