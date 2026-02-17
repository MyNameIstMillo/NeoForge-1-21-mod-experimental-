package net.mynameistmillo.experimentalmod.Modifiers.normal.changeTime;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Interface.IModifier;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LifeTimeUp extends Item implements IModifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public LifeTimeUp(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack applyChanges(Level level, ItemStack stack) {
        if (level.isClientSide()) return null;
        if (!(stack.getItem() instanceof IProjectile)) return null;

        ProjStatsI stats = ProjStatsI.loadStatsFromProj(stack);

        int lifeTIme = stats.get(StatsI.LIFETIME) + 30;

        stats.set(StatsI.LIFETIME, lifeTIme);

        return ProjStatsI.saveStatsToProj(stats, stack);
    }
}