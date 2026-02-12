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
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.ProjEntity.projectile.BasicRepetitiveClassBody;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ApplyStatsToProj;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyI;
import net.mynameistmillo.experimentalmod.Enum.CasterOrBlockPosType;
import net.mynameistmillo.experimentalmod.Enum.TriggerType;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.GetStackFromStack;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class sparkBoltTrigger extends Item implements IProjectile {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public sparkBoltTrigger(Properties properties) {
        super(properties);
        this.baseStatsF = new ProjStatsF();
        this.baseStatsF.set(StatsKeyF.SPEED, 0.9f);
        this.baseStatsF.set(StatsKeyF.DRAG, 1.0f);
        this.baseStatsF.set(StatsKeyF.GRAVITY, 0.03f);
        this.baseStatsF.set(StatsKeyF.ACCELERATION_L_R, 0.0f);
        this.baseStatsF.set(StatsKeyF.ACCELERATION_U_D, 0.0f);
        this.baseStatsF.set(StatsKeyF.ACCELERATION_F_B, 0.0f);
        this.baseStatsF.set(StatsKeyF.VERTICAL_SPREAD, 0.0f);
        this.baseStatsF.set(StatsKeyF.HORIZONTAL_SPREAD, 50.0f);
        this.baseStatsF.set(StatsKeyF.RECOIL, 0.0f);
        this.baseStatsF.set(StatsKeyF.DISPLACEMENT_L_R, 0.0f);
        this.baseStatsF.set(StatsKeyF.DISPLACEMENT_U_D, 0.0f);
        this.baseStatsF.set(StatsKeyF.DISPLACEMENT_F_B, 0.0f);
        this.baseStatsF.set(StatsKeyF.LIFETIME, 60);
        this.baseStatsF.set(StatsKeyF.DAMAGE, 20.0f);

        this.baseStatsI = new ProjStatsI();
        this.baseStatsI.set(StatsKeyI.COLOUR , 0);
        this.baseStatsI.set(StatsKeyI.EFFECT_ON_HIT , 0);
        this.baseStatsI.set(StatsKeyI.TOLERANCE , 0);
        this.baseStatsI.set(StatsKeyI.SPAGHETTI_TOLERANCE , 0);
        this.baseStatsI.set(StatsKeyI.TRIGGER_TYPE , 1);
        this.baseStatsI.set(StatsKeyI.PIERCING , 0);
        this.baseStatsI.set(StatsKeyI.TICK_EVENT , 0);
        this.baseStatsI.set(StatsKeyI.FRIENDLY_FIRE , 0);
        this.baseStatsI.set(StatsKeyI.FREE_DRAW_TRIGGER , 1);

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

        String name = "spark_bolt";

        return BasicRepetitiveClassBody.spawnProjBody(
                level, pos, caster, normal,
                wandStack, thisProj, COP,
                name, 0.25f, 0.25f);
    }

    @Override
    public void triggerAction(Level level,
                              @Nullable Entity hitEntity,
                              @Nullable BlockPos hitBlock,
                              Player caster, Vec3 normal,
                              ItemStack wandStack, ItemStack thisProj,
                              TriggerType type) {

        if (BasicRepetitiveClassBody.triggerActionBody(thisProj, type)){
            spawnSelfSavedProj(level, hitBlock, caster, normal, wandStack, thisProj);
        }

    }

    @Override
    public void onHit(Level level,
                      @Nullable Entity hitEntity,
                      @Nullable BlockPos hitBlock,
                      Player caster, Vec3 normal,
                      ItemStack wandStack, ItemStack thisProj) {
        BasicRepetitiveClassBody.onHitBody(level, hitEntity, hitBlock, caster, normal, wandStack, thisProj);
    }

    @Override
    public void spawnSelfSavedProj(Level level,
                                   BlockPos pos,
                                   Player caster,
                                   Vec3 normal,
                                   ItemStack wandStack,
                                   ItemStack thisProj) {
        BasicRepetitiveClassBody.spawnSelfSavedProjBody(level, pos, caster, normal, wandStack, thisProj);
    }
}