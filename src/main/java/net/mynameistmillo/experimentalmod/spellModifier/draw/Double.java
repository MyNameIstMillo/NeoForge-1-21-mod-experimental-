package net.mynameistmillo.experimentalmod.spellModifier.draw;

import net.minecraft.world.item.Item;
import net.mynameistmillo.experimentalmod.spellLogic.IDraw;
import net.mynameistmillo.experimentalmod.spellLogic.drawLogic.DrawKey;
import net.mynameistmillo.experimentalmod.spellLogic.drawLogic.DrawStats;




public class Double extends Item implements IDraw {

    public final DrawStats drawStats;

    public Double(Properties properties) {
        super(properties);
        this.drawStats = new DrawStats();
        this.drawStats.set(DrawKey.DRAW, 2);
    }

    @Override
    public DrawStats getBaseDrawStats() {
        return drawStats;
    }

}
