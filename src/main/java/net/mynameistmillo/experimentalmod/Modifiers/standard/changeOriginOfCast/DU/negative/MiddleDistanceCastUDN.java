package net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.DU.negative;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Interface.IModifier;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsF;

public class MiddleDistanceCastUDN extends Item implements IModifier {

    public MiddleDistanceCastUDN(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack applyChanges(Level level, ItemStack stack) {
        if (level.isClientSide()) return null;
        if (!(stack.getItem() instanceof IProjectile)) return null;

        ProjStatsF stats = ProjStatsF.loadStatsFromProj(stack);

        float displacement = stats.get(StatsF.SHIFT_UD) - 3f;

        stats.set(StatsF.SHIFT_UD, displacement);

        return ProjStatsF.saveStatsToProj(stats, stack);
    }
}