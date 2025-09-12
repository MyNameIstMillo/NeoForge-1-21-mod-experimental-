package net.mynameistmillo.experimentalmod.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.block.ModBlocks;
import net.mynameistmillo.experimentalmod.items.custom.WandItem;
import net.mynameistmillo.experimentalmod.spells.custom.fireBolt;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExperimentalMod.MOD_ID);

    public static final DeferredItem<Item> FANCY_ITEM = ITEMS.register("fancy_item",
            ()-> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> WAND = ITEMS.register("wand",
            ()-> new WandItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(128)
                    .rarity(Rarity.UNCOMMON)
                    .setNoRepair()
                    .fireResistant()
                    , 1));

    public static final DeferredItem<Item> KEY = ITEMS.register("key",
            ()-> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> FIRE_BOLT = ITEMS.register("fire_bolt",
            ()-> new fireBolt(new Item.Properties()));








    public static final DeferredItem<BlockItem> WAND_EDITOR_ITEM = ITEMS.register("wand_editor",
            ()-> new BlockItem(ModBlocks.WAND_EDITOR.get(),
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.EPIC)
                            .fireResistant()));


    public static void register(IEventBus eventBus){
         ITEMS.register(eventBus);
    }

}
