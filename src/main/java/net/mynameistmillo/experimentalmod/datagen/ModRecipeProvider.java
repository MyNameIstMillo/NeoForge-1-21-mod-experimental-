package net.mynameistmillo.experimentalmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.mynameistmillo.experimentalmod.block.custom.ModBlocks;
import net.mynameistmillo.experimentalmod.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.FANCY_BLOCK.get(), 8)
                .pattern("BBB")
                .pattern("BDB")
                .pattern("BBB")
                .define('B', Items.BRICKS)
                .define('D',Items.BLACK_DYE)
                .unlockedBy("has_bricks", has(Items.BRICKS)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FANCY_ITEM.get())
                .requires(Items.STICK, 2)
                .requires(Items.LAPIS_LAZULI)
                .unlockedBy("has_lapis_lazuli", has(Items.LAPIS_LAZULI)).save(recipeOutput);



    }
}
