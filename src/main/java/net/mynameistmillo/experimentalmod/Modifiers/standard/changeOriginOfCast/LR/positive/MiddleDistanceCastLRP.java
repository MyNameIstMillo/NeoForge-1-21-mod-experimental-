package net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.LR.positive;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Interface.IModifier;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsF;

public class MiddleDistanceCastLRP extends Item implements IModifier {

    public MiddleDistanceCastLRP(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack applyChanges(Level level, ItemStack stack) {
        if (level.isClientSide()) return null;
        if (!(stack.getItem() instanceof IProjectile)) return null;

        ProjStatsF stats = ProjStatsF.loadStatsFromProj(stack);

        float displacement = stats.get(StatsF.SHIFT_LR) + 3f;

        stats.set(StatsF.SHIFT_LR, displacement);

        return ProjStatsF.saveStatsToProj(stats, stack);
    }
}