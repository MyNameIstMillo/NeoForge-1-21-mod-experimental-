package net.mynameistmillo.experimentalmod.spellLogic.stats;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.data.ModDataComponents;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import net.mynameistmillo.experimentalmod.spellLogic.IProjectile;
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
        double shift = stats.get(StatsKey.DISPLACEMENT);
        Vec3 lookNorn = (look == null || look.lengthSqr() == 0.0) ?
                new Vec3(0,0,1) : look.normalize();
        double baseOffset = 1.0;
        double distance = baseOffset + shift;
        Vec3 spawnPos;
        if(caster != null){// if player so plater, yes
            Vec3 eye = caster.getEyePosition(1.0f);
            spawnPos = new Vec3(
                    eye.x + lookNorn.x * distance,
                    eye.y - 0.125 + lookNorn.y * distance,
                    eye.z + lookNorn.z * distance);
        }else{ // not player so not player
            Vec3 center = Vec3.atCenterOf(pos);
            spawnPos = center.add(lookNorn.scale(distance));
        }
        float speed = stats.get(StatsKey.SPEED);
        e.setDeltaMovement( lookNorn.x * speed,
                            lookNorn.y * speed,
                            lookNorn.z * speed);

        e.setGravity(stats.get(StatsKey.GRAVITY));
        e.setDrag(stats.get(StatsKey.DRAG));
        e.setLifeTime(stats.get(StatsKey.LIFETIME));
        e.setPos(spawnPos.x , spawnPos.y, spawnPos.z);

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
