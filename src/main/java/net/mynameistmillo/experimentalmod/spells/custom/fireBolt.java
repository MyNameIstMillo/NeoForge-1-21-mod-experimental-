package net.mynameistmillo.experimentalmod.spells.custom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.spells.ISpell;

public class fireBolt extends Item implements ISpell {
    public fireBolt(Properties properties) {
        super(properties);
    }

    @Override
    public Entity spawnSpell(Level level, Player caster, ItemStack wandStack, ItemStack spell) {
        if(level.isClientSide) return null;

        Vec3 look = caster.getLookAngle();

        SmallFireball fireball = new SmallFireball(level, look.x, look.y, look.z, look);

        fireball.setPos(caster.getX()+look.x*1.2,caster.getEyeY()+look.y*1.2, caster.getZ()+look.z*1.2);
        level.addFreshEntity(fireball);

        return fireball;
    }
}
