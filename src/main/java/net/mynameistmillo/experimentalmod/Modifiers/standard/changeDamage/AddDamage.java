package net.mynameistmillo.experimentalmod.Modifiers.standard.changeDamage;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Interface.IModifier;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsF;

public class AddDamage extends Item implements IModifier {

    public AddDamage(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack applyChanges(Level level, ItemStack stack) {
        if (level.isClientSide()) return null;
        if (!(stack.getItem() instanceof IProjectile)) return null;

        ProjStatsF stats = ProjStatsF.loadStatsFromProj(stack);

        float damage = stats.get(StatsF.NORMAL_DAMAGE) + 1.0f;

        stats.set(StatsF.NORMAL_DAMAGE, damage);

        return ProjStatsF.saveStatsToProj(stats, stack);
    }
}