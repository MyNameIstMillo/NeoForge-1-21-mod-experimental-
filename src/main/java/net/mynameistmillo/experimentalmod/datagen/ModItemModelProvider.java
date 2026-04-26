package net.mynameistmillo.experimentalmod.datagen;

import net.minecraft.data.PackOutput;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.items.ModItems;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ExperimentalMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.FANCY_ITEM.get());
        basicItem(ModItems.WAND.get());
        basicItem(ModItems.KEY.get());

        basicItem(ModItems.SPARK_BOLT.get());
        basicItem(ModItems.SPARK_BOLT_TRIGGER.get());
        basicItem(ModItems.BUBBLE_SPARK.get());
        basicItem(ModItems.TELEPORT_BOLT.get());

        basicItem(ModItems.PIN_POINT.get());
        basicItem(ModItems.PIN_POINT_TRIGGER.get());
        basicItem(ModItems.PIN_POINT_EXPIRE.get());
        basicItem(ModItems.SPIN_SPARK.get());
        basicItem(ModItems.SLIME_BALL.get());
        basicItem(ModItems.INFESTATION_SINGLE.get());

        basicItem(ModItems.INFESTATION.get());


        basicItem(ModItems.SPEED_UP.get());
        basicItem(ModItems.SPEED_DOWN.get());
        basicItem(ModItems.LESS_DRAG.get());
        basicItem(ModItems.MORE_DRAG.get());
        basicItem(ModItems.LIFE_TIME_UP.get());
        basicItem(ModItems.LIFE_TIME_DOWN.get());
        basicItem(ModItems.NO_GRAVITY.get());
        basicItem(ModItems.ADD_GRAVITY.get());
        basicItem(ModItems.REMOVE_GRAVITY.get());
        basicItem(ModItems.REVERSE_GRAVITY.get());

        basicItem(ModItems.PLANE_XY.get());
        basicItem(ModItems.PLANE_XZ.get());
        basicItem(ModItems.PLANE_ZY.get());
        basicItem(ModItems.PLANE_RESET.get());


        basicItem(ModItems.SMALL_DISTANCE_CAST_FBN.get());
        basicItem(ModItems.MIDDLE_DISTANCE_CAST_FBN.get());
        basicItem(ModItems.BIG_DISTANCE_CAST_FBN.get());
        basicItem(ModItems.SMALL_DISTANCE_CAST_FBP.get());
        basicItem(ModItems.MIDDLE_DISTANCE_CAST_FBP.get());
        basicItem(ModItems.BIG_DISTANCE_CAST_FBP.get());

        basicItem(ModItems.SMALL_DISTANCE_CAST_LRN.get());
        basicItem(ModItems.MIDDLE_DISTANCE_CAST_LRN.get());
        basicItem(ModItems.BIG_DISTANCE_CAST_LRN.get());
        basicItem(ModItems.SMALL_DISTANCE_CAST_LRP.get());
        basicItem(ModItems.MIDDLE_DISTANCE_CAST_LRP.get());
        basicItem(ModItems.BIG_DISTANCE_CAST_LRP.get());

        basicItem(ModItems.SMALL_DISTANCE_CAST_UDN.get());
        basicItem(ModItems.MIDDLE_DISTANCE_CAST_UDN.get());
        basicItem(ModItems.BIG_DISTANCE_CAST_UDN.get());
        basicItem(ModItems.SMALL_DISTANCE_CAST_UDP.get());
        basicItem(ModItems.MIDDLE_DISTANCE_CAST_UDP.get());
        basicItem(ModItems.BIG_DISTANCE_CAST_UDP.get());


        basicItem(ModItems.DOUBLE.get());
        basicItem(ModItems.TRIPLE.get());
        basicItem(ModItems.TEN_CAST.get());


        basicItem(ModItems.DOUBLE_WITH_LIFE_TIME_DOWN.get());
        basicItem(ModItems.DOUBLE_SPARK_BOLT.get());
        basicItem(ModItems.LIFE_TIME_DOWN_AND_SPEED_UP.get());
        basicItem(ModItems.TEN_BUBBLE_SPARK.get());


    }
}
