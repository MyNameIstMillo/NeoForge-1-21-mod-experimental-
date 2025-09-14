package net.mynameistmillo.experimentalmod.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.units.qual.N;

import java.util.List;


public interface ISpell {

    Entity spawnSpell(Level level, BlockPos pos, Player caster, ItemStack wandStack, List<ItemStack> spellList);

    Explosion spawnExplosion(Level level, BlockPos pos, Player caster, ItemStack wandStack, ItemStack spell);

    void onHit(Level level, @Nullable Entity hitEntity, @Nullable BlockPos hitBlock, Player caster, ItemStack wandStack, List<ItemStack> spellList);

}
