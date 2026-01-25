package net.mynameistmillo.experimentalmod.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Enum.ModOrProjType;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.GetStackFromStack;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.wand.GetSpells;
import net.mynameistmillo.experimentalmod.Enum.CasterOrBlockPosType;
import net.mynameistmillo.experimentalmod.Enum.SaveOrGetTypeW;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import net.mynameistmillo.experimentalmod.Interface.IDraw;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
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

    public void increaseIndex(ItemStack wand){
        int index = wand.get(ModDataComponents.WAND_INDEX.get());
        int capacity = wand.get(ModDataComponents.WAND_CAPACITY_COMPACT.get());
        index = (index+1)%capacity;
        wand.set(ModDataComponents.WAND_INDEX.get(), index);
    }

    public boolean areThereSpellsInWand(ItemStack wand, Level level) {
        if (!(wand.getItem() instanceof WandItem wandItem)) {
            return false;
        }
        List<ItemStack> contents = GetSpells.getSpellsType(wand,level,
                getCapacity(wand), SaveOrGetTypeW.NORMAL);

        for (ItemStack stack : contents) {

            if (!stack.is(Items.DIRT)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack wand = player.getItemInHand(usedHand);
        if(level.isClientSide())    return InteractionResultHolder.pass(wand);

        int index = getCurrentIndex(wand);
        List<ItemStack> storedSpells = GetSpells.getSpellsType(wand, level,
                                                    0, SaveOrGetTypeW.COMPACT);

        ItemStack currentStack = storedSpells.get(index);

        if(currentStack.getItem() instanceof IProjectile p){

            p.spawnProj(level, player.getOnPos(), player,
                    player.getLookAngle(), wand, currentStack, CasterOrBlockPosType.CASTER);

            increaseIndex(wand);

           return InteractionResultHolder.success(wand);
        }

        if(currentStack.getItem() instanceof IDraw ){

            List<ItemStack> projList = GetStackFromStack.stackFromDraw(level, currentStack, ModOrProjType.PROJ);

            for (ItemStack stack : projList){
                if (stack.getItem() instanceof IProjectile p){
                    p.spawnProj(level, player.getOnPos(), player,
                            player.getLookAngle(), wand, stack, CasterOrBlockPosType.CASTER);
                }
            }
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
            List<ItemStack> spells = GetSpells.getSpellsType(wand, level,
                                                capacity, SaveOrGetTypeW.NORMAL);
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