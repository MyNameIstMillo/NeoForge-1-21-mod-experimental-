package net.mynameistmillo.experimentalmod.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.mynameistmillo.experimentalmod.Draw.normal.Triple;
import net.mynameistmillo.experimentalmod.Draw.withSomething.DoubleWithLifeTimeDown;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Modifiers.normal.changeOriginOfCast.MiddleDistanceCastFB;
import net.mynameistmillo.experimentalmod.Modifiers.normal.changeTime.LifeTimeDown;
import net.mynameistmillo.experimentalmod.Modifiers.normal.changeTime.LifeTimeUp;
import net.mynameistmillo.experimentalmod.Modifiers.normal.changeGravity.NoGravity;
import net.mynameistmillo.experimentalmod.Modifiers.normal.changeSpeed.SpeedDown;
import net.mynameistmillo.experimentalmod.Modifiers.normal.changeSpeed.SpeedUp;
import net.mynameistmillo.experimentalmod.Modifiers.withSomething.LifeTimeDownAndSpeedUp;
import net.mynameistmillo.experimentalmod.ProjEntity.projectile.normal.sparkBoltTrigger;
import net.mynameistmillo.experimentalmod.ProjEntity.projectile.withSomrthing.DoubleSparkBolt;
import net.mynameistmillo.experimentalmod.ProjEntity.projectile.withSomrthing.TenBubbleSpark;
import net.mynameistmillo.experimentalmod.block.ModBlocks;
import net.mynameistmillo.experimentalmod.items.custom.WandItem;
import net.mynameistmillo.experimentalmod.ProjEntity.projectile.normal.bubbleSpark;
import net.mynameistmillo.experimentalmod.ProjEntity.projectile.normal.sparkBolt;
import net.mynameistmillo.experimentalmod.ProjEntity.projectile.normal.teleportBolt;
import net.mynameistmillo.experimentalmod.Draw.normal.Double;
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
            ()-> new Item(new Item.Properties().stacksTo(1).fireResistant()));

    // PROJECTILE

    public static final DeferredItem<Item> SPARK_BOLT = ITEMS.register("spark_bolt",
            ()-> new sparkBolt(new Item.Properties()));

    public static final DeferredItem<Item> SPARK_BOLT_TRIGGER = ITEMS.register("spark_bolt_trigger",
            ()-> new sparkBoltTrigger(new Item.Properties()));

    public static final DeferredItem<Item> BUBBLE_SPARK = ITEMS.register("bubble_spark",
            ()-> new bubbleSpark(new Item.Properties()));

    public static final DeferredItem<Item> TELEPORT_BOLT = ITEMS.register("teleport_bolt",
            ()-> new teleportBolt(new Item.Properties()));


    // MODIFIER

    public static final DeferredItem<Item> SPEED_UP = ITEMS.register("speed_up",
            () -> new SpeedUp(new Item.Properties()));

    public static final DeferredItem<Item> SPEED_DOWN = ITEMS.register("speed_down",
            () -> new SpeedDown(new Item.Properties()));

    public static final DeferredItem<Item> MIDDLE_DISTANCE_CAST_FB = ITEMS.register("middle_distance_cast_fb",
            () -> new MiddleDistanceCastFB(new Item.Properties()));

    public static final DeferredItem<Item> LIFE_TIME_UP = ITEMS.register("life_time_up",
            () -> new LifeTimeUp(new Item.Properties()));

    public static final DeferredItem<Item> LIFE_TIME_DOWN = ITEMS.register("life_time_down",
            () -> new LifeTimeDown(new Item.Properties()));

    public static final DeferredItem<Item> NO_GRAVITY = ITEMS.register("no_gravity",
            () -> new NoGravity(new Item.Properties()));


    // DRAW

    public static final DeferredItem<Item> DOUBLE = ITEMS.register("double",
            () -> new Double(new Item.Properties()));

    public static final DeferredItem<Item> TRIPLE = ITEMS.register("triple",
            () -> new Triple(new Item.Properties()));


    // MULTIPLE

    public static final DeferredItem<Item> DOUBLE_WITH_LIFE_TIME_DOWN = ITEMS.register("double_with_life_time_down",
            () -> new DoubleWithLifeTimeDown(new Item.Properties()));

    public static final DeferredItem<Item> DOUBLE_SPARK_BOLT = ITEMS.register("double_spark_bolt",
            () -> new DoubleSparkBolt(new Item.Properties()));

    public static final DeferredItem<Item> LIFE_TIME_DOWN_AND_SPEED_UP = ITEMS.register("life_time_down_and_speed_up",
            () -> new LifeTimeDownAndSpeedUp(new Item.Properties()));

    public static final DeferredItem<Item> TEN_BUBBLE_SPARK = ITEMS.register("ten_bubble_spark",
            () -> new TenBubbleSpark(new Item.Properties()));





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
