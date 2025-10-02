package net.mynameistmillo.experimentalmod.spellModifier.speed;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.spellLogic.IModifier;
import net.mynameistmillo.experimentalmod.spellLogic.ISpell;
import net.mynameistmillo.experimentalmod.spellLogic.stats.StatsKey;

public class SpeedDown extends Item implements IModifier {

    public SpeedDown(Properties properties) {
        super(properties);
    }

    @Override
    public void applyChanges(Level level, ItemStack stack) {
        if (level.isClientSide()) return;

        if (stack.getItem() instanceof ISpell spell){
            float speed = spell.getBaseStats().get(StatsKey.SPEED);
            speed *=0.66f;
            spell.getBaseStats().set(StatsKey.SPEED, speed);
        }

    }
}