package net.mynameistmillo.experimentalmod.spellModifier.speed;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.spellLogic.IModifier;
import net.mynameistmillo.experimentalmod.spellLogic.ISpell;
import net.mynameistmillo.experimentalmod.spellLogic.stats.StatsKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeedUp extends Item implements IModifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public SpeedUp(Properties properties) {
        super(properties);
    }

    @Override
    public void applyChanges(Level level, ItemStack stack) {
        if (level.isClientSide()) return;

        if (stack.getItem() instanceof ISpell spell){
            float speed = spell.getBaseStats().get(StatsKey.SPEED);
            speed *=2;
            spell.getBaseStats().set(StatsKey.SPEED, speed);
        }

    }
}
