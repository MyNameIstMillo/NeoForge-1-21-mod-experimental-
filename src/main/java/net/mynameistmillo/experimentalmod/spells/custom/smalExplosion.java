package net.mynameistmillo.experimentalmod.spells.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.mynameistmillo.experimentalmod.spells.ISpell;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class smalExplosion extends Item implements ISpell {
    public smalExplosion(Properties properties) {
        super(properties);
    }

    @Override
    public Entity spawnSpell(Level level, BlockPos pos, Player caster, ItemStack wandStack, List<ItemStack> spellList) {
        return null;
    }

    @Override
    public Explosion spawnExplosion(Level level, BlockPos pos, Player player, ItemStack wandStack, ItemStack spell) {
        if(level.isClientSide()) return null;



        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        return level.explode(
                null,
                x, y, z,
                0.7f,
                false,
                Level.ExplosionInteraction.TNT
        );
    }

    @Override
    public void onHit(Level level, @Nullable Entity hitEntity, @Nullable BlockPos hitBlock, Player caster, ItemStack wandStack, List<ItemStack> spellList) {

    }


}
