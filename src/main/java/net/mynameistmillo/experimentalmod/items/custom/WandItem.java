package net.mynameistmillo.experimentalmod.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import net.mynameistmillo.experimentalmod.spellLogic.IModifier;
import net.mynameistmillo.experimentalmod.spellLogic.ISpell;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WandItem extends Item {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);
    public WandItem(Properties properties, int capacity) {
        super(properties);
        this.defaultCapacity = capacity;
    }

    private final int defaultCapacity;

    public int getCapacity(ItemStack wand) {
        Integer capacity = wand.get(ModDataComponents.WAND_CAPACITY.get());
        if (capacity == null) {
            capacity = defaultCapacity;
            wand.set(ModDataComponents.WAND_CAPACITY.get(), capacity);
        }
        return defaultCapacity;
    }

    public int getCurrentIndex(ItemStack wand){
        Integer capacity = wand.get(ModDataComponents.WAND_CAPACITY_COMPACT.get());
        Integer index = wand.get(ModDataComponents.WAND_INDEX.get());
        if(index == null){
            index = 0;
            wand.set(ModDataComponents.WAND_INDEX.get(), index);
        }
        return Math.floorMod(index, capacity);
    }

    public void setCurrentIndex(ItemStack wand, int index){
        wand.set(ModDataComponents.WAND_INDEX.get(), index);
    }
    public void increaseIndex(ItemStack wand){
        int index = wand.get(ModDataComponents.WAND_INDEX.get());
        int capacity = wand.get(ModDataComponents.WAND_CAPACITY_COMPACT.get());
        index = (index+1)%capacity;
        wand.set(ModDataComponents.WAND_INDEX.get(), index);
    }


    public void saveSpells(ItemStack wand, List<ItemStack> spellList, Level level) {
        int capacity = getCapacity(wand);
        ListTag spellsListTag = new ListTag();

        for (int i = 0; i < capacity; i++) {
            ItemStack spell = spellList.get(i);
            CompoundTag spellTag = new CompoundTag();

            if (!spell.is(Items.DIRT)) {
                spell.save(level.registryAccess(), spellTag);
                spellTag.putString("id", BuiltInRegistries.ITEM.getKey(spell.getItem()).toString());
                spellTag.putByte("Count", (byte) spell.getCount());
            }
            else {
                spellTag.putString("id", "minecraft:dirt");
                spellTag.putByte("Count", (byte) 1);

            }
            spellTag.putString("proj_name", "");
            spellTag.putInt("Slot", i);
            spellsListTag.add(spellTag);
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put("Spells", spellsListTag);

        wand.set(ModDataComponents.WAND_SPELLS.get(), rootTag);
        setCurrentIndex(wand, 0);
        compactSpells(wand, spellList, level);
    }

    public NonNullList<ItemStack> getSavedSpells(ItemStack wand, Level level) {
        int capacity = getCapacity(wand);
        NonNullList<ItemStack> list = NonNullList.withSize(capacity, new ItemStack(Items.DIRT));
        CompoundTag wandSpellsTag = wand.get(ModDataComponents.WAND_SPELLS.get());

        if (wandSpellsTag != null && wandSpellsTag.contains("Spells", ListTag.TAG_LIST)) {
            ListTag listTag = wandSpellsTag.getList("Spells", Tag.TAG_COMPOUND);

            for (int i = 0; i < capacity; i++) {
                CompoundTag spellTag = listTag.getCompound(i);
                int slot = spellTag.getInt("Slot");
                ItemStack spell = ItemStack.parse(level.registryAccess(), spellTag).orElse(new ItemStack(Items.DIRT));

                if(slot >= 0 && slot < list.size()){
                    list.set(slot, spell);
                }
            }
        }
        //LOGGER.info("getSavedSpells -> list -> {}", list);
        return list;
    }

    public boolean areThereSpellsInWand(ItemStack wand, Level level) {
        if (!(wand.getItem() instanceof WandItem wandItem)) {
            return false;
        }
        NonNullList<ItemStack> contents = wandItem.getSavedSpells(wand, level);

        for (ItemStack stack : contents) {

            if (!stack.is(Items.DIRT)) {
                return true;
            }
        }
        return false;
    }


    public List<ItemStack> destroyEmpty(List<ItemStack> list){
        List<ItemStack> nonEmpty = new ArrayList<>();
        for(ItemStack stack : list){
            if(stack.is(Items.DIRT)) continue;
            nonEmpty.add(stack);
        }
        return nonEmpty;
    }

    public void compactSpells(ItemStack wand, List<ItemStack> list, Level level){
         List<ItemStack> allList = destroyEmpty(list);
         List<ItemStack> spellList = new ArrayList<>();

         int index = 0;
         for (ItemStack stack : allList){
             if (stack.getItem() instanceof IModifier modifier){
                 applyModifierToNextSpell(allList, modifier, index, level);

             }
             if (stack.getItem() instanceof ISpell) spellList.add(stack);
             index++;
         }
        saveCompactSpells(wand, spellList, level);
    }

    public void applyModifierToNextSpell(List<ItemStack> list, IModifier modifier, int index, Level level){
        for (int i = index; i<list.size(); i++){
            if (list.get(i).getItem() instanceof ISpell){
                modifier.applyChanges(level, list.get(i));
                break;
            }
        }

    }

    public void saveCompactSpells(ItemStack wand, List<ItemStack> listS, Level level){
        ListTag listTag = new ListTag();
        int count = 0;

        for (ItemStack stack : listS){
            CompoundTag tag = new CompoundTag();
            stack.save(level.registryAccess(), tag);
            tag.putString("id", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            tag.putByte("Count", (byte) stack.getCount());
            tag.putString("proj_name", "");
            listTag.add(tag);
            count++;

        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put("Spells", listTag);
        wand.set(ModDataComponents.WAND_SPELLS_COMPACT.get(), rootTag);
        wand.set(ModDataComponents.WAND_CAPACITY_COMPACT.get(), count);
    }

    public List<ItemStack> getCompactSpells(ItemStack wand, Level level){
        List<ItemStack> list = new ArrayList<>();
        CompoundTag wandSpells = wand.get(ModDataComponents.WAND_SPELLS_COMPACT.get());
        int capacity = wand.get(ModDataComponents.WAND_CAPACITY_COMPACT.get());

        if (wandSpells != null && wandSpells.contains("Spells", ListTag.TAG_LIST)){
            ListTag listTag = wandSpells.getList("Spells", Tag.TAG_COMPOUND);

            for (int i=0; i<capacity; i++){
                CompoundTag tag = listTag.getCompound(i);
                ItemStack stack = ItemStack.parseOptional(level.registryAccess(), tag);
                list.add(stack);
            }
        }
        return list;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack wand = player.getItemInHand(usedHand);
        if(level.isClientSide()){return InteractionResultHolder.pass(wand);}

        int capacity = getCapacity(wand);
        int index = getCurrentIndex(wand);
        List<ItemStack> storedSpells = getCompactSpells(wand, level);

        ItemStack currentSpell = storedSpells.get(index);


//        int checked=0;
//        ItemStack currentSpell = ItemStack.EMPTY;
//        while(checked < capacity){
//                currentSpell = storedSpells.get(index);
//                if(!currentSpell.is(Items.DIRT)){
//                        break;
//                    }
//                index = (index+1)%capacity;
//            }

        if(currentSpell.getItem() instanceof ISpell spellCast){

            Entity entity = spellCast.spawnSpell(level, player.getOnPos(), player, player.getLookAngle(), wand, currentSpell, index);
            Explosion explosion = spellCast.createExplosion(level, player.getOnPos(), player, wand);


            increaseIndex(wand);

           return InteractionResultHolder.success(wand);
        }
        return InteractionResultHolder.pass(wand);
    }

    @Override
    public void appendHoverText(ItemStack wand, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        Level level = context.level();
        int capacity = getCapacity(wand);

        if (Screen.hasShiftDown()){
            NonNullList<ItemStack> spells = getSavedSpells(wand, level);
            tooltip.add(Component.literal(" Spells:").withStyle(ChatFormatting.GRAY));


            for(int i=0; i<capacity; i++){
                if (!spells.get(i).is(Items.DIRT)) {
                    tooltip.add(Component.literal(i+1 + ": " + spells.get(i).getHoverName().getString()).withStyle(ChatFormatting.GRAY));
                }
                else{
                    tooltip.add(Component.literal(i+1 + ": - - - - - ").withStyle(ChatFormatting.DARK_GRAY));
                }

            }
        }
        else {
            tooltip.add(Component.literal("Press SHIFT to view Spells.").withStyle(ChatFormatting.GRAY));
        }
    }

}