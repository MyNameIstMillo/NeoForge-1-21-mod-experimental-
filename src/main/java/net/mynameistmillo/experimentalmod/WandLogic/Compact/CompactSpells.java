package net.mynameistmillo.experimentalmod.WandLogic.Compact;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Interface.IDraw;
import net.mynameistmillo.experimentalmod.Interface.IModifier;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.DrawKey;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.DrawStats;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.SaveOrGetTypeD;

import java.util.ArrayList;
import java.util.List;

public class CompactSpells {

    public static void compactSpells(ItemStack wand, List<ItemStack> list, Level level){
        list = applyModifiersFromRawList(list, level);
        List<ItemStack> drawQueue = new ArrayList<>();
        List<ItemStack> finalList = new ArrayList<>();


        for (ItemStack stack : list){

            if (stack.getItem() instanceof IDraw){
                drawQueue.add(stack);
                continue;
            }

            if (!drawQueue.isEmpty()){
                ItemStack lastD = drawQueue.getLast();
                DrawStats dS = new DrawStats().loadStatsFromDraw(lastD);
                if (dS.get(DrawKey.FREE_SPACE)>0){
                    stack = applyModifiersFromDraw(stack, lastD, level);
                    lastD = dS.saveModOrProjIntoDrawType(level, stack, lastD, SaveOrGetTypeD.PROJ);
                    dS.subtractFromFree();
                }
                if (dS.get(DrawKey.FREE_SPACE)==0){
                    drawQueue.removeLast();
                    finalList.add(lastD);
                } else drawQueue.set(drawQueue.size(), lastD);
                continue;
            }





        }





    }

    private static ItemStack applyModifiersFromDraw(ItemStack proj, ItemStack draw, Level level){
        DrawStats dS = new DrawStats().loadStatsFromDraw(draw);
        List<ItemStack> modList = dS.loadModOrProjFormDrawType(level, draw, SaveOrGetTypeD.MOD);

        for (ItemStack mod : modList){
            if (mod.getItem() instanceof IModifier modifier){
                proj = modifier.applyChanges(level, proj);
            }
        }
        return proj;
    }


    private static List<ItemStack> applyModifiersFromRawList(List<ItemStack> list, Level level){
        List<ItemStack> finalList = new ArrayList<>();
        List<ItemStack> modList = new ArrayList<>();

        for (ItemStack stack : list){
            if (stack.getItem() instanceof IModifier){
                modList.add(stack);
                continue;
            }

            if (stack.getItem() instanceof IProjectile && !modList.isEmpty()){
                for(ItemStack mod : modList){
                    if (mod.getItem() instanceof IModifier modifier){
                        stack = modifier.applyChanges(level, stack);
                    }
                }
                finalList.add(stack.copy());
                modList.clear();
                continue;
            }

            if (stack.getItem() instanceof IDraw && !modList.isEmpty()){
                DrawStats dS = new DrawStats();
                for(ItemStack mod : modList){
                    if (mod.getItem() instanceof IModifier){
                        stack = dS.saveModOrProjIntoDrawType(level, mod, stack, SaveOrGetTypeD.MOD);
                    }
                }
                finalList.add(stack.copy());
                modList.clear();
                continue;
            }
            finalList.add(stack.copy());
        }
        return finalList;
    }



}
