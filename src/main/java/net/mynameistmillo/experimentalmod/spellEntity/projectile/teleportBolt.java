package net.mynameistmillo.experimentalmod.spellEntity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import net.mynameistmillo.experimentalmod.spellLogic.IProjectile;
import net.mynameistmillo.experimentalmod.spellLogic.stats.SpellStats;
import net.mynameistmillo.experimentalmod.spellLogic.stats.StatsKey;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class teleportBolt extends Item implements IProjectile {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public final SpellStats baseStats;

    public teleportBolt(Properties properties) {
        super(properties);
        this.baseStats = new SpellStats();
        this.baseStats.set(StatsKey.GRAVITY, 0.025f);
        this.baseStats.set(StatsKey.DRAG, 0.999f);
        this.baseStats.set(StatsKey.SPEED, 0.4f);
        this.baseStats.set(StatsKey.LIFETIME, 50);
        this.baseStats.set(StatsKey.DAMAGE, 0.0f);
        this.baseStats.set(StatsKey.DISPLACEMENT, -0.4f);
    }

    @Override
    public SpellStats getBaseStats() {
        return this.baseStats;
    }

    @Override
    public Entity spawnSpell(Level level, BlockPos pos, Player caster, Vec3 normal, ItemStack wandStack, ItemStack thisSpell, int index) {
        if(level.isClientSide()) return null;
        if (!(thisSpell.getItem() instanceof IProjectile iProjectile)) return null;
        //create projectile
        BasicProjectileEntity proj = new BasicProjectileEntity(level, caster, 0.25f, 0.25f);

        //set texture for projectile
        String name = "teleport_bolt";
        proj.setProjName(name);


        //connect spellItem to the projectile
        proj.setSpellStack(thisSpell.copy());
        proj.setWandStack(wandStack.copy());
        proj.setCasterUUID(caster.getUUID());

        SpellStats stats = new SpellStats();
        stats = stats.loadStatsFromStack(thisSpell);


        //apply stats
        this.baseStats.applyToProjectile(proj, pos, normal, caster, stats);
        //add projectile to the world
        level.addFreshEntity(proj);

        return proj;
    }

    @Override
    public void onHit(Level level, @Nullable Entity hitEntity, @Nullable BlockPos hitBlock, Player caster, Vec3 normal, ItemStack wandStack, ItemStack thisSpell) {
        if (level.isClientSide()) return;
        SpellStats stats = new SpellStats();
        stats = stats.loadStatsFromStack(thisSpell);

        if(hitEntity instanceof LivingEntity living && !hitEntity.level().isClientSide()){
            living.hurt(living.damageSources().generic() , stats.get(StatsKey.DAMAGE));

        }
    }

    @Override
    public void onExpire(Level level, BlockPos pos, Player caster, Vec3 normal, ItemStack wandStack, ItemStack thisSpell) {
        if (level.isClientSide()) return;
        if(pos != null && normal != null) {
            double x = pos.getX() ;
            double y = pos.getY() ;
            double z = pos.getZ() ;


            caster.teleportTo(x +0.5f, y, z +0.5f);
        }

    }
}
