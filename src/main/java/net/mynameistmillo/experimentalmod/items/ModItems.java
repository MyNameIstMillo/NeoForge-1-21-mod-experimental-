package net.mynameistmillo.experimentalmod.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.block.ModBlocks;
import net.mynameistmillo.experimentalmod.items.custom.WandItem;
import net.mynameistmillo.experimentalmod.spellEntity.projectile.bubbleSpark;
import net.mynameistmillo.experimentalmod.spellEntity.projectile.sparkBolt;
import net.mynameistmillo.experimentalmod.spellEntity.projectile.teleportBolt;
import net.mynameistmillo.experimentalmod.spellModifier.speed.SpeedDown;
import net.mynameistmillo.experimentalmod.spellModifier.speed.SpeedUp;
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
                    , 9));

    public static final DeferredItem<Item> KEY = ITEMS.register("key",
            ()-> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SPARK_BOLT = ITEMS.register("spark_bolt",
            ()-> new sparkBolt(new Item.Properties()));

    public static final DeferredItem<Item> BUBBLE_SPARK = ITEMS.register("bubble_spark",
            ()-> new bubbleSpark(new Item.Properties()));

    public static final DeferredItem<Item> TELEPORT_BOLT = ITEMS.register("teleport_bolt",
            ()-> new teleportBolt(new Item.Properties()));

    public static final DeferredItem<Item> SPEED_UP = ITEMS.register("speed_up",
            () -> new SpeedUp(new Item.Properties()));

    public static final DeferredItem<Item> SPEED_DOWN = ITEMS.register("speed_down",
            () -> new SpeedDown(new Item.Properties()));






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
