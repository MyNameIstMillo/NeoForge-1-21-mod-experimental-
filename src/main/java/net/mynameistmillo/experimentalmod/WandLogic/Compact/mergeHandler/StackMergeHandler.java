package net.mynameistmillo.experimentalmod.WandLogic.Compact.mergeHandler;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.Enum.ModOrProj;
import net.mynameistmillo.experimentalmod.Enum.ProjOrDraw;
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

                new StackState(ProjOrDraw.PROJ, ProjOrDraw.PROJ),
                (TRIGGER, PROJ) ->

                        SaveStackIntoStack.projToTrigger(level, PROJ, null, null, TRIGGER),


                new StackState(ProjOrDraw.DRAW, ProjOrDraw.PROJ),
                (DRAW, PROJ) ->

                        SaveStackIntoStack.stackToDraw(level, PROJ, null, null, DRAW, ModOrProj.PROJ),


                new StackState(ProjOrDraw.PROJ, ProjOrDraw.DRAW),
                (PROJ, DRAW) ->

                        SaveStackIntoStack.projToTrigger(level, null, GetStackFromStack.stackFromDraw(level, DRAW, ModOrProj.PROJ), null, PROJ),


                new StackState(ProjOrDraw.DRAW, ProjOrDraw.DRAW),
                (DRAW1, DRAW2) ->

                        DrawStats.transferContentsDrawDrawType(level, DRAW2, DRAW1, ModOrProj.PROJ)
        );
    }

    public ItemStack merge(ItemStack before, ItemStack end){
        StackState state= new StackState(type(before), type(end));
        return handler.get(state).apply(before, end);
    }

    private ProjOrDraw type(ItemStack stack){
        Item item = stack.getItem();
        switch (item){
            case IProjectile p -> {
                return ProjOrDraw.PROJ;
            }
            case IDraw d -> {
                return ProjOrDraw.DRAW;
            }
            default -> throw new IllegalStateException("Unsupported item: "+item);
        }
    }
}
