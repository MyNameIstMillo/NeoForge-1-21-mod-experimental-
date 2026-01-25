package net.mynameistmillo.experimentalmod.WandLogic.Compact.mergeHandler;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Enum.ModOrProjType;
import net.mynameistmillo.experimentalmod.Enum.ProjOrDrawType;
import net.mynameistmillo.experimentalmod.Interface.IDraw;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.DrawStats;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.GetStackFromStack;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.SaveStackIntoStack;

import java.util.Map;
import java.util.function.BiFunction;

public class StackMergeHandler {

    Map<StackState, BiFunction<ItemStack, ItemStack, ItemStack>> handler;

    public StackMergeHandler(Level level){
        handler = Map.of(

                new StackState(ProjOrDrawType.PROJ, ProjOrDrawType.PROJ),
                (TRIGGER, PROJ) ->

                        SaveStackIntoStack.projToTrigger(level, TRIGGER, null, null, PROJ),


                new StackState(ProjOrDrawType.DRAW, ProjOrDrawType.PROJ),
                (DRAW, PROJ) ->

                        SaveStackIntoStack.stackToDraw(level, PROJ, null, null, DRAW, ModOrProjType.PROJ),


                new StackState(ProjOrDrawType.PROJ, ProjOrDrawType.DRAW),
                (PROJ, DRAW) ->

                        SaveStackIntoStack.projToTrigger(level, null, GetStackFromStack.stackFromDraw(level, DRAW, ModOrProjType.PROJ), null, PROJ),


                new StackState(ProjOrDrawType.DRAW, ProjOrDrawType.DRAW),
                (DRAW1, DRAW2) ->

                        DrawStats.transferContentsDrawDrawType(level, DRAW2, DRAW1, ModOrProjType.PROJ)
        );
    }

    public ItemStack merge(ItemStack before, ItemStack end){
        StackState state= new StackState(type(before), type(end));
        return handler.get(state).apply(before, end);
    }

    private ProjOrDrawType type(ItemStack stack){
        Item item = stack.getItem();
        switch (item){
            case IProjectile p -> {
                return ProjOrDrawType.PROJ;
            }
            case IDraw d -> {
                return ProjOrDrawType.DRAW;
            }
            default -> throw new IllegalStateException("Unsupported item: "+item);
        }
    }
}
