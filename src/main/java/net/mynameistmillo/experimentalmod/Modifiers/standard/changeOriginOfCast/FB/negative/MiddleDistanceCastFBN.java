package net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.FB.negative;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Interface.IModifier;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiddleDistanceCastFBN extends Item implements IModifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public MiddleDistanceCastFBN(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack applyChanges(Level level, ItemStack stack) {
        if (level.isClientSide()) return null;
        if (!(stack.getItem() instanceof IProjectile)) return null;

        ProjStatsF stats = ProjStatsF.loadStatsFromProj(stack);

        float displacement = stats.get(StatsF.SHIFT_FB) - 3f;

        stats.set(StatsF.SHIFT_FB, displacement);

        return ProjStatsF.saveStatsToProj(stats, stack);
    }
}