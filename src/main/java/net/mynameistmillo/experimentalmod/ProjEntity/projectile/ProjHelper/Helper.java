package net.mynameistmillo.experimentalmod.ProjEntity.projectile.ProjHelper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ApplyStatsToProj;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.ProjStats.ProjStatsI;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsF;
import net.mynameistmillo.experimentalmod.Stats.ProjItem.StatsKey.StatsI;
import net.mynameistmillo.experimentalmod.WandLogic.SaveGet.stack.GetStackFromStack;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class Helper {

    public static Entity spawnProjBasic(Level level,
                                        Vec3 pos, Player caster, Vec3 normal,
                                        ItemStack wand, ItemStack thisProj,
                                        String txtName, float width, float height){

        if (level.isClientSide() || !(thisProj.getItem() instanceof IProjectile)) return null;

        BasicProjectileEntity p = new BasicProjectileEntity(level, width, height);
        p.setProjName(txtName);

        p.setProjStack(thisProj.copy());
        p.setWandStack(wand.copy());
        p.setCasterUUID(caster.getUUID());

        ApplyStatsToProj.applyStatsToProjectile(p, pos, normal, caster, thisProj);

        return p;
    }


    public static void onHit(Level level,
                             @Nullable Entity hitEntity,
                             @Nullable Vec3 hitPos,
                             Player caster, Vec3 normal,
                             ItemStack wandStack, ItemStack thisProj){
        if (level.isClientSide()) return;

        ProjStatsF statsF = ProjStatsF.loadStatsFromProj(thisProj);
        ProjStatsI statsI = ProjStatsI.loadStatsFromProj(thisProj);

        if(hitEntity instanceof LivingEntity living && !hitEntity.level().isClientSide()) {
            if (hitEntity.is(caster) && statsI.get(StatsI.FRIENDLY_FIRE) == 0) return;
            assert thisProj.getEntityRepresentation() != null;

            living.hurt(living.damageSources().indirectMagic(thisProj.getEntityRepresentation(),
                    caster), statsF.get(StatsF.NORMAL_DAMAGE));
        }
    }


    public static void spawnSelfSavedProj(Level level,
                                          Vec3 pos, Player caster, Vec3 normal,
                                          ItemStack wandStack, ItemStack thisProj){
        List<ItemStack> spellsToSpawn = GetStackFromStack.projFromTrigger(level, thisProj);

        for(ItemStack stack : spellsToSpawn){
            if (stack.getItem() instanceof IProjectile proj){
                proj.spawnProj(level, pos, caster, (normal==null? new Vec3(0.0,1.0,0.0) : normal.reverse()), wandStack, stack);
            }
        }
    }






}
