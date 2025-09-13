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
            ()-> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.FIRE_BOLT.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(ExperimentalMod.MOD_ID, "mod_items_tab"))
                    .title(Component.translatable("creativetab.experimentalmodid.mod_spells_tab"))
                    .displayItems((itemDisplayParameters,output)-> {
                        output.accept(ModItems.FIRE_BOLT);
                        output.accept(ModItems.SNOW_BOLT);



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
