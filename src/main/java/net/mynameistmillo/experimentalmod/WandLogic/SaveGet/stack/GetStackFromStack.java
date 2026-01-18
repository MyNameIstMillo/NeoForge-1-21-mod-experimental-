package net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Enum.ModOrProjType;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;

import java.util.ArrayList;
import java.util.List;

public class GetStackFromStack {

    public static List<ItemStack> projFromTrigger(Level level, ItemStack trigger,
                                                  ModOrProjType MOP){

        CompoundTag cT = trigger.get(ModDataComponents.TRIGGER_PROJ_SAVED.get());

        List<ItemStack> list = new ArrayList<>();

        if(cT != null && cT.contains(MOP.getId(), ListTag.TAG_LIST)){
            ListTag listTag = cT.getList(MOP.getId(), Tag.TAG_COMPOUND);

            for (int i=0; i<listTag.size(); i++){
                CompoundTag tag = listTag.getCompound(i);
                ItemStack stack = ItemStack.parseOptional(level.registryAccess(), tag);

                if (stack.getItem() instanceof IProjectile) {
                    CompoundTag projStatsF = tag.getCompound("ProjStatsF");
                    stack.set(ModDataComponents.SPELL_STATS_F.get(), projStatsF);

                    CompoundTag projStatsI = tag.getCompound("ProjStatsI");
                    stack.set(ModDataComponents.SPELL_STATS_I.get(), projStatsI);

                    CompoundTag projSaved = tag.getCompound("ProjSaved");
                    stack.set(ModDataComponents.TRIGGER_PROJ_SAVED, projSaved);
                }
                list.add(stack);
            }
        }
        return list;
    }


    // ======================================================================================


    public static List<ItemStack> stackFromDraw(Level level, ItemStack fromStack,
                                                  ModOrProjType MOP){

        CompoundTag cT = new CompoundTag();

        switch (MOP) {
            case MOD -> cT = fromStack.get(ModDataComponents.DRAW_MOD_SAVED.get());
            case PROJ -> cT = fromStack.get(ModDataComponents.DRAW_PROJ_SAVED.get());
        }

        List<ItemStack> list = new ArrayList<>();

        if(cT != null && cT.contains(MOP.getId(), ListTag.TAG_LIST)){
            ListTag listTag = cT.getList(MOP.getId(), Tag.TAG_COMPOUND);

            for (int i=0; i<listTag.size(); i++){
                CompoundTag tag = listTag.getCompound(i);
                ItemStack stack = ItemStack.parseOptional(level.registryAccess(), tag);

                if (stack.getItem() instanceof IProjectile) {
                    CompoundTag projStatsF = tag.getCompound("ProjStatsF");
                    stack.set(ModDataComponents.SPELL_STATS_F.get(), projStatsF);

                    CompoundTag projStatsI = tag.getCompound("ProjStatsI");
                    stack.set(ModDataComponents.SPELL_STATS_I.get(), projStatsI);

                    CompoundTag projSaved = tag.getCompound("ProjSaved");
                    stack.set(ModDataComponents.TRIGGER_PROJ_SAVED, projSaved);
                }
                list.add(stack);
            }
        }
        return list;
    }
}
