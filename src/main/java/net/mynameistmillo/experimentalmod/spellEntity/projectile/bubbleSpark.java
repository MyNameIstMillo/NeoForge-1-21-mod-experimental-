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
import net.mynameistmillo.experimentalmod.LogicStats.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.LogicStats.projItemStats.SpellStats;
import net.mynameistmillo.experimentalmod.LogicStats.projItemStats.StatsKey;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class bubbleSpark extends Item implements IProjectile {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public bubbleSpark(Properties properties) {
        super(properties);
        this.baseStats = new SpellStats();
        this.baseStats.set(StatsKey.SPEED, 0.7f);
        this.baseStats.set(StatsKey.DRAG, 0.80f);
        this.baseStats.set(StatsKey.GRAVITY, 0.00f);
        this.baseStats.set(StatsKey.ACCELERATION_L_R, 0.0f);
        this.baseStats.set(StatsKey.ACCELERATION_U_D, 0.0f);
        this.baseStats.set(StatsKey.ACCELERATION_F_B, 0.0f);
        this.baseStats.set(StatsKey.VERTICAL_SPREAD, 0.0f);
        this.baseStats.set(StatsKey.HORIZONTAL_SPREAD, 0.0f);
        this.baseStats.set(StatsKey.RECOIL, 0.0f);
        this.baseStats.set(StatsKey.DISPLACEMENT_L_R, 0.0f);
        this.baseStats.set(StatsKey.DISPLACEMENT_U_D, 0.0f);
        this.baseStats.set(StatsKey.DISPLACEMENT_F_B, 0.0f);
        this.baseStats.set(StatsKey.COLOUR, 6.0f); // blue
        this.baseStats.set(StatsKey.EFFECT_ON_HIT, 0.0f);
        this.baseStats.set(StatsKey.TOLERANCE, 100.0f);
        this.baseStats.set(StatsKey.SPAGHETTI_TOLERANCE, 0.0f);
        this.baseStats.set(StatsKey.TRIGGER_TYPE, 0.0f);
        this.baseStats.set(StatsKey.PIERCING, 0.0f);
        this.baseStats.set(StatsKey.TICK_EVENT, 30.0f);
        this.baseStats.set(StatsKey.FRIENDLY_FIRE, 0.0f);
        this.baseStats.set(StatsKey.LIFETIME, 80.0f);
        this.baseStats.set(StatsKey.DAMAGE, 1.0f);

    }

    public final SpellStats baseStats;
    @Override
    public SpellStats getBaseStats() {
        return baseStats;
    }

    @Override
    public Entity spawnSpell(Level level, BlockPos pos, Player caster, Vec3 normal, ItemStack wandStack, ItemStack thisSpell, int index) {
        if (level.isClientSide()) return null;

        BasicProjectileEntity proj = new BasicProjectileEntity(level, caster, 0.25f, 0.25f);

        String name = "bubble_spark";
        proj.setProjName(name);

        proj.setSpellStack(thisSpell.copy());
        proj.setWandStack(wandStack.copy());
        proj.setCasterUUID(caster.getUUID());

        SpellStats stats = new SpellStats().loadStatsFromStack(thisSpell);


        this.baseStats.applyToProjectile(proj, pos, normal, caster, stats);

        level.addFreshEntity(proj);

        return proj;
    }

    @Override
    public void onHit(Level level, @Nullable Entity hitEntity, @Nullable BlockPos hitBlock, Player caster, Vec3 normal, ItemStack wandStack, ItemStack thisSpell) {

        SpellStats stats = new SpellStats();
        stats = stats.loadStatsFromStack(thisSpell);


        if(hitEntity instanceof LivingEntity living && !hitEntity.level().isClientSide()){
            living.hurt(living.damageSources().generic() , stats.get(StatsKey.DAMAGE));
        }

    }

    @Override
    public void onExpire(Level level, BlockPos pos, Player caster, Vec3 normal, ItemStack wandStack, ItemStack thisSpell) {

    }



}
