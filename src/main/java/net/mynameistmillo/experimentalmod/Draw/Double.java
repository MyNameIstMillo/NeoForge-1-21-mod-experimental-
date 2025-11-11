package net.mynameistmillo.experimentalmod.Draw;

import net.minecraft.world.item.Item;
import net.mynameistmillo.experimentalmod.Interface.IDraw;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.DrawKey;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.DrawStats;




public class Double extends Item implements IDraw {

    public final DrawStats drawStats;

    public Double(Properties properties) {
        super(properties);
        this.drawStats = new DrawStats();
        this.drawStats.set(DrawKey.DRAW, 2);
        this.drawStats.set(DrawKey.FREE_SPACE, 2);
    }

    @Override
    public DrawStats getBaseDrawStats() {
        return drawStats;
    }

}
