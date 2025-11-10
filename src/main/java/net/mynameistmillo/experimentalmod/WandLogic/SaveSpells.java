package net.mynameistmillo.experimentalmod.WandLogic;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.LogicStats.Interface.IDraw;
import net.mynameistmillo.experimentalmod.LogicStats.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SaveSpells {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public static void saveSpells(ItemStack wand, List<ItemStack> list,
                                  Level level, int cap,
                                  SaveOrGetType type){
        ListTag spellsListTag = new ListTag();

        for (int i=0;i<cap;i++){
            ItemStack spell = list.get(i);
            CompoundTag spellTag = new CompoundTag();
            if(!spell.is(Items.DIRT)){
                spell.save(level.registryAccess(), spellTag);
                spellTag.putString("id", BuiltInRegistries.ITEM.getKey(spell.getItem()).toString());
            }
            else spellTag.putString("id", "minecraft:dirt");

            if (type== SaveOrGetType.COMPACT){
                if(spell.getItem() instanceof IProjectile){
                    CompoundTag cT = spell.getOrDefault(ModDataComponents.SPELL_STATS.get(),
                            new CompoundTag());
                    spellTag.put("ProjStats", cT);
                }
                if(spell.getItem() instanceof IDraw){
                    CompoundTag cT = spell.getOrDefault(ModDataComponents.DRAW_STATS.get(),
                            new CompoundTag());
                    spellTag.put("DrawStats", cT);
                }
            }

            spellTag.putByte("Count", (byte) 1);
            spellTag.putString("proj_name","");
            spellTag.putInt("Slot",i);
            spellsListTag.add(spellTag);
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put("Spells", spellsListTag);
        LOGGER.info("type -> {}, spells -> {}",type, spellsListTag);
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
