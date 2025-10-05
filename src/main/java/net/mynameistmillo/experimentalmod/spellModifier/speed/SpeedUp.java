package net.mynameistmillo.experimentalmod.spellModifier.speed;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.spellLogic.IModifier;
import net.mynameistmillo.experimentalmod.spellLogic.ISpell;
import net.mynameistmillo.experimentalmod.spellLogic.stats.SpellStats;
import net.mynameistmillo.experimentalmod.spellLogic.stats.StatsKey;
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
        if (!(stack.getItem() instanceof ISpell spell)) return null;

        SpellStats stats = new SpellStats();
        stats = stats.loadStatsFromStack(stack);

        float speed = stats.get(StatsKey.SPEED);
        speed *= 2;
        stats.set(StatsKey.SPEED, speed);

        return stats.saveStatsToSpell(stats, stack);

    }
}
