package net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Enum.ModOrProjType;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SaveStackIntoStack {


    //Proj -> Trigger
    public static ItemStack projToTrigger(Level level,
                                          @Nullable ItemStack proj,
                                          @Nullable List<ItemStack> moreProj,
                                          @Nullable List<ItemStack> resetAndSave,
                                          ItemStack trigger) {

        List<ItemStack> list = GetStackFromStack.projFromTrigger(level, trigger);
        if (proj != null) list.add(proj);
        if (moreProj != null) list.addAll(moreProj);
        if (resetAndSave != null) list = resetAndSave;

        ListTag listTag = new ListTag();
        for (ItemStack stack : list) {
            CompoundTag tag = new CompoundTag();
            stack.save(level.registryAccess(), tag);
            tag.putString("id", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            tag.putByte("Count", (byte) stack.getCount());
            if (stack.getItem() instanceof IProjectile) {
                tag.put("ProjStatsF", stack.getOrDefault(ModDataComponents.SPELL_STATS_F.get(), new CompoundTag()));
                tag.put("ProjStatsI", stack.getOrDefault(ModDataComponents.SPELL_STATS_I.get(), new CompoundTag()));
                tag.put("ProjSaved", stack.getOrDefault(ModDataComponents.TRIGGER_PROJ_SAVED, new CompoundTag()));
            }
            listTag.add(tag);
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put(ModOrProjType.PROJ.getId(), listTag);

        //save in TRIGGER
        trigger.set(ModDataComponents.TRIGGER_PROJ_SAVED.get(), rootTag);

        return trigger;
    }



    //  ============================================================




    //Stack -> Draw
    public static ItemStack stackToDraw(Level level,
                                        @Nullable ItemStack proj,
                                        @Nullable List<ItemStack> moreProj,
                                        @Nullable List<ItemStack> resetAndSave,
                                        ItemStack draw,
                                        ModOrProjType MOP) {

        List<ItemStack> list = GetStackFromStack.stackFromDraw(level, draw, MOP);
        if (proj != null) list.add(proj);
        if (moreProj != null) list.addAll(moreProj);
        if (resetAndSave != null) list = resetAndSave;


        ListTag listTag = new ListTag();
        for (ItemStack stack : list) {
            CompoundTag tag = new CompoundTag();
            stack.save(level.registryAccess(), tag);
            tag.putString("id", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            tag.putByte("Count", (byte) stack.getCount());
            if (stack.getItem() instanceof IProjectile) {
                tag.put("ProjStatsF", stack.getOrDefault(ModDataComponents.SPELL_STATS_F.get(), new CompoundTag()));
                tag.put("ProjStatsI", stack.getOrDefault(ModDataComponents.SPELL_STATS_I.get(), new CompoundTag()));
                tag.put("ProjSaved", stack.getOrDefault(ModDataComponents.TRIGGER_PROJ_SAVED, new CompoundTag()));
            }
            listTag.add(tag);
        }
        CompoundTag rootTag = new CompoundTag();
        rootTag.put(MOP.getId(), listTag);

        //save in DRAW
        switch (MOP) {
            case MOD -> draw.set(ModDataComponents.DRAW_MOD_SAVED.get(), rootTag);
            case PROJ -> draw.set(ModDataComponents.DRAW_PROJ_SAVED.get(), rootTag);
        }

        return draw;
    }
}