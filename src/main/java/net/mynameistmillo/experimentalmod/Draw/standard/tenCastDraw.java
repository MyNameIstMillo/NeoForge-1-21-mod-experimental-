package net.mynameistmillo.experimentalmod.Draw.standard;

import net.minecraft.world.item.Item;
import net.mynameistmillo.experimentalmod.Interface.IDraw;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.DrawKey;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.DrawStats;

public class tenCastDraw extends Item implements IDraw {

    public final DrawStats drawStats;

    public tenCastDraw(Properties properties) {
        super(properties);
        this.drawStats = new DrawStats();
        this.drawStats.set(DrawKey.DRAW, 10);
        this.drawStats.set(DrawKey.FREE_SPACE, 10);
    }

    @Override
    public DrawStats getBaseDrawStats() {
        return drawStats;
    }
}