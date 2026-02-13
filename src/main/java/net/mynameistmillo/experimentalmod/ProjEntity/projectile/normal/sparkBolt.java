package net.mynameistmillo.experimentalmod.ProjEntity.projectile.normal;

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
import net.mynameistmillo.experimentalmod.Enum.CasterOrBlockPosType;
import net.mynameistmillo.experimentalmod.Enum.TriggerType;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.GetStackFromStack;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class sparkBolt extends Item implements IProjectile {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public sparkBolt(Properties properties) {
        super(properties);
        this.baseStatsF = new ProjStatsF();
        this.baseStatsF.set(StatsKeyF.SPEED, 0.9f);
        this.baseStatsF.set(StatsKeyF.DRAG, 1.0f);
        this.baseStatsF.set(StatsKeyF.GRAVITY, 0.03f);
        this.baseStatsF.set(StatsKeyF.ACCELERATION_L_R, 0.0f);
        this.baseStatsF.set(StatsKeyF.ACCELERATION_U_D, 0.0f);
        this.baseStatsF.set(StatsKeyF.ACCELERATION_F_B, 0.0f);
        this.baseStatsF.set(StatsKeyF.VERTICAL_SPREAD, 10.0f);
        this.baseStatsF.set(StatsKeyF.HORIZONTAL_SPREAD, 4.0f);
        this.baseStatsF.set(StatsKeyF.RECOIL, 0.0f);
        this.baseStatsF.set(StatsKeyF.DISPLACEMENT_L_R, 0.0f);
        this.baseStatsF.set(StatsKeyF.DISPLACEMENT_U_D, 0.0f);
        this.baseStatsF.set(StatsKeyF.DISPLACEMENT_F_B, 0.0f);
        this.baseStatsF.set(StatsKeyF.LIFETIME, 60);
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
        this.baseStatsI.set(StatsKeyI.FREE_DRAW_TRIGGER , 0);

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
    public Entity spawnProj(Level level,
                            BlockPos pos, Player caster, Vec3 normal,
                            ItemStack wandStack, ItemStack thisProj,
                            CasterOrBlockPosType COP) {


        if (level.isClientSide()) return null;
        if (!(thisProj.getItem() instanceof IProjectile)) return null;

        BasicProjectileEntity p = new BasicProjectileEntity(level, caster, 0.25f, 0.25f);
        String name = "spark_bolt";
        p.setProjName(name);

        p.setProjStack(thisProj.copy());
        p.setWandStack(wandStack.copy());
        p.setCasterUUID(caster.getUUID());

        ProjStatsF statsF = ProjStatsF.loadStatsFromProj(thisProj);
        ProjStatsI statsI = ProjStatsI.loadStatsFromProj(thisProj);

        ApplyStatsToProj.applyStatsToProjectile(p, pos, normal, caster, statsF, statsI, COP);

        level.addFreshEntity(p);

        return p;
    }

    @Override
    public void triggerAction(Level level,
                              @Nullable Entity hitEntity,
                              @Nullable BlockPos hitBlock,
                              Player caster, Vec3 normal,
                              ItemStack wandStack, ItemStack thisProj,
                              TriggerType type) {
        ProjStatsI i = ProjStatsI.loadStatsFromProj(thisProj);
        if (type.getId() == i.get(StatsKeyI.TRIGGER_TYPE)){
            spawnSelfSavedProj(level, hitBlock, caster, normal, wandStack, thisProj);
        }

    }

    @Override
    public void onHit(Level level,
                      @Nullable Entity hitEntity,
                      @Nullable BlockPos hitBlock,
                      Player caster, Vec3 normal,
                      ItemStack wandStack, ItemStack thisProj) {

        if (level.isClientSide()) return;

        ProjStatsF statsF = ProjStatsF.loadStatsFromProj(thisProj);
        ProjStatsI statsI = ProjStatsI.loadStatsFromProj(thisProj);

        if(hitEntity instanceof LivingEntity living && !hitEntity.level().isClientSide()) {
            if (hitEntity.is(caster) && statsI.get(StatsKeyI.FRIENDLY_FIRE) == 0) return;
            assert thisProj.getEntityRepresentation() != null;
            living.hurt(living.damageSources().indirectMagic(thisProj.getEntityRepresentation(),
                    caster), statsF.get(StatsKeyF.DAMAGE));
        }
    }


    @Override
    public void spawnSelfSavedProj(Level level, BlockPos pos, Player caster, Vec3 normal,
                                   ItemStack wandStack, ItemStack thisProj) {
        List<ItemStack> spellsToSpawn = GetStackFromStack.projFromTrigger(level, thisProj);

        for(ItemStack stack : spellsToSpawn){
            if (stack.getItem() instanceof IProjectile proj){
                proj.spawnProj(level, pos, caster, (normal==null? new Vec3(0.0,1.0,0.0) : normal.reverse()), wandStack, stack, CasterOrBlockPosType.BLOCK_POS);
            }
        }
    }
}