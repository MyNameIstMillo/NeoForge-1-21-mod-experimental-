package net.mynameistmillo.experimentalmod.spells.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import net.mynameistmillo.experimentalmod.items.ModItems;
import net.mynameistmillo.experimentalmod.spells.ISpell;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class boltTrigger extends Item implements ISpell {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);
    public boltTrigger(Properties properties) {
        super(properties);
    }

    @Override
    public Entity spawnSpell(Level level, BlockPos pos, Player caster, ItemStack wandStack) {
        if(level.isClientSide()) return null;

        Vec3 look = caster.getLookAngle();

        double x = caster.getX() + look.x * 1.2;
        double y = caster.getY() + 1.25 + look.y * 1.2;
        double z = caster.getZ() + look.z * 1.2;

        BasicProjectileEntity projectile = new BasicProjectileEntity(level, caster);

        projectile.setDeltaMovement(look);
        projectile.setGravity(0.01f);
        projectile.setDrag(0.950f);
        projectile.setDamage(1);
        projectile.setLifeTime(80);

        projectile.setPos(x,y,z);

        ItemStack spellStack = new ItemStack(ModItems.BOLT_TRIGGER.get());
        projectile.setSpellStack(spellStack);
        projectile.setWandStack(wandStack.copy());
        projectile.setCasterUUID(caster.getUUID());

        level.addFreshEntity(projectile);

        return projectile;
    }


    @Override
    public Explosion spawnExplosion(Level level, BlockPos pos, Player caster, ItemStack wandStack) {
        return null;
    }

    @Override
    public void onHit(Level level, @Nullable Entity hitEntity, @Nullable BlockPos hitBlock, Player caster, ItemStack wandStack) {
        if(level.isClientSide()) return;

        LOGGER.info("onHit boltTrigger -> block -> {} , entyti -> {}", hitBlock, hitEntity);

    }
}
