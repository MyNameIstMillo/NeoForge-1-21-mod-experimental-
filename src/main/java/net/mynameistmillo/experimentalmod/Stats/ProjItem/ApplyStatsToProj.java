package net.mynameistmillo.experimentalmod.Stats.ProjItem;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplyStatsToProj {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public static void applyStatsToProjectile(
            BasicProjectileEntity e,
            BlockPos pos,
            Vec3 look,
            Player caster,
            ProjStatsF stats,
            ProjStatsI statsI){

        double sLR = stats.get(StatsKeyF.DISPLACEMENT_L_R);
        double sUD = stats.get(net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF.DISPLACEMENT_U_D);
        double sFB = stats.get(net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF.DISPLACEMENT_F_B);

        double hSpread = stats.get(net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF.HORIZONTAL_SPREAD);
        double vSpread = stats.get(net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF.VERTICAL_SPREAD);

        Vec3 lookNorn = (look == null || look.lengthSqr() == 0.0) ?
                new Vec3(0,0,1) : look.normalize();

        Vec3 worldUp = new Vec3(0,1,0);
        Vec3 right = lookNorn.cross(worldUp);

        if (right.lengthSqr()==0.0){
            right = new Vec3(1,0,0);
        }else right = right.normalize();

        Vec3 up = right.cross(lookNorn).normalize();

        double baseOffset = 1.0;
        double distFB = baseOffset + sFB;
        Vec3 spawnPos;

        if(caster != null){// if player so plater, yes
            Vec3 eye = caster.getEyePosition(1.0f);
            spawnPos = eye.add(0.0, -0.125, 0.0)
                    .add(lookNorn.scale(distFB))
                    .add(right.scale(sLR))
                    .add(up.scale(sUD));

        }else{ // not player so not player
            Vec3 center = Vec3.atCenterOf(pos);
            spawnPos = center.add(lookNorn.scale(distFB))
                    .add(right.scale(sLR))
                    .add(up.scale(sUD));
        }

        double yawRad = Math.toRadians((Math.random()*2-1.0)*hSpread);
        double pitchRad = Math.toRadians((Math.random()*2-1.0)*vSpread);

        Vec3 forwardYaw = rotateAroundAxis(lookNorn, up, yawRad);
        Vec3 right1 = up.cross(forwardYaw);
        if (right1.lengthSqr()==0.0){
            right1 = new Vec3(1,0,0);
        }else right1 = right1.normalize();
        Vec3 forwardFinal = rotateAroundAxis(forwardYaw, right1, pitchRad).normalize();


        float speed = stats.get(net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF.SPEED);
        e.setDeltaMovement( forwardFinal.x * speed,
                forwardFinal.y * speed,
                forwardFinal.z * speed);

        e.setDrag(stats.get(net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF.DRAG));
        e.setGravity(stats.get(net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF.GRAVITY));
        e.setLifeTime(stats.get(net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsKeyF.LIFETIME));
        e.setPos(spawnPos.x , spawnPos.y, spawnPos.z);
    }

    private static Vec3 rotateAroundAxis(Vec3 v, Vec3 axis, double angleRad){
        Vec3 a = axis.normalize();
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);
        Vec3 term1 = v.scale(cos);
        Vec3 term2 = a.cross(v).scale(sin);
        Vec3 term3 = a.scale(a.dot(v)*(1.0-cos));
        return term1.add(term2).add(term3);
    }

}
