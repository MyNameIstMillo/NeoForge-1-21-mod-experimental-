package net.mynameistmillo.experimentalmod.ProjEntity.projectile.standard;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.ProjEntity.projectile.ProjHelper.Helper;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ApplyStatsToProj;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsI;
import net.mynameistmillo.experimentalmod.Enum.TriggerType;
import net.mynameistmillo.experimentalmod.UsefullFunction.TeleportInSomeWay;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.GetStackFromStack;
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
        this.baseStatsF.set(StatsF.SPEED, 0.4f);
        this.baseStatsF.set(StatsF.DRAG, 0.999f);
        this.baseStatsF.set(StatsF.GRAVITY, 0.025f);

        this.baseStatsF.set(StatsF.FORCE_Y, 0.0f);
        this.baseStatsF.set(StatsF.FORCE_X, 0.0f);
        this.baseStatsF.set(StatsF.FORCE_Z, 0.0f);

        this.baseStatsF.set(StatsF.VERTICAL_SPREAD, 0.0f);
        this.baseStatsF.set(StatsF.HORIZONTAL_SPREAD, 0.0f);

        this.baseStatsF.set(StatsF.SHIFT_LR, 0.0f);
        this.baseStatsF.set(StatsF.SHIFT_UD, 0.0f);
        this.baseStatsF.set(StatsF.SHIFT_FB, -0.4f);

        this.baseStatsF.set(StatsF.NORMAL_DAMAGE, 1.0f);

        this.baseStatsI = new ProjStatsI();
        this.baseStatsI.set(StatsI.LIFETIME, 50);
        this.baseStatsI.set(StatsI.TRIGGER_TYPE , 0);
        this.baseStatsI.set(StatsI.FRIENDLY_FIRE , 0);
        this.baseStatsI.set(StatsI.DRAW_TRIGGER, 0);
        this.baseStatsI.set(StatsI.CAST_POS, 0);
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
                            Vec3 pos, Player caster, Vec3 normal,
                            ItemStack wandStack, ItemStack thisProj) {

        Entity e = Helper.spawnProjBasic(level, pos, caster, normal, wandStack, thisProj,
                "teleport_bolt", 0.25f, 0.25f);
        assert e != null;
        level.addFreshEntity(e);
        return e;
    }

    @Override
    public void triggerAction(Level level,
                              @Nullable Entity hitEntity,
                              @Nullable Vec3 hitPos,
                              Player caster, Vec3 normal,
                              ItemStack wandStack, ItemStack thisSpell,
                              TriggerType type) {

        ProjStatsI statsI = ProjStatsI.loadStatsFromProj(thisSpell);

        if (type.getId() == statsI.get(StatsI.TRIGGER_TYPE)){
            spawnSelfSavedProj(level, hitPos, caster, normal, wandStack, thisSpell);
        }

    }

    @Override
    public void onHit(Level level,
                      @Nullable Entity hitEntity,
                      @Nullable Vec3 hitPos,
                      Player caster, Vec3 normal,
                      ItemStack wandStack, ItemStack thisProj) {
        if(level.isClientSide()) return;

        Helper.onHit(level, hitEntity, hitPos, caster, normal, wandStack, thisProj);

        assert hitPos != null;
        TeleportInSomeWay.checkAndResetFallSpeed(level, BlockPos.containing(hitPos), normal, caster);


    }

    @Override
    public void spawnSelfSavedProj(Level level,
                                   Vec3 pos, Player caster, Vec3 normal,
                                   ItemStack wandStack, ItemStack thisProj) {

        Helper.spawnSelfSavedProj(level, pos, caster, normal, wandStack, thisProj);

    }

}
