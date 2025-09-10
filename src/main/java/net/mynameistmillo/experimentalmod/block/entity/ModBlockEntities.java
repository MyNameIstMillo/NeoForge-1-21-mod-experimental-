package net.mynameistmillo.experimentalmod.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.block.ModBlocks;
import net.mynameistmillo.experimentalmod.block.entity.custom.WandEditorEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ExperimentalMod.MOD_ID);

    public static final Supplier<BlockEntityType<WandEditorEntity>> WAND_EDITOR_BE =
            BLOCK_ENTITIES.register("wand_editor_be", ()-> BlockEntityType.Builder.of(
                    WandEditorEntity::new, ModBlocks.WAND_EDITOR.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
