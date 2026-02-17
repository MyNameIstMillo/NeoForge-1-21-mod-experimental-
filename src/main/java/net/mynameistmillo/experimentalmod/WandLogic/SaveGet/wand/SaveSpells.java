package net.mynameistmillo.experimentalmod.WandLogic.SaveGet.wand;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Interface.IDraw;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsI;
import net.mynameistmillo.experimentalmod.Enum.NormalOrCompactType;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SaveSpells {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public static void saveSpells(ItemStack wand, List<ItemStack> list,
                                  Level level, int cap,
                                  NormalOrCompactType type){
        ListTag spellsListTag = new ListTag();

        for (int i=0;i<cap;i++){
            ItemStack stack = list.get(i);
            CompoundTag spellTag = new CompoundTag();
            if(!stack.is(Items.DIRT)){
                stack.save(level.registryAccess(), spellTag);
                spellTag.putString("id", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            }
            else spellTag.putString("id", "minecraft:dirt");

            if (type == NormalOrCompactType.COMPACT){
                switch (stack.getItem()){
                    case IProjectile p -> {
                        spellTag.put("ProjStatsF", stack.getOrDefault(ModDataComponents.SPELL_STATS_F.get(),
                                new CompoundTag()));
                        spellTag.put("ProjStatsI", stack.getOrDefault(ModDataComponents.SPELL_STATS_I.get(),
                                new CompoundTag()));

                        if(ProjStatsI.loadStatsFromProj(stack).get(StatsI.TRIGGER_TYPE)>=1){
                            spellTag.put("SavedProjForTrigger", stack.getOrDefault(ModDataComponents.TRIGGER_PROJ_SAVED.get(),
                                    new CompoundTag()));
                        }
                    }
                    case IDraw d -> {
                        spellTag.put("DrawStats", stack.getOrDefault(ModDataComponents.DRAW_STATS.get(),
                                new CompoundTag()));
                        spellTag.put("DrawSavedProj", stack.getOrDefault(ModDataComponents.DRAW_PROJ_SAVED.get(),
                                new CompoundTag()));

                    }

                    default -> throw new IllegalStateException("Unexpected value: " + stack.getItem());
                }
            }

            spellTag.putByte("Count", (byte) 1);
            spellTag.putString("proj_name","");
            spellTag.putInt("Slot",i);
            spellsListTag.add(spellTag);
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put("Spells", spellsListTag);
        LOGGER.info("root tag -> {}", rootTag);
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
