package net.mynameistmillo.experimentalmod.Stats.DrawItem;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.WandLogic.Types.DrawOrTriggerType;
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


    public static ItemStack saveModOrProjIntoDrawOrTriggerTypeType(Level level,
                                                                   @Nullable ItemStack modOrProj,
                                                                   @Nullable List<ItemStack> moreMOP,
                                                                   @Nullable List<ItemStack> resetAndSave,
                                                                   ItemStack DrawOrTrigger,
                                                                   ModOrProjType MOP, DrawOrTriggerType DOT){
        //never SAVE: MOD -> TRIGGER
        if (MOP== ModOrProjType.MOD && DOT==DrawOrTriggerType.TRIGGER) return DrawOrTrigger;

        List<ItemStack> list = new ArrayList<>();

        //when PROJ -> TRIGGER
        if (MOP== ModOrProjType.PROJ && DOT==DrawOrTriggerType.TRIGGER){
            if (modOrProj != null) list.add(modOrProj);
            if (moreMOP != null) list.addAll(moreMOP);
        }

        //when MOD or PROJ -> DRAW
        else {
            list = loadModOrProjFormDrawOrTriggerTypeType(level, DrawOrTrigger, MOP, DrawOrTriggerType.DRAW);
            if (modOrProj != null) list.add(modOrProj);
            if (moreMOP != null) list.addAll(moreMOP);
            if (resetAndSave != null) list = resetAndSave;
        }

        ListTag listTag = new ListTag();
        for (ItemStack stack : list){
            CompoundTag tag = new CompoundTag();
            stack.save(level.registryAccess(), tag);
            tag.putString("id", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            tag.putByte("Count", (byte) stack.getCount());
            if (stack.getItem() instanceof IProjectile && MOP== ModOrProjType.PROJ) {
                tag.put("ThisProjStats", stack.getOrDefault(ModDataComponents.SPELL_STATS_F.get(), new CompoundTag()));
            }
            listTag.add(tag);
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put(MOP.getId(), listTag);
        LOGGER.info("rootTag -> {}", rootTag);

        //save in DRAW
        if (DOT==DrawOrTriggerType.DRAW) {
            switch (MOP) {
                case MOD -> DrawOrTrigger.set(ModDataComponents.DRAW_MOD_SAVED.get(), rootTag);
                case PROJ -> DrawOrTrigger.set(ModDataComponents.DRAW_PROJ_SAVED.get(), rootTag);
            }
        }
        //save in TRIGGER
        else DrawOrTrigger.set(ModDataComponents.TRIGGER_PROJ_SAVED.get(), rootTag);

        return DrawOrTrigger;
    }


    public static List<ItemStack> loadModOrProjFormDrawOrTriggerTypeType(Level level, ItemStack fromStack,
                                                                         ModOrProjType MOP, DrawOrTriggerType DOT){
        if (MOP== ModOrProjType.MOD && DOT==DrawOrTriggerType.TRIGGER) return null;
        CompoundTag cT = new CompoundTag();
        if(DOT==DrawOrTriggerType.DRAW) {
            switch (MOP) {
                case MOD -> cT = fromStack.get(ModDataComponents.DRAW_MOD_SAVED.get());
                case PROJ -> cT = fromStack.get(ModDataComponents.DRAW_PROJ_SAVED.get());
            }
        }
        else cT = fromStack.get(ModDataComponents.TRIGGER_PROJ_SAVED.get());

        List<ItemStack> list = new ArrayList<>();

        if(cT != null && cT.contains(MOP.getId(), ListTag.TAG_LIST)){
            ListTag listTag = cT.getList(MOP.getId(), Tag.TAG_COMPOUND);

            for (int i=0; i<listTag.size(); i++){
                CompoundTag tag = listTag.getCompound(i);
                ItemStack stack = ItemStack.parseOptional(level.registryAccess(), tag);
                if (stack.getItem() instanceof IProjectile && MOP== ModOrProjType.PROJ) {
                    CompoundTag statsProj = tag.getCompound("ThisProjStats");
                    stack.set(ModDataComponents.SPELL_STATS_F.get(), statsProj);
                }
                list.add(stack);
            }
        }
        return list;
    }

    public static ItemStack transferContentsDrawDrawType(Level level, ItemStack fromDraw, ItemStack finalDraw, ModOrProjType type){
        List<ItemStack> list = loadModOrProjFormDrawOrTriggerTypeType(level, fromDraw, type, DrawOrTriggerType.DRAW);
        return saveModOrProjIntoDrawOrTriggerTypeType(level, null, list,null, finalDraw, type, DrawOrTriggerType.DRAW);
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
