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
        basicItem(ModItems.BOLT_TRIGGER.get());
    }
}
