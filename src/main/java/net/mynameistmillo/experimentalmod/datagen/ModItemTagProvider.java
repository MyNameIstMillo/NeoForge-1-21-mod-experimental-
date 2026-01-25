package net.mynameistmillo.experimentalmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.block.ModBlocks;
import net.mynameistmillo.experimentalmod.items.ModItems;
import net.mynameistmillo.experimentalmod.data.ModTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ExperimentalMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.FANCY_ITEMS)
                .add(ModItems.FANCY_ITEM.get())
                .add(ModBlocks.FANCY_BLOCK.asItem());

        tag(ModTags.Items.WAND_ITEM)
                .add(ModItems.WAND.get());

        tag(ModTags.Items.KEY_ITEM)
                .add(ModItems.KEY.get());

        tag(ModTags.Items.SPELL_ITEM)
                .add(ModItems.SPARK_BOLT.get())
                .add(ModItems.SPARK_BOLT_TRIGGER.get())
                .add(ModItems.BUBBLE_SPARK.get())
                .add(ModItems.TELEPORT_BOLT.get())


                .add(ModItems.SPEED_UP.get())
                .add(ModItems.SPEED_DOWN.get())
                .add(ModItems.MIDDLE_DISTANCE_CAST_FB.get())
                .add(ModItems.LIFE_TIME_UP.get())
                .add(ModItems.LIFE_TIME_DOWN.get())
                .add(ModItems.NO_GRAVITY.get())


                .add(ModItems.DOUBLE.get())
                .add(ModItems.TRIPLE.get())


                .add(ModItems.DOUBLE_WITH_LIFE_TIME_DOWN.get())
                .add(ModItems.DOUBLE_SPARK_BOLT.get())
                .add(ModItems.LIFE_TIME_DOWN_AND_SPEED_UP.get())


        ;
    }
}
