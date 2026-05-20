package net.mynameistmillo.experimentalmod.Stats.DrawItem;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Enum.ModOrProj;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.GetStackFromStack;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.SaveStackIntoStack;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import net.mynameistmillo.experimentalmod.Interface.IDraw;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class DrawStats implements INBTSerializable<CompoundTag> {
    private final EnumMap<DrawKey, Integer> map = new EnumMap<DrawKey, Integer>(DrawKey.class);

    public DrawStats(){
        for (DrawKey k : DrawKey.values()){
            map.put(k, k.getDefaultValue());
        }
    }

    public int get(DrawKey key){
        return map.getOrDefault(key, key.getDefaultValue());
    }

    public void set(DrawKey key, int value){
        map.put(key, value);
    }

    public static ItemStack subtractFromFree(ItemStack draw){
        DrawStats s = loadStatsFromDraw(draw);
        s.set(DrawKey.FREE_SPACE, s.get(DrawKey.FREE_SPACE)-1);
        return saveStatsDraw(s, draw);
    }

    public static ItemStack increaseFreeByOtherDraw(ItemStack before, ItemStack end){
        DrawStats stats = DrawStats.loadStatsFromDraw(before);
        stats.set(DrawKey.FREE_SPACE, DrawStats.loadStatsFromDraw(end).get(DrawKey.FREE_SPACE));
        return DrawStats.saveStatsDraw(stats, before);
    }

    public DrawStats copy(){
        DrawStats drawStats = new DrawStats();
        for (DrawKey key : DrawKey.values()) drawStats.set(key, this.get(key));
        return drawStats;
    }

    public static ItemStack saveStatsDraw(DrawStats stats, ItemStack stack){
        CompoundTag tag = new CompoundTag();
        for (DrawKey key : DrawKey.values()){
            tag.putInt(key.name(), stats.get(key));
        }
        stack.set(ModDataComponents.DRAW_STATS.get(), tag);
        return stack;
    }

    public static DrawStats loadStatsFromDraw(ItemStack stack){
        CompoundTag tag = stack.getOrDefault(ModDataComponents.DRAW_STATS.get(), new CompoundTag());

        DrawStats stats = new DrawStats();
        for (DrawKey key : DrawKey.values()){
            if (tag.contains(key.name())){
                stats.set(key, tag.getInt(key.name()));
            }
            else {
                stats.set(key, key.getDefaultValue());
            }
        }
        return stats;
    }

    public ItemStack resetsStats(ItemStack stack){
        if (!(stack.getItem() instanceof IDraw iDraw)) return null;
        DrawStats stats = iDraw.getBaseDrawStats().copy();

        CompoundTag tag = new CompoundTag();
        for (DrawKey key : DrawKey.values()){
            tag.putInt(key.name(), stats.get(key));
        }
        stack.set(ModDataComponents.DRAW_STATS.get(), tag);
        return stack;
    }

    public static ItemStack transferContentsDrawDrawType(Level level, ItemStack fromDraw, ItemStack finalDraw, ModOrProj type){
        List<ItemStack> list = GetStackFromStack.stackFromDraw(level, fromDraw, type);
        return SaveStackIntoStack.stackToDraw(level, null, list,null, finalDraw, type);
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<DrawKey, Integer> e : map.entrySet()){
            tag.putInt(e.getKey().getId(), e.getValue());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        for (DrawKey key : DrawKey.values()){
            if (nbt.contains(key.getId())){
                map.put(key, nbt.getInt(key.getId()));
            }else {
                map.put(key, key.getDefaultValue());
            }
        }
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
