package net.mynameistmillo.experimentalmod.WandLogic.SaveGet.wand;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Enum.NormalOrCompact;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GetSpells {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public static List<ItemStack> getSpellsType(ItemStack wand, Level level,
                                                int cap, NormalOrCompact type) {
        List<ItemStack> list = new ArrayList<>();
        CompoundTag wandSpells = new CompoundTag();
        switch (type) {
            case NORMAL ->{
                for(int i = 0; i < cap; i++) {
                    list.add(new ItemStack(Items.DIRT));
                }
                 wandSpells = wand.get(ModDataComponents.WAND_SPELLS.get());
            }
            case COMPACT -> {
                wandSpells = wand.get(ModDataComponents.WAND_SPELLS_COMPACT.get());
                cap = wand.get(ModDataComponents.WAND_CAPACITY_COMPACT.get());
            }
        }

        if (wandSpells !=null && wandSpells.contains("SP", ListTag.TAG_LIST)){
            ListTag listTag = wandSpells.getList("SP", Tag.TAG_COMPOUND);

            for (int i=0;i<cap;i++){
                CompoundTag spellTag = listTag.getCompound(i);

                switch (type){
                    case NORMAL -> {
                        int index = spellTag.getInt("SL");
                        ItemStack spell = ItemStack.parse(level.registryAccess(),
                                spellTag).orElse(new ItemStack(Items.DIRT));

                        if(index>=0 && index<=list.size()) {
                            list.set(index, spell);
                        }
                    }
                    case COMPACT -> {
                        ItemStack spell = ItemStack.parseOptional(level.registryAccess(), spellTag);
                        if(spellTag.contains("PSF", Tag.TAG_COMPOUND)){
                            spell.set(ModDataComponents.SPELL_STATS_F.get(), spellTag.getCompound("PSF"));
                        }
                        if(spellTag.contains("PSI", Tag.TAG_COMPOUND)){
                            spell.set(ModDataComponents.SPELL_STATS_I.get(), spellTag.getCompound("PSI"));
                        }
                        if(spellTag.contains("SPT", Tag.TAG_COMPOUND)){
                            spell.set(ModDataComponents.TRIGGER_PROJ_SAVED.get(), spellTag.getCompound("SPT"));
                        }
                        if(spellTag.contains("DS", Tag.TAG_COMPOUND)){
                            spell.set(ModDataComponents.DRAW_STATS, spellTag.getCompound("DS"));
                        }
                        if(spellTag.contains("DSP", Tag.TAG_COMPOUND)){
                            spell.set(ModDataComponents.DRAW_PROJ_SAVED, spellTag.getCompound("DSP"));
                        }
                        list.add(spell);
                    }
                }
            }
        }
        return list;
    }
}
