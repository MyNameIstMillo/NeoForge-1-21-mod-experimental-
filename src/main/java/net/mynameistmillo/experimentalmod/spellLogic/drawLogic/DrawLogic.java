package net.mynameistmillo.experimentalmod.spellLogic.drawLogic;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class DrawLogic implements INBTSerializable<CompoundTag> {
    private final EnumMap<DrawKey, Integer> map = new EnumMap<DrawKey, Integer>(DrawKey.class);

    public DrawLogic(){
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

    public DrawLogic copy(){
        DrawLogic drawLogic = new DrawLogic();
        for (DrawKey key : DrawKey.values()) drawLogic.set(key, this.get(key));
        return drawLogic;
    }

    public void saveModifiersIntoStack(Level level, ItemStack modifier, ItemStack drawStack){
        List<ItemStack> list = loadModifiersFormStack(level, drawStack);
        list.add(modifier);

        ListTag listTag = new ListTag();

        for (int i=0; i<list.size(); i++){
            ItemStack stack = list.get(i);
            CompoundTag tag = new CompoundTag();
            stack.save(level.registryAccess(), tag);
            tag.putString("id", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            tag.putByte("Count", (byte) stack.getCount());

            listTag.add(tag);
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put("Modifiers", listTag);
        drawStack.set(ModDataComponents.SAVED_MODIFIERS.get(), rootTag);
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
}
