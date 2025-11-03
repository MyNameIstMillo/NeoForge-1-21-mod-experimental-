package net.mynameistmillo.experimentalmod.spellModifier.speed;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.LogicStats.Interface.IModifier;
import net.mynameistmillo.experimentalmod.LogicStats.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.LogicStats.projItemStats.SpellStats;
import net.mynameistmillo.experimentalmod.LogicStats.projItemStats.StatsKey;
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

        SpellStats stats = new SpellStats();
        stats = stats.loadStatsFromStack(stack);

        float speed = stats.get(StatsKey.SPEED);
        speed *= 2;
        stats.set(StatsKey.SPEED, speed);

        return stats.saveStatsToSpell(stats, stack);

    }
}
