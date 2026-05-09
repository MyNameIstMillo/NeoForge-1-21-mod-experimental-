package net.mynameistmillo.experimentalmod.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.mynameistmillo.experimentalmod.Draw.standard.tenCastDraw;
import net.mynameistmillo.experimentalmod.Draw.standard.tripleDraw;
import net.mynameistmillo.experimentalmod.Draw.multipleInOneStack.DoubleWithLifeTimeDown;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Modifiers.standard.RestrictToPlane.PlaneReset;
import net.mynameistmillo.experimentalmod.Modifiers.standard.RestrictToPlane.PlaneXY;
import net.mynameistmillo.experimentalmod.Modifiers.standard.RestrictToPlane.PlaneXZ;
import net.mynameistmillo.experimentalmod.Modifiers.standard.RestrictToPlane.PlaneZY;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeDamage.AddDamage;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeDamage.BloodLust;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeDrag.LessDrag;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeDrag.MoreDrag;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeGravity.AddGravity;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeGravity.RemoveGravity;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeGravity.ReverseGravity;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.DU.negative.MiddleDistanceCastUDN;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.DU.negative.SmallDistanceCastUDN;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.DU.positive.BigDistanceCastUDP;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.DU.positive.MiddleDistanceCastUDP;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.DU.positive.SmallDistanceCastUDP;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.FB.negative.BigDistanceCastFBN;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.FB.negative.MiddleDistanceCastFBN;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.FB.negative.SmallDistanceCastFBN;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.FB.positive.BigDistanceCastFBP;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.FB.positive.MiddleDistanceCastFBP;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.FB.positive.SmallDistanceCastFBP;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.LR.negative.MiddleDistanceCastLRN;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.LR.negative.SmallDistanceCastLRN;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.LR.positive.BigDistanceCastLRP;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.LR.positive.MiddleDistanceCastLRP;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeOriginOfCast.LR.positive.SmallDistanceCastLRP;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changePointOfCast.CastByPlayer;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeSpread.IncreaseHorizontalSpread;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeSpread.IncreaseVerticalSpread;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeSpread.ReduceHorizontalSpread;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeSpread.ReduceVerticalSpread;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeTime.LifeTimeDown;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeTime.LifeTimeUp;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeGravity.NoGravity;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeSpeed.SpeedDown;
import net.mynameistmillo.experimentalmod.Modifiers.standard.changeSpeed.SpeedUp;
import net.mynameistmillo.experimentalmod.Modifiers.multipleInOneStack.LifeTimeDownAndSpeedUp;
import net.mynameistmillo.experimentalmod.ProjEntity.projectile.multipleInOneStack.Infestation;
import net.mynameistmillo.experimentalmod.ProjEntity.projectile.standard.*;
import net.mynameistmillo.experimentalmod.ProjEntity.projectile.multipleInOneStack.DoubleSparkBolt;
import net.mynameistmillo.experimentalmod.ProjEntity.projectile.multipleInOneStack.TenBubbleSpark;
import net.mynameistmillo.experimentalmod.block.ModBlocks;
import net.mynameistmillo.experimentalmod.items.custom.WandItem;
import net.mynameistmillo.experimentalmod.Draw.standard.doubleDraw;
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
                    , 27));

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

    public static final DeferredItem<Item> NOT_SAFE_TELEPORT_BOLT = ITEMS.register("not_safe_teleport_bolt",
            ()-> new notSafeTeleportBolt(new Item.Properties()));

    public static final DeferredItem<Item> PIN_POINT = ITEMS.register("pin_point",
            ()-> new pinPoint(new Item.Properties()));

    public static final DeferredItem<Item> PIN_POINT_TRIGGER = ITEMS.register("pin_point_trigger",
            ()-> new pinPointTrigger(new Item.Properties()));

    public static final DeferredItem<Item> PIN_POINT_EXPIRE = ITEMS.register("pin_point_expire",
            ()-> new pinPointExpire(new Item.Properties()));

    public static final DeferredItem<Item> SLIME_BALL = ITEMS.register("slime_ball",
            ()-> new slimeBall(new Item.Properties()));

    public static final DeferredItem<Item> SPIN_SPARK = ITEMS.register("spin_spark",
            ()-> new spinSpark(new Item.Properties()));

    public static final DeferredItem<Item> INFESTATION_SINGLE = ITEMS.register("infestation_single",
            ()-> new infestationSingle(new Item.Properties()));


    // MODIFIER

    public static final DeferredItem<Item> SPEED_UP = ITEMS.register("speed_up",
            () -> new SpeedUp(new Item.Properties()));

    public static final DeferredItem<Item> SPEED_DOWN = ITEMS.register("speed_down",
            () -> new SpeedDown(new Item.Properties()));

    public static final DeferredItem<Item> LESS_DRAG = ITEMS.register("less_drag",
            () -> new LessDrag(new Item.Properties()));

    public static final DeferredItem<Item> MORE_DRAG = ITEMS.register("more_drag",
            () -> new MoreDrag(new Item.Properties()));

    public static final DeferredItem<Item> LIFE_TIME_UP = ITEMS.register("life_time_up",
            () -> new LifeTimeUp(new Item.Properties()));

    public static final DeferredItem<Item> LIFE_TIME_DOWN = ITEMS.register("life_time_down",
            () -> new LifeTimeDown(new Item.Properties()));

    public static final DeferredItem<Item> CAST_BY_PLAYER = ITEMS.register("cast_by_player",
            () -> new CastByPlayer(new Item.Properties()));

    public static final DeferredItem<Item> ADD_DAMAGE = ITEMS.register("add_damage",
            () -> new AddDamage(new Item.Properties()));

    public static final DeferredItem<Item> BLOOD_LUST = ITEMS.register("blood_lust",
            () -> new BloodLust(new Item.Properties()));


    public static final DeferredItem<Item> NO_GRAVITY = ITEMS.register("no_gravity",
            () -> new NoGravity(new Item.Properties()));

    public static final DeferredItem<Item> ADD_GRAVITY = ITEMS.register("add_gravity",
            () -> new AddGravity(new Item.Properties()));

    public static final DeferredItem<Item> REMOVE_GRAVITY = ITEMS.register("remove_gravity",
            () -> new RemoveGravity(new Item.Properties()));

    public static final DeferredItem<Item> REVERSE_GRAVITY = ITEMS.register("reverse_gravity",
            () -> new ReverseGravity(new Item.Properties()));


    public static final DeferredItem<Item> PLANE_XY = ITEMS.register("plane_xy",
            () -> new PlaneXY(new Item.Properties()));

    public static final DeferredItem<Item> PLANE_XZ = ITEMS.register("plane_xz",
            () -> new PlaneXZ(new Item.Properties()));

    public static final DeferredItem<Item> PLANE_ZY = ITEMS.register("plane_zy",
            () -> new PlaneZY(new Item.Properties()));

    public static final DeferredItem<Item> PLANE_RESET = ITEMS.register("plane_reset",
            () -> new PlaneReset(new Item.Properties()));


    public static final DeferredItem<Item> REDUCE_V_SPREAD = ITEMS.register("reduce_vertical_spread",
            () -> new ReduceVerticalSpread(new Item.Properties()));

    public static final DeferredItem<Item> REDUCE_H_SPREAD = ITEMS.register("reduce_horizontal_spread",
            () -> new ReduceHorizontalSpread(new Item.Properties()));

    public static final DeferredItem<Item> INCREASE_V_SPREAD = ITEMS.register("increase_vertical_spread",
            () -> new IncreaseVerticalSpread(new Item.Properties()));

    public static final DeferredItem<Item> INCREASE_H_SPREAD = ITEMS.register("increase_horizontal_spread",
            () -> new IncreaseHorizontalSpread(new Item.Properties()));


    public static final DeferredItem<Item> SMALL_DISTANCE_CAST_FBN = ITEMS.register("small_distance_cast_fbn",
            () -> new SmallDistanceCastFBN(new Item.Properties()));

    public static final DeferredItem<Item> MIDDLE_DISTANCE_CAST_FBN = ITEMS.register("middle_distance_cast_fbn",
            () -> new MiddleDistanceCastFBN(new Item.Properties()));

    public static final DeferredItem<Item> BIG_DISTANCE_CAST_FBN = ITEMS.register("big_distance_cast_fbn",
            () -> new BigDistanceCastFBN(new Item.Properties()));

    public static final DeferredItem<Item> SMALL_DISTANCE_CAST_FBP = ITEMS.register("small_distance_cast_fbp",
            () -> new SmallDistanceCastFBP(new Item.Properties()));

    public static final DeferredItem<Item> MIDDLE_DISTANCE_CAST_FBP = ITEMS.register("middle_distance_cast_fbp",
            () -> new MiddleDistanceCastFBP(new Item.Properties()));

    public static final DeferredItem<Item> BIG_DISTANCE_CAST_FBP = ITEMS.register("big_distance_cast_fbp",
            () -> new BigDistanceCastFBP(new Item.Properties()));


    public static final DeferredItem<Item> SMALL_DISTANCE_CAST_UDN = ITEMS.register("small_distance_cast_udn",
            () -> new SmallDistanceCastUDN(new Item.Properties()));

    public static final DeferredItem<Item> MIDDLE_DISTANCE_CAST_UDN = ITEMS.register("middle_distance_cast_udn",
            () -> new MiddleDistanceCastUDN(new Item.Properties()));

    public static final DeferredItem<Item> BIG_DISTANCE_CAST_UDN = ITEMS.register("big_distance_cast_udn",
            () -> new BigDistanceCastUDP(new Item.Properties()));

    public static final DeferredItem<Item> SMALL_DISTANCE_CAST_UDP = ITEMS.register("small_distance_cast_udp",
            () -> new SmallDistanceCastUDP(new Item.Properties()));

    public static final DeferredItem<Item> MIDDLE_DISTANCE_CAST_UDP = ITEMS.register("middle_distance_cast_udp",
            () -> new MiddleDistanceCastUDP(new Item.Properties()));

    public static final DeferredItem<Item> BIG_DISTANCE_CAST_UDP = ITEMS.register("big_distance_cast_udp",
            () -> new BigDistanceCastUDP(new Item.Properties()));


    public static final DeferredItem<Item> SMALL_DISTANCE_CAST_LRN = ITEMS.register("small_distance_cast_lrn",
            () -> new SmallDistanceCastLRN(new Item.Properties()));

    public static final DeferredItem<Item> MIDDLE_DISTANCE_CAST_LRN = ITEMS.register("middle_distance_cast_lrn",
            () -> new MiddleDistanceCastLRN(new Item.Properties()));

    public static final DeferredItem<Item> BIG_DISTANCE_CAST_LRN = ITEMS.register("big_distance_cast_lrn",
            () -> new BigDistanceCastLRP(new Item.Properties()));

    public static final DeferredItem<Item> SMALL_DISTANCE_CAST_LRP = ITEMS.register("small_distance_cast_lrp",
            () -> new SmallDistanceCastLRP(new Item.Properties()));

    public static final DeferredItem<Item> MIDDLE_DISTANCE_CAST_LRP = ITEMS.register("middle_distance_cast_lrp",
            () -> new MiddleDistanceCastLRP(new Item.Properties()));

    public static final DeferredItem<Item> BIG_DISTANCE_CAST_LRP = ITEMS.register("big_distance_cast_lrp",
            () -> new BigDistanceCastLRP(new Item.Properties()));

    // DRAW

    public static final DeferredItem<Item> DOUBLE = ITEMS.register("double",
            () -> new doubleDraw(new Item.Properties()));

    public static final DeferredItem<Item> TRIPLE = ITEMS.register("triple",
            () -> new tripleDraw(new Item.Properties()));

    public static final DeferredItem<Item> TEN_CAST = ITEMS.register("ten_cast",
            () -> new tenCastDraw(new Item.Properties()));


    // MULTIPLE

    public static final DeferredItem<Item> DOUBLE_WITH_LIFE_TIME_DOWN = ITEMS.register("double_with_life_time_down",
            () -> new DoubleWithLifeTimeDown(new Item.Properties()));

    public static final DeferredItem<Item> DOUBLE_SPARK_BOLT = ITEMS.register("double_spark_bolt",
            () -> new DoubleSparkBolt(new Item.Properties()));

    public static final DeferredItem<Item> LIFE_TIME_DOWN_AND_SPEED_UP = ITEMS.register("life_time_down_and_speed_up",
            () -> new LifeTimeDownAndSpeedUp(new Item.Properties()));

    public static final DeferredItem<Item> TEN_BUBBLE_SPARK = ITEMS.register("ten_bubble_spark",
            () -> new TenBubbleSpark(new Item.Properties()));

    public static final DeferredItem<Item> INFESTATION = ITEMS.register("infestation",
            () -> new Infestation(new Item.Properties()));





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
