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
import net.mynameistmillo.experimentalmod.WandLogic.Compact.mergeHandler.StackMergeHandler;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.GetStackFromStack;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.SaveStackIntoStack;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.wand.SaveSpells;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CompactSpells {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    //167 -> 70
    public static void compactSpells(ItemStack wand, List<ItemStack> list, Level level){
        //applying modifiers from list, so now I have DRAW and PROJ
        list = applyModifiersFromRawList(destroyDirt(list), level);
        List<ItemStack> drawQueue = new ArrayList<>();
        List<ItemStack> finalList = new ArrayList<>();

        StackMergeHandler sMH = new StackMergeHandler(level);

        for (ItemStack stack : list){


            switch (stack.getItem()){
                //if DRAW -> dQ
                case IDraw d -> {
//                    if (drawQueue.getLast().getItem() instanceof IDraw){
//                        drawQueue.set(drawQueue.size()-1, DrawStats.increaseFreeByOtherDraw(drawQueue.getLast(), stack));
//                    }
                    drawQueue.add(stack);

                    continue;
                }
                //if PROJ is trigger -> dQ
                case IProjectile p -> {
                    if (ProjStatsI.loadStatsFromProj(stack).get(StatsKeyI.TRIGGER_TYPE)>0) {
                        drawQueue.add(stack);
                        continue;
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + stack.getItem());
            }

            //when reached here STACK is non-DRAW and non-TRIGGER
            if (!drawQueue.isEmpty()){

                ItemStack lastDQ = drawQueue.getLast();
                if (!isFull(lastDQ)) drawQueue.set(drawQueue.size()-1, prepareStackAndMerge(level, drawQueue.getLast(), stack, sMH));

                if (isFull(drawQueue.getLast())){
                    if (drawQueue.size()>1) {
                        while (!drawQueue.isEmpty()) {
                            ItemStack lastStack = prepareStackAndMerge(level,
                                    drawQueue.get(drawQueue.size() - 2),
                                    drawQueue.get(drawQueue.size() - 1), sMH);
                            drawQueue.removeLast();
                            drawQueue.set(drawQueue.size() - 1, lastStack);
                            if (isFull(lastStack)) break;
                        }
                    } else {
                        finalList.add(drawQueue.getLast());
                        drawQueue.removeLast();
                    }
                }
                continue;
            }
            finalList.add(stack);

        }
        while (!drawQueue.isEmpty()) {

            if (drawQueue.size()>1){

                ItemStack beforeStack = sMH.merge(drawQueue.get(drawQueue.size()-2), drawQueue.get(drawQueue.size()-1));

                drawQueue.removeLast();
                drawQueue.set(drawQueue.size()-1, beforeStack);
            }
            if (drawQueue.size()==1){
                finalList.add(drawQueue.getLast());
                drawQueue.removeLast();
            }
        }

        SaveSpells.saveSpells(wand, finalList, level, finalList.size(), SaveOrGetTypeW.COMPACT);
    }


    private static ItemStack prepareStackAndMerge(Level level, ItemStack lDQ, ItemStack stack, StackMergeHandler sMH){

        switch (lDQ.getItem()){
            case IDraw a -> {
                return sMH.merge(DrawStats.subtractFromFree(lDQ), applyMod(level, lDQ, stack));

            }
            case IProjectile a -> {
                return sMH.merge(ProjStatsI.subtractFromFree(lDQ),
                        stack);

            }
            default -> throw new IllegalStateException("Unexpected value: " + lDQ.getItem());
        }
    }

    private static boolean isFull(ItemStack s){
        switch (s.getItem()){
            case IDraw a -> {
                return DrawStats.loadStatsFromDraw(s).get(DrawKey.FREE_SPACE) == 0;
            }
            case IProjectile a -> {
                return ProjStatsI.loadStatsFromProj(s).get(StatsKeyI.FREE_DRAW_TRIGGER) == 0;
            }
            default -> throw new IllegalStateException("Unexpected value: " + s.getItem());
        }
    }

    private static ItemStack applyMod(Level level, ItemStack before, ItemStack end){
        switch (end.getItem()){
            case IDraw a ->{
                return applyModDrawToProjDraw(before, end, level);
            }
            case IProjectile a ->{
                return applyModifiersFromDraw(before, end, level);
            }
            case null, default -> throw new IllegalStateException("Unexpected value: " + end.getItem());
        }
    }

    private static ItemStack applyModifiersFromDraw(ItemStack draw, ItemStack proj, Level level){
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
