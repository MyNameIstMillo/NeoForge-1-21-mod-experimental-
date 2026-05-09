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
                .add(ModItems.NOT_SAFE_TELEPORT_BOLT.get())

                .add(ModItems.PIN_POINT.get())
                .add(ModItems.PIN_POINT_TRIGGER.get())
                .add(ModItems.PIN_POINT_EXPIRE.get())
                .add(ModItems.SPIN_SPARK.get())
                .add(ModItems.SLIME_BALL.get())
                .add(ModItems.INFESTATION_SINGLE.get())
                .add(ModItems.INFESTATION.get())


                .add(ModItems.SPEED_UP.get())
                .add(ModItems.SPEED_DOWN.get())
                .add(ModItems.LESS_DRAG.get())
                .add(ModItems.MORE_DRAG.get())
                .add(ModItems.LIFE_TIME_UP.get())
                .add(ModItems.LIFE_TIME_DOWN.get())
                .add(ModItems.CAST_BY_PLAYER.get())
                .add(ModItems.ADD_DAMAGE.get())
                .add(ModItems.BLOOD_LUST.get())

                .add(ModItems.REDUCE_H_SPREAD.get())
                .add(ModItems.REDUCE_V_SPREAD.get())
                .add(ModItems.INCREASE_H_SPREAD.get())
                .add(ModItems.INCREASE_V_SPREAD.get())

                .add(ModItems.NO_GRAVITY.get())
                .add(ModItems.ADD_GRAVITY.get())
                .add(ModItems.REMOVE_GRAVITY.get())
                .add(ModItems.REVERSE_GRAVITY.get())

                .add(ModItems.PLANE_XY.get())
                .add(ModItems.PLANE_XZ.get())
                .add(ModItems.PLANE_ZY.get())
                .add(ModItems.PLANE_RESET.get())

                .add(ModItems.SMALL_DISTANCE_CAST_FBN.get())
                .add(ModItems.MIDDLE_DISTANCE_CAST_FBN.get())
                .add(ModItems.BIG_DISTANCE_CAST_FBN.get())
                .add(ModItems.SMALL_DISTANCE_CAST_FBP.get())
                .add(ModItems.MIDDLE_DISTANCE_CAST_FBP.get())
                .add(ModItems.BIG_DISTANCE_CAST_FBP.get())

                .add(ModItems.SMALL_DISTANCE_CAST_LRN.get())
                .add(ModItems.MIDDLE_DISTANCE_CAST_LRN.get())
                .add(ModItems.BIG_DISTANCE_CAST_LRN.get())
                .add(ModItems.SMALL_DISTANCE_CAST_LRP.get())
                .add(ModItems.MIDDLE_DISTANCE_CAST_LRP.get())
                .add(ModItems.BIG_DISTANCE_CAST_LRP.get())

                .add(ModItems.SMALL_DISTANCE_CAST_UDN.get())
                .add(ModItems.MIDDLE_DISTANCE_CAST_UDN.get())
                .add(ModItems.BIG_DISTANCE_CAST_UDN.get())
                .add(ModItems.SMALL_DISTANCE_CAST_UDP.get())
                .add(ModItems.MIDDLE_DISTANCE_CAST_UDP.get())
                .add(ModItems.BIG_DISTANCE_CAST_UDP.get())


                .add(ModItems.DOUBLE.get())
                .add(ModItems.TRIPLE.get())
                .add(ModItems.TEN_CAST.get())


                .add(ModItems.DOUBLE_WITH_LIFE_TIME_DOWN.get())
                .add(ModItems.DOUBLE_SPARK_BOLT.get())
                .add(ModItems.LIFE_TIME_DOWN_AND_SPEED_UP.get())
                .add(ModItems.TEN_BUBBLE_SPARK.get())


        ;
    }
}
