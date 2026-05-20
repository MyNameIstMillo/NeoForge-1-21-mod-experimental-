package net.mynameistmillo.experimentalmod.Modifiers.standard.changeSpread;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Interface.IModifier;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsF;

public class ReduceHorizontalSpread extends Item implements IModifier {

    public ReduceHorizontalSpread(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack applyChanges(Level level, ItemStack stack) {
        if (level.isClientSide()) return null;
        if (!(stack.getItem() instanceof IProjectile)) return null;

        ProjStatsF stats = ProjStatsF.loadStatsFromProj(stack);

        float mod = stats.get(StatsF.HORIZONTAL_SPREAD) - 20.0f;

        stats.set(StatsF.HORIZONTAL_SPREAD, mod);

        return ProjStatsF.saveStatsToProj(stats, stack);
    }
}