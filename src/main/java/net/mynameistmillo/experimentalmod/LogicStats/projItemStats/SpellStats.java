package net.mynameistmillo.experimentalmod.LogicStats.projItemStats;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import net.mynameistmillo.experimentalmod.LogicStats.Interface.IProjectile;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;

public class SpellStats implements INBTSerializable<CompoundTag> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);
    private final EnumMap<StatsKey, Float> map = new EnumMap<StatsKey, Float>(StatsKey.class);

    public SpellStats(){
        for (StatsKey k : StatsKey.values()){
            map.put(k, k.getDefaultValue());
        }
    }

    public float get(StatsKey key){
        return map.getOrDefault(key, key.getDefaultValue());
    }

    public void set(StatsKey key, float value){
        map.put(key, value);
    }

    public SpellStats copy(){
        SpellStats stats = new SpellStats();
        for (StatsKey key : StatsKey.values()) stats.set(key, this.get(key));
        return stats;
    }

    public void applyToProjectile(BasicProjectileEntity e,
                                  BlockPos pos,
                                  Vec3 look,
                                  Player caster,
                                  SpellStats stats){

        double sLR = stats.get(StatsKey.DISPLACEMENT_L_R);
        double sUD = stats.get(StatsKey.DISPLACEMENT_U_D);
        double sFB = stats.get(StatsKey.DISPLACEMENT_F_B);

        double hSpread = stats.get(StatsKey.HORIZONTAL_SPREAD);
        double vSpread = stats.get(StatsKey.VERTICAL_SPREAD);

        Vec3 lookNorn = (look == null || look.lengthSqr() == 0.0) ?
                new Vec3(0,0,1) : look.normalize();

        Vec3 worldUp = new Vec3(0,1,0);
        Vec3 right = worldUp.cross(lookNorn);

        if (right.lengthSqr()==0.0){
            right = new Vec3(1,0,0);
        }else right = right.normalize();

        Vec3 up = lookNorn.cross(right).normalize();

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

        Vec3 forwardYaw = rotateAroundAxis(lookNorn, worldUp, yawRad);
        Vec3 right1 = worldUp.cross(forwardYaw);
        if (right1.lengthSqr()==0.0){
            right1 = new Vec3(1,0,0);
        }else right1 = right1.normalize();
        Vec3 forwardFinal = rotateAroundAxis(forwardYaw, right1, pitchRad).normalize();


        float speed = stats.get(StatsKey.SPEED);
        e.setDeltaMovement( forwardFinal.x * speed,
                            forwardFinal.y * speed,
                            forwardFinal.z * speed);

        e.setDrag(stats.get(StatsKey.DRAG));
        e.setGravity(stats.get(StatsKey.GRAVITY));
        e.setLifeTime(stats.get(StatsKey.LIFETIME));
        e.setPos(spawnPos.x , spawnPos.y, spawnPos.z);

    }

    private Vec3 rotateAroundAxis(Vec3 v, Vec3 axis, double angleRad){
        Vec3 a = axis.normalize();
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);
        Vec3 term1 = v.scale(cos);
        Vec3 term2 = a.cross(v).scale(sin);
        Vec3 term3 = a.scale(a.dot(v)*(1.0-cos));
        return term1.add(term2).add(term3);
    }

    public ItemStack saveStatsToSpell(SpellStats stats, ItemStack stack){
        CompoundTag tag = new CompoundTag();
        for (StatsKey key : StatsKey.values()){
            tag.putFloat(key.name(), stats.get(key));
        }
        stack.set(ModDataComponents.SPELL_STATS.get(), tag);
        return stack;
    }

    public SpellStats loadStatsFromStack(ItemStack stack){
        CompoundTag tag = stack.getOrDefault(ModDataComponents.SPELL_STATS.get(), new CompoundTag());
        
        SpellStats stats = new SpellStats();
        for (StatsKey key : StatsKey.values()){
            if (tag.contains(key.name())) {
                stats.set(key, tag.getFloat(key.name()));
            }
            else {
                stats.set(key, key.getDefaultValue());
            }
        }
        return stats;
    }

    public ItemStack resetStats(ItemStack stack){
        if (!(stack.getItem() instanceof IProjectile iProjectile)) return null;
        SpellStats stats = iProjectile.getBaseStats().copy();

        CompoundTag tag = new CompoundTag();
        for (StatsKey key : StatsKey.values()) {
            tag.putFloat(key.name(), stats.get(key));
        }
        stack.set(ModDataComponents.SPELL_STATS.get(), tag);
        return stack;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<StatsKey, Float> e : map.entrySet()){
            tag.putFloat(e.getKey().getId(), e.getValue());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        for (StatsKey k : StatsKey.values()){
            if (nbt.contains(k.getId())){
                map.put(k, nbt.getFloat(k.getId()));
            }else {
                map.put(k, k.getDefaultValue());
            }
        }
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
