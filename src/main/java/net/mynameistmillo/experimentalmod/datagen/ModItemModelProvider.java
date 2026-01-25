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


        basicItem(ModItems.SPEED_UP.get());
        basicItem(ModItems.SPEED_DOWN.get());
        basicItem(ModItems.MIDDLE_DISTANCE_CAST_FB.get());
        basicItem(ModItems.LIFE_TIME_UP.get());
        basicItem(ModItems.LIFE_TIME_DOWN.get());
        basicItem(ModItems.NO_GRAVITY.get());


        basicItem(ModItems.DOUBLE.get());
        basicItem(ModItems.TRIPLE.get());


        basicItem(ModItems.DOUBLE_WITH_LIFE_TIME_DOWN.get());
        basicItem(ModItems.DOUBLE_SPARK_BOLT.get());
        basicItem(ModItems.LIFE_TIME_DOWN_AND_SPEED_UP.get());


    }
}
