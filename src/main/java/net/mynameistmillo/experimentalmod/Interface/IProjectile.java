package net.mynameistmillo.experimentalmod.Interface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Enum.CasterOrBlockPosType;
import net.mynameistmillo.experimentalmod.Enum.TriggerType;
import org.checkerframework.checker.nullness.qual.Nullable;


public interface IProjectile {

    ProjStatsF getBaseStatsF();
    ProjStatsI getBaseStatsI();


    Entity spawnProj(Level level,
                     Vec3 pos,
                     Player caster,
                     Vec3 normal,
                     ItemStack wandStack,
                     ItemStack thisProj,
                     CasterOrBlockPosType COP);

    //when HIT something
    void triggerAction(Level level,
                       @Nullable Entity hitEntity,
                       @Nullable Vec3 hitPos,
                       Player caster,
                       Vec3 normal,
                       ItemStack wandStack,
                       ItemStack thisProj,
                       TriggerType type);

    //mainly for DAMAGE, APPLY EFFECT, CHANGE BLOCK <- non-TRIGGER
    //always happen when PROJ finish EXISTING
    void onHit(Level level,
               @Nullable Entity hitEntity,
               @Nullable Vec3 hitPos,
               Player caster,
               Vec3 normal,
               ItemStack wandStack,
               ItemStack thisProj);


    //just to cast Saved Proj
    void spawnSelfSavedProj(Level level,
                            Vec3 pos,
                            Player caster,
                            Vec3 normal,
                            ItemStack wandStack,
                            ItemStack thisProj);

}
