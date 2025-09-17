package net.mynameistmillo.experimentalmod.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.Nullable;


public interface ISpell {

    Entity spawnSpell(Level level,
                      BlockPos pos,
                      Player caster,
                      ItemStack wandStack);

    Explosion spawnExplosion(Level level,
                             BlockPos pos,
                             Player caster,
                             ItemStack wandStack);

    void onHit(Level level,
               @Nullable Entity hitEntity,
               @Nullable BlockPos hitBlock,
               Player caster,
               ItemStack wandStack);

    void onExpire(Level level,
                  BlockPos pos,
                  Player caster,
                  ItemStack wandStack);

}
