package net.mynameistmillo.experimentalmod.WandLogic.SaveGet;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Interface.IDraw;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.DrawStats;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.ModOrProjType;
import net.mynameistmillo.experimentalmod.WandLogic.Types.DrawOrTriggerType;
import net.mynameistmillo.experimentalmod.WandLogic.Types.SaveOrGetTypeW;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SaveSpells {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public static void saveSpells(ItemStack wand, List<ItemStack> list,
                                  Level level, int cap,
                                  SaveOrGetTypeW type){
        ListTag spellsListTag = new ListTag();

        for (int i=0;i<cap;i++){
            ItemStack spell = list.get(i);
            CompoundTag spellTag = new CompoundTag();
            if(!spell.is(Items.DIRT)){
                spell.save(level.registryAccess(), spellTag);
                spellTag.putString("id", BuiltInRegistries.ITEM.getKey(spell.getItem()).toString());
            }
            else spellTag.putString("id", "minecraft:dirt");

            if (type== SaveOrGetTypeW.COMPACT){
                if(spell.getItem() instanceof IProjectile){
                    spellTag.put("ProjStats", spell.getOrDefault(ModDataComponents.SPELL_STATS_F.get(),
                                                                new CompoundTag()));
                }
                if(spell.getItem() instanceof IDraw){
                    spellTag.put("DrawStats", spell.getOrDefault(ModDataComponents.DRAW_STATS.get(),
                                                                new CompoundTag()));
                    spellTag.put("DrawSavedProj", spell.getOrDefault(ModDataComponents.DRAW_PROJ_SAVED.get(),
                            new CompoundTag()));

                    List<ItemStack> l = DrawStats.loadModOrProjFormDrawOrTriggerTypeType(level, spell, ModOrProjType.PROJ, DrawOrTriggerType.DRAW);
                    ListTag lt = new ListTag();
                    for (ItemStack s : l){
                        lt.add(s.getOrDefault(ModDataComponents.SPELL_STATS_F.get(), new CompoundTag()));
                    }
                }
            }

            spellTag.putByte("Count", (byte) 1);
            spellTag.putString("proj_name","");
            spellTag.putInt("Slot",i);
            spellsListTag.add(spellTag);
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put("Spells", spellsListTag);
        switch (type){
            case NORMAL -> wand.set(ModDataComponents.WAND_SPELLS.get(), rootTag);
            case COMPACT -> {
                wand.set(ModDataComponents.WAND_SPELLS_COMPACT.get(), rootTag);
                wand.set(ModDataComponents.WAND_CAPACITY_COMPACT.get(), cap);
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
}
