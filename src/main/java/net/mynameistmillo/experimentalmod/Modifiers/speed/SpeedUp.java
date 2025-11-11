package net.mynameistmillo.experimentalmod.Modifiers.speed;

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

public class SpeedUp extends Item implements IModifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public SpeedUp(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack applyChanges(Level level, ItemStack stack) {
        if (level.isClientSide()) return null;
        if (!(stack.getItem() instanceof IProjectile spell)) return null;

        ProjStatsF stats = new ProjStatsF();
        stats = stats.loadStatsFromStack(stack);

        float speed = stats.get(StatsKeyF.SPEED);
        speed *= 2;
        stats.set(net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF.SPEED, speed);

        return stats.saveStatsToSpell(stats, stack);

    }
}
