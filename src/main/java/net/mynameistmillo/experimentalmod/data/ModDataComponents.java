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
            DATA_COMPONENTS.register("wc",
                    () -> new DataComponentType.Builder<Integer>()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> WAND_CAPACITY_COMPACT =
            DATA_COMPONENTS.register("wcc",
                    () -> new DataComponentType.Builder<Integer>()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> WAND_SPELLS =
            DATA_COMPONENTS.register("ws",
                    () -> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> WAND_SPELLS_COMPACT =
            DATA_COMPONENTS.register("wsc",
                    () -> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> WAND_INDEX =
            DATA_COMPONENTS.register("wi",
                    ()-> new DataComponentType.Builder<Integer>()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
                            .build());

    //Projectile

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> SPELL_STATS_F =
            DATA_COMPONENTS.register("ssf",
                    ()-> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> SPELL_STATS_I =
            DATA_COMPONENTS.register("ssi",
                    ()-> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());


    //Draw
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> DRAW_STATS =
            DATA_COMPONENTS.register("ds",
                    ()-> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> DRAW_MOD_SAVED =
            DATA_COMPONENTS.register("dms",
                    ()-> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> DRAW_PROJ_SAVED =
            DATA_COMPONENTS.register("dps",
                    ()-> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> TRIGGER_PROJ_SAVED =
            DATA_COMPONENTS.register("tps",
                    ()-> new DataComponentType.Builder<CompoundTag>()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .build());



}