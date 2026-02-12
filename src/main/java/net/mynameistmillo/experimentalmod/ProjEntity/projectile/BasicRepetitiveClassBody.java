package net.mynameistmillo.experimentalmod.ProjEntity.projectile;

import com.mojang.blaze3d.shaders.Effect;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.Enum.CasterOrBlockPosType;
import net.mynameistmillo.experimentalmod.Enum.TriggerType;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ApplyStatsToProj;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyI;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.GetStackFromStack;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class BasicRepetitiveClassBody {

    public static Entity spawnProjBody(Level level,
                                       BlockPos pos, Player caster, Vec3 normal,
                                       ItemStack wandStack, ItemStack thisProj,
                                       CasterOrBlockPosType COP, String name,
                                       float width, float height){
        if(level.isClientSide()) return null;
        if (!(thisProj.getItem() instanceof IProjectile)) return null;

        BasicProjectileEntity p = new BasicProjectileEntity(level, caster, width, height);
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


    public static boolean triggerActionBody(ItemStack thisProj, TriggerType t){
        ProjStatsI i = ProjStatsI.loadStatsFromProj(thisProj);
        return t.getId() == i.get(StatsKeyI.TRIGGER_TYPE);
    }


    public static void onHitBody(Level level,
                                 @Nullable Entity hitEntity,
                                 @Nullable BlockPos hitBlock,
                                 Player caster, Vec3 normal,
                                 ItemStack wandStack, ItemStack thisProj){
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


    public static void spawnSelfSavedProjBody(Level level, BlockPos pos, Player caster, Vec3 normal,
                                              ItemStack wandStack, ItemStack thisProj){
        List<ItemStack> projToSpawn = GetStackFromStack.projFromTrigger(level, thisProj);

        for(ItemStack stack : projToSpawn){
            if (stack.getItem() instanceof IProjectile proj){
                proj.spawnProj(level, pos, caster, (normal==null? new Vec3(0.0,1.0,0.0) : normal.reverse()), wandStack, thisProj, CasterOrBlockPosType.BLOCK_POS);
            }
        }
    }




}
