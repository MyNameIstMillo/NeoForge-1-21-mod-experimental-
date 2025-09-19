package net.mynameistmillo.experimentalmod.spells.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.data.ModTags;
import net.mynameistmillo.experimentalmod.spells.ISpell;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class snowBolt extends Item implements ISpell {
    public snowBolt(Properties properties) {
        super(properties);
    }

    @Override
    public Entity spawnSpell(Level level, BlockPos pos, Player caster, ItemStack wandStack) {
        if(level.isClientSide()) return null;

        Vec3 look = caster.getLookAngle();

        //Snowball snowball = new Snowball(level, look.x, look.y, look.z);

        Snowball snowball = new Snowball(level, caster);

        snowball.setDeltaMovement(look);

        snowball.setPos(caster.getX()+look.x*1.2,caster.getEyeY()+look.y*1.2, caster.getZ()+look.z*1.2);

        level.addFreshEntity(snowball);

        return snowball;
    }

    @Override
    public Explosion spawnExplosion(Level level, BlockPos pos, Player player, ItemStack wandStack) {
        return null;
    }

    @Override
    public void onHit(Level level, @Nullable Entity hitEntity, @Nullable BlockPos hitBlock, Player caster, Vec3 normal, ItemStack wandStack) {

    }

    @Override
    public void onExpire(Level level, BlockPos pos, Player caster, Vec3 normal,  ItemStack wandStack) {

    }
}
