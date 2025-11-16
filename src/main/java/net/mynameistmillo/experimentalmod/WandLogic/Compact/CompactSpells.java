package net.mynameistmillo.experimentalmod.WandLogic.Compact;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Interface.IDraw;
import net.mynameistmillo.experimentalmod.Interface.IModifier;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.DrawKey;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.DrawStats;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.SaveOrGetTypeD;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.SaveOrGetTypeW;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.SaveSpells;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CompactSpells {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public static void compactSpells(ItemStack wand, List<ItemStack> list, Level level){
        list = applyModifiersFromRawList(destroyDirt(list), level);
        List<ItemStack> drawQueue = new ArrayList<>();
        List<ItemStack> finalList = new ArrayList<>();


        for (ItemStack stack : list){

            if (stack.getItem() instanceof IDraw){
                drawQueue.add(stack);
                continue;
            }

            if (!drawQueue.isEmpty()){
                ItemStack lastDraw = drawQueue.getLast();
                if (DrawStats.loadStatsFromDraw(lastDraw).get(DrawKey.FREE_SPACE)>0){
                    stack = applyModifiersFromDraw(stack, lastDraw, level);
                    lastDraw = DrawStats.saveModOrProjIntoDrawType(level, stack, null, null, DrawStats.subtractFromFree(lastDraw),  SaveOrGetTypeD.PROJ);

                }
                if (DrawStats.loadStatsFromDraw(lastDraw).get(DrawKey.FREE_SPACE)==0){
                    if (drawQueue.size()>1){
                        for (int i = drawQueue.size(); i >= 0; i--) {

                            ItemStack endDraw = applyModDrawToProjDraw(drawQueue.get(drawQueue.size()-2), drawQueue.get(drawQueue.size()-1), level);
                            ItemStack beforeDraw = DrawStats.transferContentsDrawDrawType(level,
                                    endDraw, drawQueue.get(drawQueue.size()-2), SaveOrGetTypeD.PROJ);


                            drawQueue.removeLast();
                            drawQueue.set(drawQueue.size()-1, beforeDraw);
                            if (DrawStats.loadStatsFromDraw(drawQueue.getLast()).get(DrawKey.FREE_SPACE)!=0) break;
                        }
                    }

                } else drawQueue.set(drawQueue.size()-1, lastDraw);
                continue;
            }
            finalList.add(stack);


        }
        if (!drawQueue.isEmpty()){
            do {
                if (drawQueue.size()>1){
                    ItemStack somethingDraw = DrawStats.transferContentsDrawDrawType(level, drawQueue.get(drawQueue.size()-1),
                                                                    drawQueue.get(drawQueue.size()-2), SaveOrGetTypeD.PROJ);
                    drawQueue.removeLast();
                    drawQueue.set(drawQueue.size()-1, somethingDraw);
                }
                if (drawQueue.size()==1){
                    finalList.add(drawQueue.getLast());
                    drawQueue.removeLast();
                }

            }while (!drawQueue.isEmpty());
        }
        //finalList = applyFinalModFromDraw(finalList, level);
        SaveSpells.saveSpells(wand, finalList, level, finalList.size(), SaveOrGetTypeW.COMPACT);
    }

    private static ItemStack applyModifiersFromDraw(ItemStack proj, ItemStack draw, Level level){
        List<ItemStack> modList = DrawStats.loadModOrProjFormDrawType(level, draw, SaveOrGetTypeD.MOD);

        for (ItemStack mod : modList){
            if (mod.getItem() instanceof IModifier modifier){
                proj = modifier.applyChanges(level, proj);
            }
        }
        return proj;
    }

    private static ItemStack applyModDrawToProjDraw(ItemStack modDraw, ItemStack projDraw, Level level){
        List<ItemStack> modList = DrawStats.loadModOrProjFormDrawType(level, modDraw, SaveOrGetTypeD.MOD);
        List<ItemStack> projList = DrawStats.loadModOrProjFormDrawType(level, projDraw, SaveOrGetTypeD.PROJ);

        for (ItemStack p : projList){
            if (p.getItem() instanceof IProjectile){
                for (ItemStack m : modList){
                    if (m.getItem() instanceof IModifier modifier){
                        p = modifier.applyChanges(level, p);
                    }
                }
            }
        }
        return DrawStats.saveModOrProjIntoDrawType(level, null, null, projList, projDraw, SaveOrGetTypeD.PROJ);
    }


    private static List<ItemStack> applyFinalModFromDraw(List<ItemStack> list, Level level){
        List<ItemStack> toReturn = new ArrayList<>();
        for (ItemStack stack : list){
            if (stack.getItem() instanceof IDraw){
                List<ItemStack> modList = DrawStats.loadModOrProjFormDrawType(level, stack, SaveOrGetTypeD.MOD);
                List<ItemStack> projList = DrawStats.loadModOrProjFormDrawType(level, stack, SaveOrGetTypeD.PROJ);
                List<ItemStack> projToSave = new ArrayList<>();
                for (ItemStack proj : projList){
                    if (proj.getItem() instanceof IProjectile){
                        for (ItemStack mod : modList){
                            if (mod.getItem() instanceof IModifier modifier){
                                proj = modifier.applyChanges(level, proj);
                            }
                        }
                        projToSave.add(proj);
                    }
                }
                toReturn.add(DrawStats.saveModOrProjIntoDrawType(level,null, null, projToSave, stack, SaveOrGetTypeD.PROJ));
            }
            else toReturn.add(stack);

        }
        return toReturn;
    }



    private static List<ItemStack> applyModifiersFromRawList(List<ItemStack> list, Level level){
        List<ItemStack> finalList = new ArrayList<>();
        List<ItemStack> modList = new ArrayList<>();

        for (ItemStack stack : list){
            switch (stack.getItem()) {
                case IModifier modifier -> {
                    modList.add(stack);
                }
                case IProjectile iProjectile when !modList.isEmpty() -> {
                    for (ItemStack mod : modList) {
                        if (mod.getItem() instanceof IModifier modifier) {
                            stack = modifier.applyChanges(level, stack);
                        }
                    }
                    finalList.add(stack);
                    modList.clear();
                }
                case IDraw iDraw when !modList.isEmpty() -> {
                    for (ItemStack mod : modList) {
                        if (mod.getItem() instanceof IModifier) {
                            stack = DrawStats.saveModOrProjIntoDrawType(level, mod,null, null, stack, SaveOrGetTypeD.MOD);
                        }
                    }
                    finalList.add(stack);
                    modList.clear();
                }
                default -> finalList.add(stack);
            }
        }
        return finalList;
    }

    private static List<ItemStack> destroyDirt(List<ItemStack> D){
        List<ItemStack> nD = new ArrayList<>();
        for (ItemStack s : D) if (!s.is(Items.DIRT)) nD.add(s);
        return nD;
    }



}
