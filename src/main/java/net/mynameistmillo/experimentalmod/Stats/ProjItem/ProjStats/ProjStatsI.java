package net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats;


import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyI;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;

public class ProjStatsI implements INBTSerializable<CompoundTag> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);
    private final EnumMap<StatsKeyI, Integer> map = new EnumMap<StatsKeyI, Integer>(StatsKeyI.class);

    public ProjStatsI(){
        for (StatsKeyI k : StatsKeyI.values()){
            map.put(k, k.getDefaultValue());
        }
    }

    public int get(StatsKeyI key){
        return map.getOrDefault(key, key.getDefaultValue());
    }

    public void set(StatsKeyI key, int value){
        map.put(key, value);
    }

    public ProjStatsI copy(){
        ProjStatsI stats = new ProjStatsI();
        for (StatsKeyI key : StatsKeyI.values()) stats.set(key, this.get(key));
        return stats;
    }


    public ItemStack saveStatsToSpell(ProjStatsI stats, ItemStack stack){
        CompoundTag tag = new CompoundTag();
        for (StatsKeyI key : StatsKeyI.values()){
            tag.putInt(key.name(), stats.get(key));
        }
        stack.set(ModDataComponents.SPELL_STATS_I.get(), tag);
        return stack;
    }

    public ProjStatsI loadStatsFromStack(ItemStack stack){
        CompoundTag tag = stack.getOrDefault(ModDataComponents.SPELL_STATS_I.get(), new CompoundTag());

        ProjStatsI stats = new ProjStatsI();
        for (StatsKeyI key : StatsKeyI.values()){
            if (tag.contains(key.name())) {
                stats.set(key, tag.getInt(key.name()));
            }
            else {
                stats.set(key, key.getDefaultValue());
            }
        }
        return stats;
    }

    public ItemStack resetStats(ItemStack stack){
        if (!(stack.getItem() instanceof IProjectile iProjectile)) return null;
        ProjStatsI stats = iProjectile.getBaseStatsI().copy();

        CompoundTag tag = new CompoundTag();
        for (StatsKeyI key : StatsKeyI.values()) {
            tag.putInt(key.name(), stats.get(key));
        }
        stack.set(ModDataComponents.SPELL_STATS_I.get(), tag);
        return stack;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<StatsKeyI, Integer> e : map.entrySet()){
            tag.putInt(e.getKey().getId(), e.getValue());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        for (StatsKeyI k : StatsKeyI.values()){
            if (nbt.contains(k.getId())){
                map.put(k, nbt.getInt(k.getId()));
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
