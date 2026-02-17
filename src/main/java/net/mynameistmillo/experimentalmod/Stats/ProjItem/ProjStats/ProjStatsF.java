package net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsF;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;

public class ProjStatsF implements INBTSerializable<CompoundTag> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);
    private final EnumMap<StatsF, Float> map = new EnumMap<StatsF, Float>(StatsF.class);

    public ProjStatsF(){
        for (StatsF k : StatsF.values()){
            map.put(k, k.getDefaultValue());
        }
    }

    public float get(StatsF key){
        return map.getOrDefault(key, key.getDefaultValue());
    }

    public void set(StatsF key, float value){
        map.put(key, value);
    }

    public ProjStatsF copy(){
        ProjStatsF stats = new ProjStatsF();
        for (StatsF key : StatsF.values()) stats.set(key, this.get(key));
        return stats;
    }


    public static ItemStack saveStatsToProj(ProjStatsF stats, ItemStack stack){
        CompoundTag tag = new CompoundTag();
        for (StatsF key : StatsF.values()){
            tag.putFloat(key.name(), stats.get(key));
        }
        stack.set(ModDataComponents.SPELL_STATS_F.get(), tag);
        return stack;
    }

    public static ProjStatsF loadStatsFromProj(ItemStack stack){
        CompoundTag tag = stack.getOrDefault(ModDataComponents.SPELL_STATS_F.get(), new CompoundTag());
        
        ProjStatsF stats = new ProjStatsF();
        for (StatsF key : StatsF.values()){
            if (tag.contains(key.name())) {
                stats.set(key, tag.getFloat(key.name()));
            }
            else {
                stats.set(key, key.getDefaultValue());
            }
        }
        return stats;
    }

    public ItemStack resetStats(ItemStack stack){
        if (!(stack.getItem() instanceof IProjectile iProjectile)) return null;
        ProjStatsF stats = iProjectile.getBaseStatsF().copy();

        CompoundTag tag = new CompoundTag();
        for (StatsF key : StatsF.values()) {
            tag.putFloat(key.name(), stats.get(key));
        }
        stack.set(ModDataComponents.SPELL_STATS_F.get(), tag);
        return stack;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<StatsF, Float> e : map.entrySet()){
            tag.putFloat(e.getKey().getId(), e.getValue());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        for (StatsF k : StatsF.values()){
            if (nbt.contains(k.getId())){
                map.put(k, nbt.getFloat(k.getId()));
            }else {
                map.put(k, k.getDefaultValue());
            }
        }
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
