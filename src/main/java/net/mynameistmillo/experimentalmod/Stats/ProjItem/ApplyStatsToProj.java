package net.mynameistmillo.experimentalmod.Stats.ProjItem;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.Enum.CastPosDef;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsI;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplyStatsToProj {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);


    public static void applyStatsToProjectile(
            BasicProjectileEntity e,
            Vec3 pos,
            Vec3 look,
            Player caster,
            ItemStack thisProj){

        ProjStatsF SF = ProjStatsF.loadStatsFromProj(thisProj);
        ProjStatsI SI = ProjStatsI.loadStatsFromProj(thisProj);

        double sLR = SF.get(StatsF.SHIFT_LR);
        double sUD = SF.get(StatsF.SHIFT_UD);
        double sFB = SF.get(StatsF.SHIFT_FB);

        double hSpread = SF.get(StatsF.HORIZONTAL_SPREAD);
        double vSpread = SF.get(StatsF.VERTICAL_SPREAD);

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

        // if player so plater, yes
        if((SI.get(StatsI.CAST_POS) == CastPosDef.FORCE_AT_PLAYER.getValue()
                ||  SI.get(StatsI.CAST_POS) == CastPosDef.DEFAULT.getValue())
                    && caster != null){
                Vec3 eye = caster.getEyePosition(1.0f);
            spawnPos = eye.add(0.0, -0.125, 0.0)
                    .add(lookNorn.scale(distFB))
                    .add(right.scale(sLR))
                    .add(up.scale(sUD));

        }else // not player so not player
            if (SI.get(StatsI.CAST_POS) == CastPosDef.BLOCK_POS.getValue()){
            Vec3 center = (pos==null? new Vec3(0.5f,0.5f,0.5f):pos);
            spawnPos = center.add(lookNorn.scale(sFB+0.1f))
                    .add(right.scale(sLR))
                    .add(up.scale(sUD));

        }else spawnPos = new Vec3(0,0,0);

        double yawRad = Math.toRadians((Math.random()*2-1.0)*hSpread);
        double pitchRad = Math.toRadians((Math.random()*2-1.0)*vSpread);

        Vec3 forwardYaw = rotateAroundAxis(lookNorn, up, yawRad);
        Vec3 right1 = up.cross(forwardYaw);
        if (right1.lengthSqr()==0.0){
            right1 = new Vec3(1,0,0);
        }else right1 = right1.normalize();
        Vec3 forwardFinal = rotateAroundAxis(forwardYaw, right1, pitchRad).normalize();


        float speed = SF.get(StatsF.SPEED);
        e.setDeltaMovement( forwardFinal.x * speed,
                forwardFinal.y * speed,
                forwardFinal.z * speed);

        e.setDrag(SF.get(StatsF.DRAG));
        e.setGravity(SF.get(StatsF.GRAVITY));
        e.setLifeTime(SI.get(StatsI.LIFETIME));

        e.setForceY(SF.get(StatsF.FORCE_Y));
        e.setForceX(SF.get(StatsF.FORCE_X));
        e.setForceZ(SF.get(StatsF.FORCE_Z));

        e.setResPlane(SI.get(StatsI.RES_PLANE));

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
