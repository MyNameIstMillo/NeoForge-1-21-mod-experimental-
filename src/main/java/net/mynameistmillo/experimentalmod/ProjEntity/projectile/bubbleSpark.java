package net.mynameistmillo.experimentalmod.ProjEntity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ApplyStatsToProj;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyI;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class bubbleSpark extends Item implements IProjectile {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public bubbleSpark(Properties properties) {
        super(properties);
        this.baseStatsF = new ProjStatsF();
        this.baseStatsF.set(StatsKeyF.SPEED, 0.7f);
        this.baseStatsF.set(StatsKeyF.DRAG, 0.80f);
        this.baseStatsF.set(StatsKeyF.GRAVITY, 0.00f);
        this.baseStatsF.set(StatsKeyF.ACCELERATION_L_R, 0.0f);
        this.baseStatsF.set(StatsKeyF.ACCELERATION_U_D, 0.0f);
        this.baseStatsF.set(StatsKeyF.ACCELERATION_F_B, 0.0f);
        this.baseStatsF.set(StatsKeyF.VERTICAL_SPREAD, 10.0f);
        this.baseStatsF.set(StatsKeyF.HORIZONTAL_SPREAD, 90.0f);
        this.baseStatsF.set(StatsKeyF.RECOIL, 0.0f);
        this.baseStatsF.set(StatsKeyF.DISPLACEMENT_L_R, 0.0f);
        this.baseStatsF.set(StatsKeyF.DISPLACEMENT_U_D, 0.0f);
        this.baseStatsF.set(StatsKeyF.DISPLACEMENT_F_B, 0.0f);
        this.baseStatsF.set(StatsKeyF.LIFETIME, 80.0f);
        this.baseStatsF.set(StatsKeyF.DAMAGE, 1.0f);

        this.baseStatsI = new ProjStatsI();
        this.baseStatsI.set(StatsKeyI.COLOUR , 0);
        this.baseStatsI.set(StatsKeyI.EFFECT_ON_HIT , 0);
        this.baseStatsI.set(StatsKeyI.TOLERANCE , 0);
        this.baseStatsI.set(StatsKeyI.SPAGHETTI_TOLERANCE , 0);
        this.baseStatsI.set(StatsKeyI.TRIGGER_TYPE , 0);
        this.baseStatsI.set(StatsKeyI.PIERCING , 0);
        this.baseStatsI.set(StatsKeyI.TICK_EVENT , 0);
        this.baseStatsI.set(StatsKeyI.FRIENDLY_FIRE , 0);

    }

    public final ProjStatsF baseStatsF;
    public final ProjStatsI baseStatsI;

    @Override
    public ProjStatsF getBaseStatsF() {
        return baseStatsF;
    }

    @Override
    public ProjStatsI getBaseStatsI() {
        return baseStatsI;
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

        ProjStatsF stats = new ProjStatsF().loadStatsFromStack(thisSpell);
        ProjStatsI statsI = new ProjStatsI().loadStatsFromStack(thisSpell);


        ApplyStatsToProj.applyStatsToProjectile(proj, pos, normal, caster, stats, statsI);

        level.addFreshEntity(proj);

        return proj;
    }

    @Override
    public void onHit(Level level, @Nullable Entity hitEntity, @Nullable BlockPos hitBlock, Player caster, Vec3 normal, ItemStack wandStack, ItemStack thisSpell) {

        ProjStatsF stats = new ProjStatsF().loadStatsFromStack(thisSpell);


        if(hitEntity instanceof LivingEntity living && !hitEntity.level().isClientSide()){
            living.hurt(living.damageSources().generic() , stats.get(StatsKeyF.DAMAGE));
        }

    }

    @Override
    public void onExpire(Level level, BlockPos pos, Player caster, Vec3 normal, ItemStack wandStack, ItemStack thisSpell) {

    }



}
