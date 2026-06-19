package net.mynameistmillo.experimentalmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.mynameistmillo.experimentalmod.block.ModBlocks;
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

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WAND_EDITOR.get())
                .pattern("BYB")
                .pattern("BLB")
                .pattern("BBB")
                .define('Y',Items.YELLOW_DYE)
                .define('B',ModBlocks.FANCY_BLOCK)
                .define('L',Items.LAPIS_BLOCK)
                .unlockedBy("has_bricks", has(Items.BRICKS)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.WAND.get())
                .pattern(" SG")
                .pattern("LSS")
                .pattern("SL ")
                .define('S',Items.STICK)
                .define('G',Items.GLOWSTONE_DUST)
                .define('L',Items.LAPIS_LAZULI)
                .unlockedBy("has_bricks", has(Items.BRICKS)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.KEY.get())
                .pattern(" D ")
                .pattern("DGD")
                .pattern(" A ")
                .define('A',Items.ANVIL)
                .define('D',Items.GLOWSTONE_DUST)
                .define('G',Items.GOLD_BLOCK)
                .unlockedBy("has_bricks", has(Items.BRICKS)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SPARK_BOLT.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.GUNPOWDER)
                .requires(Items.PINK_DYE)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SPARK_BOLT_TRIGGER.get())
                .requires(ModItems.SPARK_BOLT.get())
                .requires(Items.STONE_PRESSURE_PLATE)
                .unlockedBy("has_spark_bolt", has(ModItems.SPARK_BOLT.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BUBBLE_SPARK.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.BLUE_DYE)
                .requires(Items.FEATHER)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PIN_POINT.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.ARROW)
                .requires(Items.BLAZE_ROD)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PIN_POINT_TRIGGER.get())
                .requires(ModItems.PIN_POINT.get())
                .requires(Items.STONE_PRESSURE_PLATE)
                .unlockedBy("has_pin_point", has(ModItems.PIN_POINT.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PIN_POINT_EXPIRE.get())
                .requires(ModItems.PIN_POINT.get())
                .requires(Items.CLOCK)
                .unlockedBy("has_pin_point", has(ModItems.PIN_POINT.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TELEPORT_BOLT.get())
                .requires(ModItems.SPARK_BOLT.get())
                .requires(Items.ENDER_PEARL)
                .unlockedBy("has_spark_bolt", has(ModItems.SPARK_BOLT.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.NOT_SAFE_TELEPORT_BOLT.get())
                .requires(ModItems.TELEPORT_BOLT.get())
                .requires(Items.FIRE_CHARGE)
                .unlockedBy("has_teleport_bolt", has(ModItems.TELEPORT_BOLT.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.INFESTATION_SINGLE.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.SPIDER_EYE)
                .requires(Items.PURPLE_DYE)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.INFESTATION.get())
                .requires(ModItems.INFESTATION_SINGLE.get())
                .requires(Items.SPIDER_EYE, 4)
                .unlockedBy("has_infestation_single", has(ModItems.INFESTATION_SINGLE.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SLIME_BALL.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.SLIME_BALL)
                .requires(Items.GREEN_DYE)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SPIN_SPARK.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.COMPASS)
                .requires(Items.BLUE_DYE)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SPEED_UP.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.REDSTONE)
                .requires(Items.SUGAR)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SPEED_DOWN.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.REDSTONE)
                .requires(Items.SOUL_SAND)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LIFE_TIME_UP.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.CLOCK)
                .requires(Items.GOLD_NUGGET)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LIFE_TIME_DOWN.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.CLOCK)
                .requires(Items.ROTTEN_FLESH)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LIFE_TIME_DOWN_AND_SPEED_UP.get())
                .requires(ModItems.LIFE_TIME_DOWN.get())
                .requires(ModItems.SPEED_UP.get())
                .unlockedBy("has_speed_up", has(ModItems.SPEED_UP.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.NO_GRAVITY.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.FEATHER)
                .requires(Items.PHANTOM_MEMBRANE)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ADD_GRAVITY.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.IRON_INGOT)
                .requires(Items.GRAVEL)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.REMOVE_GRAVITY.get())
                .requires(ModItems.NO_GRAVITY.get())
                .requires(Items.PHANTOM_MEMBRANE)
                .unlockedBy("has_no_gravity", has(ModItems.NO_GRAVITY.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.REVERSE_GRAVITY.get())
                .requires(ModItems.REMOVE_GRAVITY.get())
                .requires(Items.MAGMA_CREAM)
                .unlockedBy("has_remove_gravity", has(ModItems.REMOVE_GRAVITY.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ADD_DAMAGE.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.IRON_SWORD)
                .requires(Items.REDSTONE)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLOOD_LUST.get())
                .requires(ModItems.ADD_DAMAGE.get())
                .requires(Items.ROTTEN_FLESH)
                .requires(Items.FERMENTED_SPIDER_EYE)
                .unlockedBy("has_add_damage", has(ModItems.ADD_DAMAGE.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.REDUCE_V_SPREAD.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.BOW)
                .requires(Items.IRON_NUGGET)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.REDUCE_H_SPREAD.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.BOW)
                .requires(Items.GOLD_NUGGET)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.INCREASE_V_SPREAD.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.BOW)
                .requires(Items.STICK)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.INCREASE_H_SPREAD.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.BOW)
                .requires(Items.FLINT)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LESS_DRAG.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.PACKED_ICE)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MORE_DRAG.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.STRING)
                .requires(Items.SOUL_SAND)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PLANE_XY.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.SANDSTONE)
                .requires(Items.COMPASS)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PLANE_XZ.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.STONE)
                .requires(Items.COMPASS)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PLANE_ZY.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.DIORITE)
                .requires(Items.COMPASS)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PLANE_RESET.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.FIRE_CHARGE)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DOUBLE.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.ENDER_PEARL, 2)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TRIPLE.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.ENDER_PEARL, 3)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TEN_CAST.get())
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.LEATHER)
                .requires(Items.END_STONE, 2)
                .requires(Items.ENDER_PEARL, 2)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DOUBLE_WITH_LIFE_TIME_DOWN.get())
                .requires(ModItems.DOUBLE.get())
                .requires(ModItems.LIFE_TIME_DOWN.get())
                .unlockedBy("has_double", has(ModItems.DOUBLE.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DOUBLE_SPARK_BOLT.get())
                .requires(ModItems.SPARK_BOLT.get(),2)
                .unlockedBy("has_double", has(ModItems.DOUBLE.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TEN_BUBBLE_SPARK.get())
                .requires(ModItems.TEN_CAST.get())
                .requires(ModItems.BUBBLE_SPARK.get())
                .unlockedBy("has_ten_cast", has(ModItems.TEN_CAST.get())).save(recipeOutput);

        // ── FRONT/BACK (red dye) ──────────────────────────────────────────────
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SMALL_DISTANCE_CAST_FBN.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.STRING)
                .requires(Items.BLUE_DYE).requires(Items.COAL)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SMALL_DISTANCE_CAST_FBP.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.STRING)
                .requires(Items.BLUE_DYE).requires(Items.GLOWSTONE_DUST)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MIDDLE_DISTANCE_CAST_FBN.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.IRON_INGOT)
                .requires(Items.BLUE_DYE).requires(Items.COAL)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MIDDLE_DISTANCE_CAST_FBP.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.IRON_INGOT)
                .requires(Items.BLUE_DYE).requires(Items.GLOWSTONE_DUST)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BIG_DISTANCE_CAST_FBN.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.GOLD_INGOT)
                .requires(Items.BLUE_DYE).requires(Items.COAL)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BIG_DISTANCE_CAST_FBP.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.GOLD_INGOT)
                .requires(Items.BLUE_DYE).requires(Items.GLOWSTONE_DUST)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

// ── UP/DOWN (green dye) ───────────────────────────────────────────────
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SMALL_DISTANCE_CAST_UDN.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.STRING)
                .requires(Items.GREEN_DYE).requires(Items.COAL)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SMALL_DISTANCE_CAST_UDP.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.STRING)
                .requires(Items.GREEN_DYE).requires(Items.GLOWSTONE_DUST)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MIDDLE_DISTANCE_CAST_UDN.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.IRON_INGOT)
                .requires(Items.GREEN_DYE).requires(Items.COAL)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MIDDLE_DISTANCE_CAST_UDP.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.IRON_INGOT)
                .requires(Items.GREEN_DYE).requires(Items.GLOWSTONE_DUST)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BIG_DISTANCE_CAST_UDN.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.GOLD_INGOT)
                .requires(Items.GREEN_DYE).requires(Items.COAL)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BIG_DISTANCE_CAST_UDP.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.GOLD_INGOT)
                .requires(Items.GREEN_DYE).requires(Items.GLOWSTONE_DUST)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

// ── LEFT/RIGHT (blue dye) ─────────────────────────────────────────────
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SMALL_DISTANCE_CAST_LRN.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.STRING)
                .requires(Items.RED_DYE).requires(Items.COAL)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SMALL_DISTANCE_CAST_LRP.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.STRING)
                .requires(Items.RED_DYE).requires(Items.GLOWSTONE_DUST)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MIDDLE_DISTANCE_CAST_LRN.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.IRON_INGOT)
                .requires(Items.RED_DYE).requires(Items.COAL)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MIDDLE_DISTANCE_CAST_LRP.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.IRON_INGOT)
                .requires(Items.RED_DYE).requires(Items.GLOWSTONE_DUST)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BIG_DISTANCE_CAST_LRN.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.GOLD_INGOT)
                .requires(Items.RED_DYE).requires(Items.COAL)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BIG_DISTANCE_CAST_LRP.get())
                .requires(Items.LAPIS_LAZULI).requires(Items.LEATHER)
                .requires(Items.COMPASS).requires(Items.GOLD_INGOT)
                .requires(Items.RED_DYE).requires(Items.GLOWSTONE_DUST)
                .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);
    }
}
