package net.mynameistmillo.experimentalmod.spells.stats;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.EnumMap;
import java.util.Map;

public class SpellStats implements INBTSerializable<CompoundTag> {
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

    public void applyToProjectile(BasicProjectileEntity e,
                                  BlockPos pos,
                                  Vec3 normal,
                                  Player caster){
        double x; double y; double z;
        if(caster == null){// not player so not player
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
        }else{ // player so near player
            x = caster.getX() + normal.x * 1.2;
            y = caster.getY() + 1.25 + normal.y * 1.2;
            z = caster.getZ() + normal.z * 1.2;
        }
        float speed = this.get(StatsKey.SPEED);
        e.setDeltaMovement(normal.x * speed,
                            normal.y * speed,
                            normal.z * speed);

        e.setGravity(this.get(StatsKey.GRAVITY));
        e.setDrag(this.get(StatsKey.DRAG));
        e.setLifeTime(this.get(StatsKey.LIFETIME));
        e.setPos(x,y,z);
        e.setCasterUUID(caster.getUUID());

    }



    public void writeToBuffer(FriendlyByteBuf buf){
        buf.writeByte(map.size());
        for (StatsKey k : StatsKey.values()){
            buf.writeUtf(k.getId());
            buf.writeFloat(get(k));
        }
    }

    public static SpellStats readFromBuffer(FriendlyByteBuf buf){
        SpellStats stats = new SpellStats();
        int count = buf.readByte();
        for (int i=0; i<count; i++){
            String id = buf.readUtf(128);
            float val = buf.readFloat();
            StatsKey key = StatsKey.byName(id);
            if (key != null) stats.set(key, val);
        }
        return stats;
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
