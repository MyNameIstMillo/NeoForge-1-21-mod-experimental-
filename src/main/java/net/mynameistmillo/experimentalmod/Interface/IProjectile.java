package net.mynameistmillo.experimentalmod.Interface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import org.checkerframework.checker.nullness.qual.Nullable;


public interface IProjectile {

    ProjStatsF getBaseStatsF();
    ProjStatsI getBaseStatsI();


    Entity spawnSpell(Level level,
                      BlockPos pos,
                      Player caster,
                      Vec3 normal,
                      ItemStack wandStack,
                      ItemStack thisSpell,
                      int index);

    void onHit(Level level,
               @Nullable Entity hitEntity,
               @Nullable BlockPos hitBlock,
               Player caster,
               Vec3 normal,
               ItemStack wandStack,
               ItemStack thisSpell);

    void onExpire(Level level,
                  BlockPos pos,
                  Player caster,
                  Vec3 normal,
                  ItemStack wandStack,
                  ItemStack thisSpell);




}
