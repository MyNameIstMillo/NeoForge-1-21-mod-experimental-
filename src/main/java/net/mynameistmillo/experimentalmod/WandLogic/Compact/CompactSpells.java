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
import net.mynameistmillo.experimentalmod.Stats.DrawItem.ModOrProjType;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyI;
import net.mynameistmillo.experimentalmod.WandLogic.Types.DrawOrTriggerType;
import net.mynameistmillo.experimentalmod.WandLogic.Types.SaveOrGetTypeW;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.SaveSpells;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CompactSpells {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public static void compactSpells(ItemStack wand, List<ItemStack> list, Level level){
        //applying modifiers from list, so now I have DRAW and PROJ
        list = applyModifiersFromRawList(destroyDirt(list), level);
        List<ItemStack> drawQueue = new ArrayList<>();
        List<ItemStack> finalList = new ArrayList<>();

        

        for (ItemStack stack : list){


            //if DRAW -> dQ
            if (stack.getItem() instanceof IDraw){
                drawQueue.add(stack);
                continue;
            }
            
            //if PROJ is trigger -> dQ
            if (stack.getItem() instanceof IProjectile){
                ProjStatsI statsI = new ProjStatsI().loadStatsFromProj(stack);
                int tt = statsI.get(StatsKeyI.TRIGGER_TYPE);
                if (tt==1 || tt==10 || tt==30) {
                    drawQueue.add(stack);
                    continue;
                }
            }

            //when reached here STACK is non-DRAW and non-TRIGGER
            if (!drawQueue.isEmpty()){
                ItemStack lastDrawStack = drawQueue.getLast();
                
                switch (lastDrawStack.getItem()){
                    //new stack and last from dQ -> DRAW
                    case IDraw draw -> {
                        if (DrawStats.loadStatsFromDraw(lastDrawStack).get(DrawKey.FREE_SPACE)>0){
                            stack = applyModifiersFromDraw(stack, lastDrawStack, level);
                            lastDrawStack = DrawStats.saveModOrProjIntoDrawOrTriggerTypeType(level, stack, null, null,
                                    DrawStats.subtractFromFree(lastDrawStack),  ModOrProjType.PROJ, DrawOrTriggerType.DRAW);

                        }
                    }

                    //new stack and last from dQ -> TRIGGER
                    case IProjectile proj -> {
                        if (ProjStatsI.loadStatsFromProj(lastDrawStack).get(StatsKeyI.FREE_DRAW_TRIGGER)>0){
                            lastDrawStack = DrawStats.saveModOrProjIntoDrawOrTriggerTypeType(level, stack, null, null,
                                    ProjStatsI.subtractFromFree(lastDrawStack), ModOrProjType.PROJ, DrawOrTriggerType.TRIGGER);

                        }

                    }
                    default -> throw new IllegalStateException("Unexpected value: " + lastDrawStack.getItem());
                }



                switch (lastDrawStack.getItem()) {
                    //LAST -> DRAW
                    case IDraw draw -> {
                        if (DrawStats.loadStatsFromDraw(lastDrawStack).get(DrawKey.FREE_SPACE) == 0) {
                            if (drawQueue.size() > 1) {
                                for (int i = drawQueue.size(); i >= 1; i--) {
                                    ItemStack endStack = drawQueue.get(drawQueue.size() - 1);
                                    ItemStack beforeStack = drawQueue.get(drawQueue.size() - 2);//<-BHUFDSYUFDBYUFDBYUFDSBYUFDSBYUFDS

                                    //TO -> TRIGGER
                                    if (beforeStack.getItem() instanceof IProjectile){
                                        beforeStack = DrawStats.saveModOrProjIntoDrawOrTriggerTypeType(level, null,
                                                DrawStats.loadModOrProjFormDrawOrTriggerTypeType(level, endStack, ModOrProjType.PROJ, DrawOrTriggerType.DRAW),
                                                null, beforeStack, ModOrProjType.PROJ, DrawOrTriggerType.TRIGGER);
                                        drawQueue.removeLast();
                                        drawQueue.set(drawQueue.size() - 1, ProjStatsI.subtractFromFree(beforeStack));

                                        if (ProjStatsI.loadStatsFromProj(beforeStack).get(StatsKeyI.FREE_DRAW_TRIGGER) == 0) break;

                                    }
                                    //TO -> DRAW
                                    else {
                                        endStack = applyModDrawToProjDraw(beforeStack, endStack, level);
                                        beforeStack = DrawStats.transferContentsDrawDrawType(level,
                                                endStack, beforeStack, ModOrProjType.PROJ);

                                        drawQueue.removeLast();
                                        drawQueue.set(drawQueue.size() - 1, beforeStack);
                                        if (DrawStats.loadStatsFromDraw(beforeStack).get(DrawKey.FREE_SPACE) == 0) break;
                                    }
                                }
                            }

                        } else drawQueue.set(drawQueue.size() - 1, lastDrawStack);
                        continue;
                    }
                    //LAST -> TRIGGER
                    case IProjectile proj -> {
                        if (ProjStatsI.loadStatsFromProj(lastDrawStack).get(StatsKeyI.FREE_DRAW_TRIGGER) == 0){
                            if (drawQueue.size()>1){
                                for (int i= drawQueue.size(); i>=0; i--){
                                    ItemStack endStack = drawQueue.get(drawQueue.size() - 1);
                                    ItemStack beforeStack = drawQueue.get(drawQueue.size() - 2);

                                    //TO -> TRIGGER
                                    if (beforeStack.getItem() instanceof IProjectile){
                                        beforeStack = DrawStats.saveModOrProjIntoDrawOrTriggerTypeType(level, endStack, null, null,
                                                beforeStack, ModOrProjType.PROJ, DrawOrTriggerType.TRIGGER);
                                        drawQueue.removeLast();
                                        drawQueue.set(drawQueue.size() - 1, beforeStack);
                                        if (ProjStatsI.loadStatsFromProj(beforeStack).get(StatsKeyI.FREE_DRAW_TRIGGER)!=0) break;
                                    }
                                    //TO -> DRAW
                                    else {
                                        beforeStack = DrawStats.saveModOrProjIntoDrawOrTriggerTypeType(level, endStack, null, null,
                                                beforeStack, ModOrProjType.PROJ, DrawOrTriggerType.DRAW);
                                        drawQueue.removeLast();
                                        drawQueue.set(drawQueue.size() - 1, beforeStack);
                                        if (DrawStats.loadStatsFromDraw(drawQueue.getLast()).get(DrawKey.FREE_SPACE) != 0) break;
                                    }
                                }
                            }
                            else {
                                finalList.add(drawQueue.getLast());
                                drawQueue.removeLast();
                            }
                        } else drawQueue.set(drawQueue.size() - 1, lastDrawStack);
                        continue;
                        
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + lastDrawStack.getItem());
                }
            }
            finalList.add(stack);


        }
        if (!drawQueue.isEmpty()){
            do {
                if (drawQueue.size()>1){
                    ItemStack endStack = drawQueue.get(drawQueue.size()-1);
                    ItemStack beforeStack = drawQueue.get(drawQueue.size()-2);

                    if (endStack.getItem() instanceof IProjectile && beforeStack.getItem() instanceof IProjectile){
                        beforeStack = DrawStats.saveModOrProjIntoDrawOrTriggerTypeType(level, endStack, null, null, beforeStack, ModOrProjType.PROJ, DrawOrTriggerType.TRIGGER);
                    } else
                        if (endStack.getItem() instanceof IProjectile && beforeStack.getItem() instanceof IDraw) {
                            beforeStack = DrawStats.saveModOrProjIntoDrawOrTriggerTypeType(level, endStack, null, null, beforeStack, ModOrProjType.PROJ, DrawOrTriggerType.DRAW);
                        } else
                            if (endStack.getItem() instanceof IDraw && beforeStack.getItem() instanceof IProjectile) {
                                beforeStack = DrawStats.saveModOrProjIntoDrawOrTriggerTypeType(level, null,
                                        DrawStats.loadModOrProjFormDrawOrTriggerTypeType(level, endStack, ModOrProjType.PROJ, DrawOrTriggerType.DRAW), null, beforeStack, ModOrProjType.PROJ, DrawOrTriggerType.DRAW);
                            } else
                                if (endStack.getItem() instanceof IDraw && beforeStack.getItem() instanceof IDraw) {
                                    beforeStack = DrawStats.transferContentsDrawDrawType(level, endStack, beforeStack, ModOrProjType.PROJ);
                                }

                    drawQueue.removeLast();
                    drawQueue.set(drawQueue.size()-1, beforeStack);
                }
                if (drawQueue.size()==1){
                    finalList.add(drawQueue.getLast());
                    drawQueue.removeLast();
                }

            }while (!drawQueue.isEmpty());
        }
        SaveSpells.saveSpells(wand, finalList, level, finalList.size(), SaveOrGetTypeW.COMPACT);
    }

    private static ItemStack applyModifiersFromDraw(ItemStack proj, ItemStack draw, Level level){
        List<ItemStack> modList = DrawStats.loadModOrProjFormDrawOrTriggerTypeType(level, draw, ModOrProjType.MOD, DrawOrTriggerType.DRAW);

        for (ItemStack mod : modList){
            if (mod.getItem() instanceof IModifier modifier){
                proj = modifier.applyChanges(level, proj);
            }
        }
        return proj;
    }

    private static ItemStack applyModDrawToProjDraw(ItemStack modDraw, ItemStack projDraw, Level level){
        List<ItemStack> modList = DrawStats.loadModOrProjFormDrawOrTriggerTypeType(level, modDraw, ModOrProjType.MOD, DrawOrTriggerType.DRAW);
        List<ItemStack> projList = DrawStats.loadModOrProjFormDrawOrTriggerTypeType(level, projDraw, ModOrProjType.PROJ, DrawOrTriggerType.DRAW);

        for (ItemStack p : projList){
            if (p.getItem() instanceof IProjectile){
                for (ItemStack m : modList){
                    if (m.getItem() instanceof IModifier modifier){
                        p = modifier.applyChanges(level, p);
                    }
                }
            }
        }
        return DrawStats.saveModOrProjIntoDrawOrTriggerTypeType(level, null, null, projList, projDraw, ModOrProjType.PROJ, DrawOrTriggerType.DRAW);
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
                            stack = DrawStats.saveModOrProjIntoDrawOrTriggerTypeType(level, mod,null, null, stack,
                                    ModOrProjType.MOD, DrawOrTriggerType.DRAW);
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

    private static List<ItemStack> destroyDirt(List<ItemStack> D) {
        List<ItemStack> nD = new ArrayList<>();
        for (ItemStack s : D) if (!s.is(Items.DIRT)) nD.add(s);
        return nD;
    }
}
