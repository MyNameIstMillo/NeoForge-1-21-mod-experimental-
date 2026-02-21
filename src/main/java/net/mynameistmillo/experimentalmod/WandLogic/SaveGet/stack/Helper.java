package net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack;

import net.minecraft.world.item.ItemStack;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsI;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static List<ItemStack> ChangeCastPosToPos(List<ItemStack> in){
        List<ItemStack> out = new ArrayList<>();
        for(ItemStack stack : in){
            if(!(stack.getItem() instanceof IProjectile)) continue;
            ProjStatsI stats = ProjStatsI.loadStatsFromProj(stack);
            stats.set(StatsI.CAST_POS, 1);
            out.add(ProjStatsI.saveStatsToProj(stats, stack));
        }
        return out;
    }
}
