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
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import net.mynameistmillo.experimentalmod.items.custom.WandItem;
import net.mynameistmillo.experimentalmod.spells.ISpell;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class boltTriger extends Item implements ISpell {
    public boltTriger(Properties properties) {
        super(properties);
    }

    @Override
    public Entity spawnSpell(Level level, BlockPos pos, Player caster, ItemStack wandStack, List<ItemStack> spellList) {
        if(level.isClientSide()) return null;

        Vec3 look = caster.getLookAngle();

        double x = caster.getX() + look.x * 1.2;
        double y = caster.getEyeY() + look.y * 1.2;
        double z = caster.getZ() + look.z * 1.2;

        BasicProjectileEntity projectile = new BasicProjectileEntity(level, caster);

        projectile.setDeltaMovement(look);
        //projectile.setGravity(5);
        projectile.setDrag(0.5f);
        projectile.setDamage(1);
        projectile.setLifeTime(80);

        projectile.setPos(x,y,z);

        level.addFreshEntity(projectile);

        return projectile;
    }


    @Override
    public Explosion spawnExplosion(Level level, BlockPos pos, Player caster, ItemStack wandStack, ItemStack spell) {
        return null;
    }

    @Override
    public void onHit(Level level, @Nullable Entity hitEntity, @Nullable BlockPos hitBlock, Player caster, ItemStack wandStack, List<ItemStack> spellList) {
        if(wandStack.getItem() instanceof WandItem wandItem){
            int capacity = wandItem.getCapacity(wandStack);
            int index = wandItem.getCurrentIndex(wandStack);
            index++;
            if(index <= capacity) {

                BlockPos pos;
                if(hitEntity !=null){
                    pos = hitEntity.blockPosition();
                } else
                    if(hitBlock != null){
                    pos = hitBlock;
                    } else return;



                ItemStack nextSpell = spellList.get(index);

                Entity entity = spawnSpell(level, pos, caster, wandStack, spellList);
                wandItem.setCurrentIndex(wandStack, index);



            }
        }
    }
}
