package net.mynameistmillo.experimentalmod.Stats.DrawItem;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import net.mynameistmillo.experimentalmod.Interface.IDraw;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class DrawStats implements INBTSerializable<CompoundTag> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);
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


    public static ItemStack saveModOrProjIntoDrawType(Level level, @Nullable ItemStack modOrProj,
                                               @Nullable List<ItemStack> moreMOP, ItemStack draw, SaveOrGetTypeD type){
        List<ItemStack> list = loadModOrProjFormDrawType(level, draw, type);
        if (modOrProj != null) list.add(modOrProj);
        if (moreMOP != null) list.addAll(moreMOP);

        ListTag listTag = new ListTag();

        for (ItemStack stack : list){
            CompoundTag tag = new CompoundTag();
            stack.save(level.registryAccess(), tag);
            tag.putString("id", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            tag.putByte("Count", (byte) stack.getCount());

            listTag.add(tag);
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put(type.getId(), listTag);
        switch (type){
            case MOD -> draw.set(ModDataComponents.DRAW_MOD_SAVED.get(), rootTag);
            case PROJ -> draw.set(ModDataComponents.DRAW_PROJ_SAVED.get(), rootTag);
        }
        return draw;
    }


    public static List<ItemStack> loadModOrProjFormDrawType(Level level, ItemStack draw, SaveOrGetTypeD type){
        CompoundTag cT = new CompoundTag();
        switch (type){
            case MOD -> cT = draw.get(ModDataComponents.DRAW_MOD_SAVED.get());
            case PROJ -> cT = draw.get(ModDataComponents.DRAW_PROJ_SAVED.get());
        }
        List<ItemStack> list = new ArrayList<>();

        if(cT != null && cT.contains(type.getId(), ListTag.TAG_LIST)){
            ListTag listTag = cT.getList(type.getId(), Tag.TAG_COMPOUND);

            for (int i=0; i<listTag.size(); i++){
                CompoundTag tag = listTag.getCompound(i);
                ItemStack stack = ItemStack.parseOptional(level.registryAccess(), tag);
                list.add(stack);
            }
        }
        LOGGER.info("load -> {}", list);
        return list;
    }

    public static ItemStack transferContentsDrawDrawType(Level level, ItemStack fromDraw, ItemStack finalDraw, SaveOrGetTypeD type){
        List<ItemStack> list = loadModOrProjFormDrawType(level, fromDraw, type);
        return saveModOrProjIntoDrawType(level, null, list, finalDraw, type);
    }





    public boolean areSavedModifiers(Level level, ItemStack stack){
        CompoundTag tag = stack.getOrDefault(ModDataComponents.DRAW_STATS.get(), new CompoundTag());
        return (tag != null && tag.contains("Modifiers", ListTag.TAG_LIST));
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
