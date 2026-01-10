package net.mynameistmillo.experimentalmod.data;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ExperimentalMod.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> WAND_CAPACITY =
            DATA_COMPONENTS.register("wand_capacity",
                    () -> new DataComponentType.Builder<Integer>()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> WAND_CAPACITY_COMPACT =
            DATA_COMPONENTS.register("wand_capacity_compact",
                    () -> new DataComponentType.Builder<Integer>()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> WAND_SPELLS =
            DATA_COMPONENTS.register("wand_spells",
                    () -> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> WAND_SPELLS_COMPACT =
            DATA_COMPONENTS.register("wand_spells_compact",
                    () -> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> WAND_INDEX =
            DATA_COMPONENTS.register("wand_index",
                    ()-> new DataComponentType.Builder<Integer>()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
                            .build());

    //Projectile

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> SPELL_STATS_F =
            DATA_COMPONENTS.register("spell_stats_f",
                    ()-> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> SPELL_STATS_I =
            DATA_COMPONENTS.register("spell_stats_i",
                    ()-> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());


    //Draw
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> DRAW_STATS =
            DATA_COMPONENTS.register("draw_stats",
                    ()-> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> DRAW_MOD_SAVED =
            DATA_COMPONENTS.register("draw_mod_saved",
                    ()-> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> DRAW_PROJ_SAVED =
            DATA_COMPONENTS.register("draw_proj_saved",
                    ()-> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> TRIGGER_PROJ_SAVED =
            DATA_COMPONENTS.register("trigger_proj_saved",
                    ()-> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());



}