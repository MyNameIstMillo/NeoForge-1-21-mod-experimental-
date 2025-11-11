package net.mynameistmillo.experimentalmod.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.GetSavedSpells;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.SaveSpells;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.SaveOrGetType;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import net.mynameistmillo.experimentalmod.Interface.IDraw;
import net.mynameistmillo.experimentalmod.Interface.IModifier;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.DrawKey;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.DrawStats;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WandItem extends Item {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);
    public WandItem(Properties properties, int capacity) {
        super(properties);
        this.defaultCapacity = capacity;
    }

    private final int defaultCapacity;

    public int getCapacity(ItemStack wand) {
        Integer capacity = wand.get(ModDataComponents.WAND_CAPACITY.get());
        if (capacity == null) {
            capacity = defaultCapacity;
            wand.set(ModDataComponents.WAND_CAPACITY.get(), capacity);
        }
        return defaultCapacity;
    }

    public int getCurrentIndex(ItemStack wand){
        Integer capacity = wand.get(ModDataComponents.WAND_CAPACITY_COMPACT.get());
        Integer index = wand.get(ModDataComponents.WAND_INDEX.get());
        if(index == null){
            index = 0;
            wand.set(ModDataComponents.WAND_INDEX.get(), index);
        }
        return Math.floorMod(index, capacity);
    }

    public void setCurrentIndex(ItemStack wand, int index){
        wand.set(ModDataComponents.WAND_INDEX.get(), index);
    }

    public void increaseIndex(ItemStack wand){
        int index = wand.get(ModDataComponents.WAND_INDEX.get());
        int capacity = wand.get(ModDataComponents.WAND_CAPACITY_COMPACT.get());
        index = (index+1)%capacity;
        wand.set(ModDataComponents.WAND_INDEX.get(), index);
    }

    public List<ItemStack> resetSpellStats(List<ItemStack> list){
        List<ItemStack> l2 = new ArrayList<>();

        for (ItemStack stack : list){
            if (stack.getItem() instanceof IProjectile) {
                ProjStatsF stats = new ProjStatsF();
                ProjStatsI statsI = new ProjStatsI();
                ItemStack s2 = stats.resetStats(statsI.resetStats(stack));

                l2.add(s2);
                continue;
            }
            if (stack.getItem() instanceof IDraw){
                DrawStats stats = new DrawStats();
                ItemStack s2 = stats.resetsStats(stack);
                l2.add(s2);
                continue;
            }
            l2.add(stack);
        }
        return l2;
    }



    public boolean areThereSpellsInWand(ItemStack wand, Level level) {
        if (!(wand.getItem() instanceof WandItem wandItem)) {
            return false;
        }
        List<ItemStack> contents = GetSavedSpells.getSavedSpellsType(wand,level,
                getCapacity(wand), SaveOrGetType.NORMAL);

        for (ItemStack stack : contents) {

            if (!stack.is(Items.DIRT)) {
                return true;
            }
        }
        return false;
    }



    public List<ItemStack> destroyEmpty(List<ItemStack> list){
        List<ItemStack> nonEmpty = new ArrayList<>();
        for(ItemStack stack : list){
            if(stack.is(Items.DIRT)) continue;
            nonEmpty.add(stack);
        }
        return nonEmpty;
    }

    public void compactSpells(ItemStack wand, List<ItemStack> list, Level level){
         List<ItemStack> allList = destroyEmpty(resetSpellStats(list));

         List<ItemStack> spellList = new ArrayList<>();
         List<Integer> deleteIndexList = new ArrayList<>();
         int maxIndex = allList.size();

         for (int i=0; i<maxIndex; i++) {
             ItemStack stack = allList.get(i);

             if (stack.getItem() instanceof IModifier modifier) {
                 int nextIndexOfSomeThing = findIndexOfNextSpell(allList, i);
                 if (nextIndexOfSomeThing > 0) {
                     ItemStack nextItemStackSomeThing = allList.get(nextIndexOfSomeThing);

                     if(nextItemStackSomeThing.getItem() instanceof IProjectile){
                         ItemStack editedStack = modifier.applyChanges(level, nextItemStackSomeThing);

                         allList.set(nextIndexOfSomeThing, editedStack.copy());
                     }

                     if (nextItemStackSomeThing.getItem() instanceof IDraw){
                         DrawStats drawStats = new DrawStats();
                         ItemStack drawStack = drawStats.saveModifiersIntoStack(level, stack, nextItemStackSomeThing);

                         allList.set(nextIndexOfSomeThing, drawStack.copy());

                     }
                 }
             }

             if (stack.getItem() instanceof  IDraw draw){
                DrawStats stats = new DrawStats().loadStatsFromDraw(stack);

                List<ItemStack> modList = stats.loadModifiersFormStack(level, stack);
                int drawSize = stats.get(DrawKey.DRAW), projCount = 0, index = i, temp = i ;

                while (projCount < drawSize && index < maxIndex) {
                    ItemStack proj = allList.get(index);

                    if (proj.getItem() instanceof IDraw iDraw && index>temp){
                        DrawStats drawStats = new DrawStats().loadStatsFromDraw(proj);
                        int drawTemp = drawStats.get(DrawKey.DRAW) - 1;
                        drawSize += drawTemp;
                        stats.set(DrawKey.DRAW, drawSize);
                        deleteIndexList.add(index);
                    }
                    if (proj.getItem() instanceof IProjectile iProj) {
                        ItemStack editedProj = applyModifiersToProjFromDraw(level, modList, proj);
                        allList.set(index, editedProj);
                        projCount++;
                    }
                    index++;
                }

                stack = stats.saveStatsDraw(stats, stack);
                spellList.add(stack);
             }

             if (stack.getItem() instanceof IProjectile) {
                 spellList.add(stack.copy());
             }
         }

         spellList = deleteIndexList(spellList, deleteIndexList);
         SaveSpells.saveSpells(wand, spellList, level, spellList.size(), SaveOrGetType.COMPACT);
    }

    public Integer findIndexOfNextSpell(List<ItemStack> list, int index){
        for (int i=index; i<list.size(); i++){
            if (list.get(i).getItem() instanceof IProjectile) return i;
            if (list.get(i).getItem() instanceof IDraw) return i;
        }

        return -1;
    }

    public ItemStack applyModifiersToProjFromDraw(Level level, List<ItemStack> drawList, ItemStack proj){

        ItemStack editedProj = proj;
        for (ItemStack mod : drawList){
            if (mod.getItem() instanceof IModifier modifier){
                editedProj = modifier.applyChanges(level, editedProj);
                //ProjStatsF stats = new ProjStatsF().loadStatsFromStack(editedProj);
            }
        }
        return editedProj;
    }

    public List<ItemStack> deleteIndexList(List<ItemStack> list, List<Integer> deleteList){
        for (int index : deleteList){
            list.remove(index);
        }
        return list;
    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack wand = player.getItemInHand(usedHand);
        if(level.isClientSide())    return InteractionResultHolder.pass(wand);

        int index = getCurrentIndex(wand);
        List<ItemStack> storedSpells = GetSavedSpells.getSavedSpellsType(wand, level,
                                                    0, SaveOrGetType.COMPACT);
        int maxIndex = storedSpells.size();

        ItemStack currentStack = storedSpells.get(index);

        if(currentStack.getItem() instanceof IProjectile projectile){

            Entity entity = projectile.spawnSpell(level, player.getOnPos(), player,
                    player.getLookAngle(), wand, currentStack, index);

            increaseIndex(wand);

           return InteractionResultHolder.success(wand);
        }

        if(currentStack.getItem() instanceof IDraw draw){

            DrawStats stats = new DrawStats().loadStatsFromDraw(currentStack);
            int draw_max = stats.get(DrawKey.DRAW) + index;
            int indexEnd = index;

            for (int i=index+1; i<draw_max+1; i++){
                if (i > maxIndex) {
                    setCurrentIndex(wand, 0);
                    break;
                }
                ItemStack proj = storedSpells.get(i);
                if (proj.getItem() instanceof IProjectile projectile){
                    Entity entity = projectile.spawnSpell(level, player.getOnPos(), player,
                            player.getLookAngle(), wand, proj, i);

                }
                indexEnd++;

            }
            setCurrentIndex(wand, indexEnd);
            increaseIndex(wand);
            return InteractionResultHolder.success(wand);
        }


        return InteractionResultHolder.pass(wand);
    }

    @Override
    public void appendHoverText(ItemStack wand, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        Level level = context.level();
        int capacity = getCapacity(wand);

        if (Screen.hasShiftDown()){
            List<ItemStack> spells = GetSavedSpells.getSavedSpellsType(wand, level,
                                                capacity, SaveOrGetType.NORMAL);
            tooltip.add(Component.literal(" Spells:").withStyle(ChatFormatting.GRAY));



            for(int i=0; i<capacity; i++){
                if (!spells.get(i).is(Items.DIRT)) {
                    tooltip.add(Component.literal(i+1 + ": " + spells.get(i).getHoverName().getString()).withStyle(ChatFormatting.GRAY));
                }
                else{
                    tooltip.add(Component.literal(i+1 + ": - - - - - ").withStyle(ChatFormatting.DARK_GRAY));
                }

            }
        }
        else {
            tooltip.add(Component.literal("Press SHIFT to view Spells.").withStyle(ChatFormatting.GRAY));
        }
    }

}