package net.mynameistmillo.experimentalmod.Modifiers.standard.RestrictToPlane;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Interface.IModifier;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsI;

public class PlaneXZ extends Item implements IModifier {

    public PlaneXZ(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack applyChanges(Level level, ItemStack stack) {
        if (level.isClientSide()) return null;
        if (!(stack.getItem() instanceof IProjectile)) return null;

        ProjStatsI stats = ProjStatsI.loadStatsFromProj(stack);

        stats.set(StatsI.RES_PLANE, 2);

        return ProjStatsI.saveStatsToProj(stats, stack);
    }
}