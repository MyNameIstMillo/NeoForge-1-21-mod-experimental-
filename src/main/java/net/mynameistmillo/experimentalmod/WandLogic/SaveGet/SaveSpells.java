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
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyI;
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
            ItemStack stack = list.get(i);
            CompoundTag spellTag = new CompoundTag();
            if(!stack.is(Items.DIRT)){
                stack.save(level.registryAccess(), spellTag);
                spellTag.putString("id", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            }
            else spellTag.putString("id", "minecraft:dirt");

            if (type == SaveOrGetTypeW.COMPACT){
                switch (stack.getItem()){
                    case IProjectile p -> {
                        spellTag.put("ProjStatsF", stack.getOrDefault(ModDataComponents.SPELL_STATS_F.get(),
                                new CompoundTag()));
                        spellTag.put("ProjStatsI", stack.getOrDefault(ModDataComponents.SPELL_STATS_I.get(),
                                new CompoundTag()));
                        int t = ProjStatsI.loadStatsFromProj(stack).get(StatsKeyI.TRIGGER_TYPE);
                        if(t==1 || t==2 || t==3){
                            spellTag.put("SavedProjForTrigger", stack.getOrDefault(ModDataComponents.TRIGGER_PROJ_SAVED.get(),
                                    new CompoundTag()));
                        }
                    }
                    case IDraw d -> {
                        spellTag.put("DrawStats", stack.getOrDefault(ModDataComponents.DRAW_STATS.get(),
                                new CompoundTag()));
                        spellTag.put("DrawSavedProj", stack.getOrDefault(ModDataComponents.DRAW_PROJ_SAVED.get(),
                                new CompoundTag()));

                        List<ItemStack> l = DrawStats.loadModOrProjFormDrawOrTriggerTypeType(level, stack, ModOrProjType.PROJ, DrawOrTriggerType.DRAW);
                        ListTag lt = new ListTag();
                        for (ItemStack s : l){
                            lt.add(s.getOrDefault(ModDataComponents.SPELL_STATS_F.get(), new CompoundTag()));
                            lt.add(s.getOrDefault(ModDataComponents.SPELL_STATS_I.get(), new CompoundTag()));
                        }
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
