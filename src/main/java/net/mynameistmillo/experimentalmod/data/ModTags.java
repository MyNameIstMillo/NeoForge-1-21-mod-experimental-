package net.mynameistmillo.experimentalmod.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.mynameistmillo.experimentalmod.ExperimentalMod;

public class ModTags {
    public static class Blocks {

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(ExperimentalMod.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> FANCY_ITEMS = createTag("fancy_items");

        public static final TagKey<Item> WAND_ITEM = createTag("wand_item");

        public static final TagKey<Item> KEY_ITEM = createTag("key_item");

        public static final TagKey<Item> SPELL_ITEM = createTag("spell_item");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExperimentalMod.MOD_ID, name));
        }
    }
}