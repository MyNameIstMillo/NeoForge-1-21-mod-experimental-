package net.mynameistmillo.experimentalmod.spells;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public interface ISpell {

    Entity spawnSpell(Level level, Player caster, ItemStack wandStack, ItemStack spell);

}
