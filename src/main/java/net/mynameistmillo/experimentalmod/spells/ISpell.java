package net.mynameistmillo.experimentalmod.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.spells.stats.SpellStats;
import org.checkerframework.checker.nullness.qual.Nullable;


public interface ISpell {

    SpellStats getBaseStats();

    Entity spawnSpell(Level level,
                      BlockPos pos,
                      Player caster,
                      Vec3 normal,
                      ItemStack wandStack);

    void onHit(Level level,
               @Nullable Entity hitEntity,
               @Nullable BlockPos hitBlock,
               Player caster,
               Vec3 normal,
               ItemStack wandStack);

    void onExpire(Level level,
                  BlockPos pos,
                  Player caster,
                  Vec3 normal,
                  ItemStack wandStack);

    Explosion createExplosion(Level level,
                              BlockPos pos,
                              Player caster,
                              ItemStack wandStack);

    boolean spawnNextOnHit();
    boolean spawnNextOnExpire();

}
