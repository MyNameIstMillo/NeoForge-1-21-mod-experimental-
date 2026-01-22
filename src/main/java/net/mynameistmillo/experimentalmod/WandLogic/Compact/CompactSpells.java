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
import net.mynameistmillo.experimentalmod.Enum.ModOrProjType;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyI;
import net.mynameistmillo.experimentalmod.Enum.SaveOrGetTypeW;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.GetStackFromStack;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.SaveStackIntoStack;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.wand.SaveSpells;
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


            switch (stack.getItem()){
                //if DRAW -> dQ
                case IDraw d -> {
                    drawQueue.add(stack);
                    continue;
                }
                //if PROJ is trigger -> dQ
                case IProjectile p -> {
                    int tt = ProjStatsI.loadStatsFromProj(stack).get(StatsKeyI.TRIGGER_TYPE);
                    if (tt==1 || tt==2 || tt==3) {
                        drawQueue.add(stack);
                        continue;
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + stack.getItem());
            }


            //when reached here STACK is non-DRAW and non-TRIGGER
            if (!drawQueue.isEmpty()){
                ItemStack lastDrawStack = drawQueue.getLast();
                
                switch (lastDrawStack.getItem()){
                    //new stack and last from dQ -> DRAW
                    case IDraw draw -> {
                        if (DrawStats.loadStatsFromDraw(lastDrawStack).get(DrawKey.FREE_SPACE)>0){
                            stack = applyModifiersFromDraw(stack, lastDrawStack, level);
                            lastDrawStack = SaveStackIntoStack.stackToDraw(level, stack, null, null,
                                    DrawStats.subtractFromFree(lastDrawStack),  ModOrProjType.PROJ);

                        }
                    }

                    //new stack and last from dQ -> TRIGGER
                    case IProjectile proj -> {
                        if (ProjStatsI.loadStatsFromProj(lastDrawStack).get(StatsKeyI.FREE_DRAW_TRIGGER)>0){
                            lastDrawStack = SaveStackIntoStack.projToTrigger(level, stack, null, null,
                                    ProjStatsI.subtractFromFree(lastDrawStack), ModOrProjType.PROJ);

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
                                    ItemStack beforeStack = drawQueue.get(drawQueue.size() - 2);

                                    //TO -> TRIGGER
                                    if (beforeStack.getItem() instanceof IProjectile){
                                        beforeStack = SaveStackIntoStack.projToTrigger(level, null,
                                                GetStackFromStack.stackFromDraw(level, endStack, ModOrProjType.PROJ),
                                                null, beforeStack, ModOrProjType.PROJ);
                                        drawQueue.removeLast();
                                        drawQueue.set(drawQueue.size() - 1, ProjStatsI.subtractFromFree(beforeStack));

                                        if (ProjStatsI.loadStatsFromProj(beforeStack).get(StatsKeyI.FREE_DRAW_TRIGGER) == 0) break;

                                    }
                                    //TO -> DRAW
                                    else {
                                        endStack = applyModDrawToProjDraw(beforeStack, endStack, level);
                                        beforeStack = DrawStats.transferContentsDrawDrawType(level,
                                                endStack, DrawStats.subtractFromFree(beforeStack), ModOrProjType.PROJ);

                                        drawQueue.removeLast();
                                        drawQueue.set(drawQueue.size() - 1, beforeStack);
                                        if (DrawStats.loadStatsFromDraw(beforeStack).get(DrawKey.FREE_SPACE) == 0) break;
                                    }
                                }
                            } else {
                                finalList.add(drawQueue.getLast());
                                drawQueue.removeLast();
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
                                        beforeStack = SaveStackIntoStack.projToTrigger(level, endStack, null, null,
                                                beforeStack, ModOrProjType.PROJ);
                                        drawQueue.removeLast();
                                        drawQueue.set(drawQueue.size() - 1, beforeStack);
                                        if (ProjStatsI.loadStatsFromProj(beforeStack).get(StatsKeyI.FREE_DRAW_TRIGGER)!=0) break;
                                    }
                                    //TO -> DRAW
                                    else {
                                        beforeStack = SaveStackIntoStack.stackToDraw(level, endStack, null, null,
                                                beforeStack, ModOrProjType.PROJ);
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
            while (!drawQueue.isEmpty()) {
                if (drawQueue.size()>1){
                    ItemStack endStack = drawQueue.get(drawQueue.size()-1);
                    ItemStack beforeStack = drawQueue.get(drawQueue.size()-2);

                    if (endStack.getItem() instanceof IProjectile && beforeStack.getItem() instanceof IProjectile){
                        beforeStack = SaveStackIntoStack.projToTrigger(level, endStack, null, null, beforeStack, ModOrProjType.PROJ);
                    } else
                        if (endStack.getItem() instanceof IProjectile && beforeStack.getItem() instanceof IDraw) {
                            beforeStack = SaveStackIntoStack.stackToDraw(level, endStack, null, null, beforeStack, ModOrProjType.PROJ);
                        } else
                            if (endStack.getItem() instanceof IDraw && beforeStack.getItem() instanceof IProjectile) {
                                beforeStack = SaveStackIntoStack.stackToDraw(level, null,
                                        GetStackFromStack.stackFromDraw(level, endStack, ModOrProjType.PROJ), null, beforeStack, ModOrProjType.PROJ);
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
            }
        }
        SaveSpells.saveSpells(wand, finalList, level, finalList.size(), SaveOrGetTypeW.COMPACT);
    }

    private static ItemStack applyModifiersFromDraw(ItemStack proj, ItemStack draw, Level level){
        List<ItemStack> modList = GetStackFromStack.stackFromDraw(level, draw, ModOrProjType.MOD);

        for (ItemStack mod : modList){
            if (mod.getItem() instanceof IModifier modifier){
                proj = modifier.applyChanges(level, proj);
            }
        }
        return proj;
    }

    private static ItemStack applyModDrawToProjDraw(ItemStack modDraw, ItemStack projDraw, Level level){
        List<ItemStack> modList = GetStackFromStack.stackFromDraw(level, modDraw, ModOrProjType.MOD);
        List<ItemStack> projList = GetStackFromStack.stackFromDraw(level, projDraw, ModOrProjType.PROJ);

        for (ItemStack p : projList){
            if (p.getItem() instanceof IProjectile){
                for (ItemStack m : modList){
                    if (m.getItem() instanceof IModifier modifier){
                        p = modifier.applyChanges(level, p);
                    }
                }
            }
        }
        return SaveStackIntoStack.stackToDraw(level, null, null, projList, projDraw, ModOrProjType.PROJ);
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
                            stack = SaveStackIntoStack.stackToDraw(level, mod,null, null, stack,
                                    ModOrProjType.MOD);
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
