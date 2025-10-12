package net.mynameistmillo.experimentalmod.spellLogic.drawLogic;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import net.mynameistmillo.experimentalmod.spellLogic.IDraw;
import net.neoforged.neoforge.common.util.INBTSerializable;
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

    public DrawStats copy(){
        DrawStats drawStats = new DrawStats();
        for (DrawKey key : DrawKey.values()) drawStats.set(key, this.get(key));
        return drawStats;
    }

    public ItemStack saveStatsDraw(DrawStats stats, ItemStack stack){
        CompoundTag tag = new CompoundTag();
        for (DrawKey key : DrawKey.values()){
            tag.putInt(key.name(), stats.get(key));
        }
        stack.set(ModDataComponents.DRAW_STATS.get(), tag);
        return stack;
    }

    public DrawStats loadStatsFromDraw(ItemStack stack){
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
        LOGGER.info("tag -> {}", tag);
        return stack;
    }


    public ItemStack saveModifiersIntoStack(Level level, ItemStack modifier, ItemStack drawStack){
        List<ItemStack> list = loadModifiersFormStack(level, drawStack);
        list.add(modifier);

        ListTag listTag = new ListTag();

        for (ItemStack stack : list){
            CompoundTag tag = new CompoundTag();
            stack.save(level.registryAccess(), tag);
            tag.putString("id", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            tag.putByte("Count", (byte) stack.getCount());

            listTag.add(tag);
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put("Modifiers", listTag);
        drawStack.set(ModDataComponents.SAVED_MODIFIERS.get(), rootTag);

        return drawStack;
    }


    public List<ItemStack> loadModifiersFormStack(Level level, ItemStack drawStack){
        CompoundTag compoundTag = drawStack.getOrDefault(ModDataComponents.SAVED_MODIFIERS.get(), new CompoundTag());
        List<ItemStack> list = new ArrayList<>();

        if(compoundTag != null && compoundTag.contains("Modifiers", ListTag.TAG_LIST)){
            ListTag listTag = compoundTag.getList("Modifiers", Tag.TAG_COMPOUND);

            for (int i=0; i<listTag.size(); i++){
                CompoundTag tag = listTag.getCompound(i);
                ItemStack stack = ItemStack.parseOptional(level.registryAccess(), tag);
                list.add(stack);
            }
        }
        return list;
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
