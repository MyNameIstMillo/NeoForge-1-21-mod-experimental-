package net.mynameistmillo.experimentalmod.spellModifier.draw;

import net.minecraft.world.item.Item;
import net.mynameistmillo.experimentalmod.LogicStats.Interface.IDraw;
import net.mynameistmillo.experimentalmod.LogicStats.drawItemStats.DrawKey;
import net.mynameistmillo.experimentalmod.LogicStats.drawItemStats.DrawStats;




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
