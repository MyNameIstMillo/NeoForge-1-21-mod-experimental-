package net.mynameistmillo.experimentalmod.Modifiers.DistanceCast;

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

public class MiddleDistanceCastFB extends Item implements IModifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public MiddleDistanceCastFB(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack applyChanges(Level level, ItemStack stack) {
        if (level.isClientSide()) return null;
        if (!(stack.getItem() instanceof IProjectile)) return null;

        ProjStatsF stats = new ProjStatsF().loadStatsFromStack(stack);

        float displacement = stats.get(StatsKeyF.DISPLACEMENT_F_B) + 4f;

        stats.set(StatsKeyF.DISPLACEMENT_F_B, displacement);

        return stats.saveStatsToSpell(stats, stack);
    }
}