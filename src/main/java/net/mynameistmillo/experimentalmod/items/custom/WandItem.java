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
import net.mynameistmillo.experimentalmod.spells.ISpell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WandItem extends Item {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);
    public WandItem(Properties properties, int capacity) {
        super(properties);
        this.deafultCapacity = capacity;
    }

    private final int deafultCapacity;

    public int getCapacity(ItemStack wand) {
        Integer capacity = wand.get(ModDataComponents.WAND_CAPACITY.get());
        if (capacity == null) {
            capacity = deafultCapacity;
            wand.set(ModDataComponents.WAND_CAPACITY.get(), capacity);
        }
        return deafultCapacity;
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
            spellTag.putInt("Slot", i);
            spellsListTag.add(spellTag);
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put("Spells", spellsListTag);

        wand.set(ModDataComponents.WAND_SPELLS.get(), rootTag);
        setCurrentIndex(wand, 0);
        //LOGGER.info("saveSpells -> rootTag -> {}", rootTag);
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

    public int getCurrentIndex(ItemStack wand){
        Integer capacity = wand.get(ModDataComponents.WAND_CAPACITY.get());
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



    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack wand = player.getItemInHand(usedHand);
        int capacity = getCapacity(wand);
        int index = getCurrentIndex(wand);
        List<ItemStack> storedSpells = getSavedSpells(wand, level);



        if(level.isClientSide()){
            return InteractionResultHolder.pass(wand);
        }

        int checked=0;
        ItemStack currentSpell = ItemStack.EMPTY;
        while(checked<capacity){
            currentSpell = storedSpells.get(index);
            if(!currentSpell.is(Items.DIRT)){
                break;
            }
            index = (index+1)%capacity;
        }

        if(currentSpell.getItem() instanceof ISpell spellCast){

            Entity entity = spellCast.spawnSpell(level, player.getOnPos(), player, player.getLookAngle(), wand);
            Explosion explosion = spellCast.createExplosion(level, player.getOnPos(), player, wand);

            index = (index+1)%capacity;
            setCurrentIndex(wand, index);

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