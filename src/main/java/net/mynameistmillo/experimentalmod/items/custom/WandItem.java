package net.mynameistmillo.experimentalmod.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;

import java.util.List;

public class WandItem extends Item {
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
            if (!spell.isEmpty()) {
                CompoundTag spellTag = new CompoundTag();
                spell.save(level.registryAccess(), spellTag);
                spellTag.putString("id", BuiltInRegistries.ITEM.getKey(spell.getItem()).toString());
                spellTag.putByte("Count", (byte) spell.getCount());
                spellTag.putInt("Slot", i);
                spellsListTag.add(spellTag);
            }
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put("Spells", spellsListTag);

        wand.set(ModDataComponents.WAND_SPELLS.get(), rootTag);
    }

    public NonNullList<ItemStack> getSavedSpells(ItemStack wand, Level level) {
        int capacity = getCapacity(wand);
        NonNullList<ItemStack> list = NonNullList.withSize(capacity, ItemStack.EMPTY);
        CompoundTag wandSpellsTag = wand.get(ModDataComponents.WAND_SPELLS.get());

        if (wandSpellsTag != null && wandSpellsTag.contains("Spells", ListTag.TAG_LIST)) {
            ListTag listTag = wandSpellsTag.getList("Spells", Tag.TAG_COMPOUND);

            for (int i = 0; i < capacity; i++) {
                CompoundTag spellTag = listTag.getCompound(i);
                int slot = spellTag.getInt("Slot");
                ItemStack spell = ItemStack.parse(level.registryAccess(), spellTag).orElse(ItemStack.EMPTY);

                if (!spell.isEmpty() && slot >= 0 && slot < list.size()) {
                    list.set(slot, spell);
                }
            }
        }
        return list;
    }

    public boolean areThereSpellsInWand(ItemStack wand, Level level) {
        if (!(wand.getItem() instanceof WandItem wandItem)) {
            return false;
        }
        NonNullList<ItemStack> contents = wandItem.getSavedSpells(wand, level);

        for (ItemStack stack : contents) {

            if (!stack.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void clearStoredSpells(ItemStack wand) {
        CompoundTag tag = new CompoundTag();
        tag.put("Spells", new ListTag());
        wand.set(ModDataComponents.WAND_SPELLS.get(), tag);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        Level level = context.level();
        if (!areThereSpellsInWand(stack, level)) {
            tooltip.add(Component.literal("There are no Spells.").withStyle(ChatFormatting.GRAY));
            return;
        } else if (Screen.hasShiftDown()) {

            NonNullList<ItemStack> spells = getSavedSpells(stack, level);
            tooltip.add(Component.literal("Spells:").withStyle(ChatFormatting.GRAY));
            int i = 1;
            for (ItemStack spell : spells) {
                if (!spell.isEmpty()) {
                    tooltip.add(Component.literal(i + ": " + spell.getHoverName().getString()).withStyle(ChatFormatting.GRAY));
                }
            }
        } else {
            tooltip.add(Component.literal("Press CTRL to view Spells.").withStyle(ChatFormatting.GRAY));
        }
    }

}