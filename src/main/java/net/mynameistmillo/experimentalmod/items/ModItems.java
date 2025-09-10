package net.mynameistmillo.experimentalmod.items;

import net.minecraft.world.item.Item;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExperimentalMod.MOD_ID);

    public static final DeferredItem<Item> FANCY_ITEM = ITEMS.register("fancy_item",
            ()-> new Item(new Item.Properties().stacksTo(1)));



    public static void register(IEventBus eventBus){
         ITEMS.register(eventBus);
    }

}
