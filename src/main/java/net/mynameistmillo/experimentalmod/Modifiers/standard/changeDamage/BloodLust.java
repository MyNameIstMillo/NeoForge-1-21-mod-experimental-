package net.mynameistmillo.experimentalmod.Modifiers.standard.changeDamage;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Interface.IModifier;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsI;

public class BloodLust extends Item implements IModifier {

    public BloodLust(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack applyChanges(Level level, ItemStack stack) {
        if (level.isClientSide()) return null;
        if (!(stack.getItem() instanceof IProjectile)) return null;

        ProjStatsF statsF = ProjStatsF.loadStatsFromProj(stack);
        ProjStatsI statsI = ProjStatsI.loadStatsFromProj(stack);

        float damage = statsF.get(StatsF.NORMAL_DAMAGE) + 3.0f;
        float gravity = statsF.get(StatsF.FORCE_Y) - 0.02f;

        statsF.set(StatsF.NORMAL_DAMAGE, damage);
        statsF.set(StatsF.FORCE_Y, gravity);

        statsI.set(StatsI.FRIENDLY_FIRE, 1);

        return ProjStatsF.saveStatsToProj(statsF,ProjStatsI.saveStatsToProj(statsI, stack));
    }
}