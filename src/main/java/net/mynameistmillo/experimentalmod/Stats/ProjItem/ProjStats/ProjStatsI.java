package net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsI;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.EnumMap;
import java.util.Map;

public class ProjStatsI implements INBTSerializable<CompoundTag> {
    private final EnumMap<StatsI, Integer> map = new EnumMap<StatsI, Integer>(StatsI.class);

    public ProjStatsI(){
        for (StatsI k : StatsI.values()){
            map.put(k, k.getDefaultValue());
        }
    }

    public int get(StatsI key){
        return map.getOrDefault(key, key.getDefaultValue());
    }

    public void set(StatsI key, int value){
        map.put(key, value);
    }

    public ProjStatsI copy(){
        ProjStatsI stats = new ProjStatsI();
        for (StatsI key : StatsI.values()) stats.set(key, this.get(key));
        return stats;
    }

    public static ItemStack subtractFromFree(ItemStack proj){
        ProjStatsI s = loadStatsFromProj(proj);
        s.set(StatsI.DRAW_TRIGGER, s.get(StatsI.DRAW_TRIGGER)-1);
        return saveStatsToProj(s, proj);
    }

    public static ItemStack saveStatsToProj(ProjStatsI stats, ItemStack stack){
        CompoundTag tag = new CompoundTag();
        for (StatsI key : StatsI.values()){
            tag.putInt(key.getId(), stats.get(key));
        }
        stack.set(ModDataComponents.SPELL_STATS_I.get(), tag);
        return stack;
    }

    public static ProjStatsI loadStatsFromProj(ItemStack stack){
        CompoundTag tag = stack.getOrDefault(ModDataComponents.SPELL_STATS_I.get(), new CompoundTag());

        ProjStatsI stats = new ProjStatsI();
        for (StatsI key : StatsI.values()){
            String id = key.getId();
            stats.set(key, tag.contains(id) ? tag.getInt(id) : key.getDefaultValue());

        }
        return stats;
    }

    public ItemStack resetStats(ItemStack stack){
        if (!(stack.getItem() instanceof IProjectile iProjectile)) return null;
        ProjStatsI stats = iProjectile.getBaseStatsI().copy();

        CompoundTag tag = new CompoundTag();
        for (StatsI key : StatsI.values()) {
            tag.putInt(key.getId(), stats.get(key));
        }
        stack.set(ModDataComponents.SPELL_STATS_I.get(), tag);
        return stack;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<StatsI, Integer> e : map.entrySet()){
            tag.putInt(e.getKey().getId(), e.getValue());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        for (StatsI k : StatsI.values()){
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
