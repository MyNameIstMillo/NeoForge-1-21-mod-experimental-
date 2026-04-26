package net.mynameistmillo.experimentalmod.tabs;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.block.ModBlocks;
import net.mynameistmillo.experimentalmod.items.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExperimentalMod.MOD_ID);

    public static final Supplier<CreativeModeTab> MOD_ITEMS = CREATIVE_MODE_TAB.register("mod_items_tab",
            ()-> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.FANCY_ITEM.get()))
                    .title(Component.translatable("creativetab.experimentalmodid.mod_items_tab"))
                    .displayItems((itemDisplayParameters,output)-> {
                        output.accept(ModItems.FANCY_ITEM);
                        output.accept(ModItems.WAND);
                        output.accept(ModItems.KEY);



                    }).build());

    public static final Supplier<CreativeModeTab> MOD_SPELLS = CREATIVE_MODE_TAB.register("mod_spells_tab",
            ()-> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.SPARK_BOLT.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(ExperimentalMod.MOD_ID, "mod_items_tab"))
                    .title(Component.translatable("creativetab.experimentalmodid.mod_spells_tab"))
                    .displayItems((itemDisplayParameters,output)-> {
                        output.accept(ModItems.SPARK_BOLT);
                        output.accept(ModItems.SPARK_BOLT_TRIGGER);
                        output.accept(ModItems.BUBBLE_SPARK);
                        output.accept(ModItems.TELEPORT_BOLT);

                        output.accept(ModItems.PIN_POINT);
                        output.accept(ModItems.PIN_POINT_TRIGGER);
                        output.accept(ModItems.PIN_POINT_EXPIRE);
                        output.accept(ModItems.SPIN_SPARK);
                        output.accept(ModItems.SLIME_BALL);
                        output.accept(ModItems.INFESTATION_SINGLE);

                        output.accept(ModItems.INFESTATION);

                        output.accept(ModItems.SPEED_UP);
                        output.accept(ModItems.SPEED_DOWN);
                        output.accept(ModItems.LESS_DRAG);
                        output.accept(ModItems.MORE_DRAG);
                        output.accept(ModItems.LIFE_TIME_UP);
                        output.accept(ModItems.LIFE_TIME_DOWN);
                        output.accept(ModItems.NO_GRAVITY);
                        output.accept(ModItems.ADD_GRAVITY);
                        output.accept(ModItems.REMOVE_GRAVITY);
                        output.accept(ModItems.REVERSE_GRAVITY);

                        output.accept(ModItems.PLANE_XY);
                        output.accept(ModItems.PLANE_XZ);
                        output.accept(ModItems.PLANE_ZY);
                        output.accept(ModItems.PLANE_RESET);

                        output.accept(ModItems.SMALL_DISTANCE_CAST_FBN);
                        output.accept(ModItems.MIDDLE_DISTANCE_CAST_FBN);
                        output.accept(ModItems.BIG_DISTANCE_CAST_FBN);
                        output.accept(ModItems.SMALL_DISTANCE_CAST_FBP);
                        output.accept(ModItems.MIDDLE_DISTANCE_CAST_FBP);
                        output.accept(ModItems.BIG_DISTANCE_CAST_FBP);

                        output.accept(ModItems.SMALL_DISTANCE_CAST_LRN);
                        output.accept(ModItems.MIDDLE_DISTANCE_CAST_LRN);
                        output.accept(ModItems.BIG_DISTANCE_CAST_LRN);
                        output.accept(ModItems.SMALL_DISTANCE_CAST_LRP);
                        output.accept(ModItems.MIDDLE_DISTANCE_CAST_LRP);
                        output.accept(ModItems.BIG_DISTANCE_CAST_LRP);

                        output.accept(ModItems.SMALL_DISTANCE_CAST_UDN);
                        output.accept(ModItems.MIDDLE_DISTANCE_CAST_UDN);
                        output.accept(ModItems.BIG_DISTANCE_CAST_UDN);
                        output.accept(ModItems.SMALL_DISTANCE_CAST_UDP);
                        output.accept(ModItems.MIDDLE_DISTANCE_CAST_UDP);
                        output.accept(ModItems.BIG_DISTANCE_CAST_UDP);

                        output.accept(ModItems.TRIPLE);
                        output.accept(ModItems.TEN_CAST);

                        output.accept(ModItems.DOUBLE_WITH_LIFE_TIME_DOWN);
                        output.accept(ModItems.DOUBLE_SPARK_BOLT);
                        output.accept(ModItems.LIFE_TIME_DOWN_AND_SPEED_UP);
                        output.accept(ModItems.TEN_BUBBLE_SPARK);



                    }).build());

    public static final Supplier<CreativeModeTab> MOD_BLOCKS = CREATIVE_MODE_TAB.register("mod_blocks",
            ()-> CreativeModeTab.builder().icon(()-> new ItemStack(ModBlocks.FANCY_BLOCK.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(ExperimentalMod.MOD_ID, "mod_spells_tab"))
                    .title(Component.translatable("creativetab.experimentalmodid.mod_blocks_tab"))
                    .displayItems((itemDisplayParameters,output)-> {
                        output.accept(ModBlocks.FANCY_BLOCK);
                        output.accept(ModBlocks.WAND_EDITOR);



                    }).build());


    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
