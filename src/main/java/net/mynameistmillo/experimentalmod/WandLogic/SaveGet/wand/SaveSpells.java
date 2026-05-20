package net.mynameistmillo.experimentalmod.WandLogic.SaveGet.wand;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Interface.IDraw;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsI;
import net.mynameistmillo.experimentalmod.Enum.NormalOrCompact;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class SaveSpells {

    public static void saveSpells(ItemStack wand, List<ItemStack> list,
                                  Level level, int cap,
                                  NormalOrCompact type){
        ListTag spellsListTag = new ListTag();

        for (int i=0;i<cap;i++){
            ItemStack stack = list.get(i);
            CompoundTag spellTag = new CompoundTag();
            if(!stack.is(Items.DIRT)){
                stack.save(level.registryAccess(), spellTag);
                spellTag.putString("id", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            }
            else spellTag.putString("id", "minecraft:dirt");

            if (type == NormalOrCompact.COMPACT){
                switch (stack.getItem()){
                    case IProjectile p -> {
                        spellTag.put("PSF", stack.getOrDefault(ModDataComponents.SPELL_STATS_F.get(),
                                new CompoundTag()));
                        spellTag.put("PSI", stack.getOrDefault(ModDataComponents.SPELL_STATS_I.get(),
                                new CompoundTag()));

                        if(ProjStatsI.loadStatsFromProj(stack).get(StatsI.TRIGGER_TYPE)>=1){
                            spellTag.put("SPT", stack.getOrDefault(ModDataComponents.TRIGGER_PROJ_SAVED.get(),
                                    new CompoundTag()));
                        }
                    }
                    case IDraw d -> {
                        spellTag.put("DS", stack.getOrDefault(ModDataComponents.DRAW_STATS.get(),
                                new CompoundTag()));
                        spellTag.put("DSP", stack.getOrDefault(ModDataComponents.DRAW_PROJ_SAVED.get(),
                                new CompoundTag()));

                    }

                    default -> throw new IllegalStateException("Unexpected value: " + stack.getItem());
                }
            }

            spellTag.putByte("Count", (byte) 1);
            spellTag.putString("PN","");
            spellTag.putInt("SL",i);
            spellsListTag.add(spellTag);
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put("SP", spellsListTag);

        int size = getNbtSizeBytes(rootTag);



        switch (type){
            case NORMAL -> wand.set(ModDataComponents.WAND_SPELLS.get(), rootTag);
            case COMPACT -> {
                if (size<2_097_152){
                    wand.set(ModDataComponents.WAND_SPELLS_COMPACT.get(), rootTag);
                    wand.set(ModDataComponents.WAND_CAPACITY_COMPACT.get(), cap);
                }
            }
        }

    }

    public static void saveWithDirt(ItemStack wand, int cap){
        ListTag listTag = new ListTag();

        for (int i=0;i<cap;i++){
            CompoundTag tag = new CompoundTag();
            tag.putString("id", "minecraft:dirt");
            tag.putByte("Count", (byte) 1);
            tag.putString("proj_name", "");
            tag.putInt("Slot", i);
            listTag.add(tag);
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put("Spells", listTag);
        wand.set(ModDataComponents.WAND_SPELLS.get(), rootTag);

    }

    public static int getNbtSizeBytes(CompoundTag tag) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            DataOutputStream dataStream = new DataOutputStream(byteStream);

            NbtIo.write(tag, dataStream);

            dataStream.close();
            return byteStream.size();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
