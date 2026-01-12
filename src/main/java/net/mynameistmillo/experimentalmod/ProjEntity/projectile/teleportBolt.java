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
import net.mynameistmillo.experimentalmod.Stats.DrawItem.DrawStats;
import net.mynameistmillo.experimentalmod.Stats.DrawItem.ModOrProjType;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ApplyStatsToProj;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyI;
import net.mynameistmillo.experimentalmod.WandLogic.Types.CasterOrBlockPosType;
import net.mynameistmillo.experimentalmod.WandLogic.Types.DrawOrTriggerType;
import net.mynameistmillo.experimentalmod.WandLogic.Types.TriggerType;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class teleportBolt extends Item implements IProjectile {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public teleportBolt(Properties properties) {
        super(properties);
        this.baseStatsF = new ProjStatsF();
        this.baseStatsF.set(StatsKeyF.SPEED, 0.4f);
        this.baseStatsF.set(StatsKeyF.DRAG, 0.999f);
        this.baseStatsF.set(StatsKeyF.GRAVITY, 0.025f);
        this.baseStatsF.set(StatsKeyF.ACCELERATION_L_R, 0.0f);
        this.baseStatsF.set(StatsKeyF.ACCELERATION_U_D, 0.0f);
        this.baseStatsF.set(StatsKeyF.ACCELERATION_F_B, 0.0f);
        this.baseStatsF.set(StatsKeyF.VERTICAL_SPREAD, 0.0f);
        this.baseStatsF.set(StatsKeyF.HORIZONTAL_SPREAD, 0.0f);
        this.baseStatsF.set(StatsKeyF.RECOIL, 0.0f);
        this.baseStatsF.set(StatsKeyF.DISPLACEMENT_L_R, 0.0f);
        this.baseStatsF.set(StatsKeyF.DISPLACEMENT_U_D, 0.0f);
        this.baseStatsF.set(StatsKeyF.DISPLACEMENT_F_B, -0.4f);
        this.baseStatsF.set(StatsKeyF.LIFETIME, 50.0f);
        this.baseStatsF.set(StatsKeyF.DAMAGE, 1.0f);

        this.baseStatsI = new ProjStatsI();
        this.baseStatsI.set(StatsKeyI.COLOUR , 10);
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
        return this.baseStatsF;
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
        if(level.isClientSide()) return null;
        if (!(thisProj.getItem() instanceof IProjectile )) return null;
        //create projectile
        BasicProjectileEntity proj = new BasicProjectileEntity(level, caster, 0.25f, 0.25f);

        //set texture for projectile
        String name = "teleport_bolt";
        proj.setProjName(name);


        //connect spellItem to the projectile
        proj.setProjStack(thisProj.copy());
        proj.setWandStack(wandStack.copy());
        proj.setCasterUUID(caster.getUUID());

        ProjStatsF statsF = ProjStatsF.loadStatsFromStack(thisProj);
        ProjStatsI statsI = ProjStatsI.loadStatsFromProj(thisProj);

        //apply stats
        ApplyStatsToProj.applyStatsToProjectile(proj, pos, normal, caster, statsF, statsI, COP);
        //add projectile to the world
        level.addFreshEntity(proj);

        return proj;
    }

    @Override
    public void triggerAction(Level level,
                              @Nullable Entity hitEntity,
                              @Nullable BlockPos hitBlock,
                              Player caster, Vec3 normal,
                              ItemStack wandStack, ItemStack thisSpell,
                              TriggerType type) {

        ProjStatsF statsF = ProjStatsF.loadStatsFromStack(thisSpell);
        ProjStatsI statsI = ProjStatsI.loadStatsFromProj(thisSpell);

        int v = type.getId()+statsI.get(StatsKeyI.TRIGGER_TYPE);

        if (v==2 || v==20 || v==60) {
            spawnSelfSavedProj(level, hitBlock, caster, normal, wandStack, thisSpell, statsF, statsI);
        }

    }

    @Override
    public void onHit(Level level,
                      @Nullable Entity hitEntity,
                      @Nullable BlockPos hitBlock,
                      Player caster, Vec3 normal,
                      ItemStack wandStack, ItemStack thisProj) {
        if(level.isClientSide()) return;
        //LOGGER.info("onHit boltTrigger -> block -> {} , entyti -> {} , normal -> {}", hitBlock, hitEntity, normal);
        //LOGGER.info("hit!");

        ProjStatsF statsF = ProjStatsF.loadStatsFromStack(thisProj);
        ProjStatsI statsI = ProjStatsI.loadStatsFromProj(thisProj);

        if(hitBlock != null && normal != null) {
            double x = hitBlock.getX() ;
            double y = hitBlock.getY() ;
            double z = hitBlock.getZ() ;


            caster.teleportTo(x +0.5f, y, z +0.5f);
        }

        if(hitEntity instanceof LivingEntity living && !hitEntity.level().isClientSide()) {
            if (hitEntity.is(caster) && statsI.get(StatsKeyI.FRIENDLY_FIRE) == 0) return;
            living.hurt(living.damageSources().indirectMagic(thisProj.getEntityRepresentation(), caster), statsF.get(StatsKeyF.DAMAGE));

        }

    }

    @Override
    public void spawnSelfSavedProj(Level level,
                                   BlockPos pos, Player caster, Vec3 normal,
                                   ItemStack wandStack, ItemStack thisProj,
                                   ProjStatsF statsF, ProjStatsI statsI) {
        List<ItemStack> spellsToSpawn = DrawStats.loadModOrProjFormDrawOrTriggerTypeType(level, thisProj, ModOrProjType.PROJ, DrawOrTriggerType.TRIGGER);

        for(ItemStack stack : spellsToSpawn){
            if (stack.getItem() instanceof IProjectile proj){
                proj.spawnProj(level, pos, caster, normal, wandStack, thisProj, CasterOrBlockPosType.BLOCK_POS);
            }
        }

    }

}
