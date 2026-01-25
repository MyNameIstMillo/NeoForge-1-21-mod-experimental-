package net.mynameistmillo.experimentalmod.Modifiers.normal.changeTime;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Interface.IModifier;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF;
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

        ProjStatsF stats = ProjStatsF.loadStatsFromStack(stack);

        float lifeTIme = stats.get(StatsKeyF.LIFETIME) + 30f;

        stats.set(StatsKeyF.LIFETIME, lifeTIme);

        return ProjStatsF.saveStatsToSpell(stats, stack);
    }
}