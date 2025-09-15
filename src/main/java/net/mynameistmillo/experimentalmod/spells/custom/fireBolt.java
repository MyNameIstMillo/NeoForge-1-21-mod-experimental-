package net.mynameistmillo.experimentalmod.spells.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.spells.ISpell;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class fireBolt extends Item implements ISpell {
    public fireBolt(Properties properties) {
        super(properties);
    }

    @Override
    public Entity spawnSpell(Level level, BlockPos pos, Player caster, ItemStack wandStack) {
        if(level.isClientSide()) return null;

        Vec3 look = caster.getLookAngle();

        SmallFireball fireball = new SmallFireball(level, look.x, look.y, look.z, look);

        double x = caster.getX() + look.x * 1.2;
        double y = caster.getEyeY() + look.y * 1.2;
        double z = caster.getZ() + look.z * 1.2;

        fireball.setPos(x,y,z);
        level.addFreshEntity(fireball);

        return fireball;
    }

    @Override
    public Explosion spawnExplosion(Level level, BlockPos pos, Player player, ItemStack wandStack) {
        return null;
    }

    @Override
    public void onHit(Level level, @Nullable Entity hitEntity, @Nullable BlockPos hitBlock, Player caster, ItemStack wandStack) {

    }

    @Override
    public void onExpire(Level level, BlockPos pos, Player caster, ItemStack wandStack) {

    }
}
