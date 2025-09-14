package net.mynameistmillo.experimentalmod.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.entity.ModEntities;

public class BasicProjectileEntity extends Projectile {

    private Vec3 acceleration = Vec3.ZERO;
    private float gravity = 0.1f;
    //the more drag the less drag you have! yes 1 -> heavy 10 -> almost non
    //wait drag should be in (1,0)?
    private float drag = 0.99f;
    private float damage = 0.0f;
    private float lifeTime = 60;

    
    public BasicProjectileEntity(EntityType<? extends  BasicProjectileEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = false;
    }

    public BasicProjectileEntity(Level level, LivingEntity shooter){
        this(ModEntities.BASIC_PROJECTILE.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getY(), shooter.getZ());
    }

    public void setAcceleration(Vec3 acceleration) {
        this.acceleration = acceleration;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public void setDrag(float drag) {
        this.drag = drag;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setLifeTime(float lifeTime) {
        this.lifeTime = lifeTime;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat("ProjGravity", this.gravity);
        nbt.putFloat("ProjDrag", this.drag);
        nbt.putFloat("ProjDamage", this.damage);
        nbt.putDouble("AccelX", this.acceleration.x);
        nbt.putDouble("AccelY", this.acceleration.y);
        nbt.putDouble("AccelZ", this.acceleration.z);
        nbt.putDouble("lifeTime", this.lifeTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("ProjGravity")) this.gravity = nbt.getFloat("ProjGravity");
        if (nbt.contains("ProjDrag")) this.drag = nbt.getFloat("ProjDrag");
        if (nbt.contains("ProjDamage")) this.damage = nbt.getFloat("ProjDamage");
        this.acceleration = new Vec3(nbt.getDouble("AccelX"), nbt.getDouble("AccelY"), nbt.getDouble("AccelZ"));
        if (nbt.contains("lifeTime")) this.lifeTime = nbt.getFloat("lifeTime");
    }

    @Override
    public void tick() {
        super.tick();
        //lifeTime logic
        if(!this.level().isClientSide()){
            this.lifeTime--;
            if(this.lifeTime<=0){
                this.onExpire();
                this.discard();
                return;
            }
        }
        //acceleration
        Vec3 vector = this.getDeltaMovement().add(this.acceleration);
        //gravity
        vector = vector.add(0, -this.gravity, 0);
        //drag
        vector = vector.multiply(this.drag, this.drag, this.drag);
        //apply changes
        this.setDeltaMovement(vector);
        this.moveDesc();

    }

    private void moveDesc(){
        Vec3 start = this.position();
        Vec3 end = start.add(this.getDeltaMovement());

        HitResult blockHit = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE, this));
        if(blockHit != null && blockHit.getType() == HitResult.Type.BLOCK){
            this.onHitBlock(blockHit);
            if(!this.level().isClientSide()) this.discard();
            return;
        }

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(this.level(), this, start, end,
                this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), (e)->{
            return !e.isSpectator() && e.isAlive() && e != this.getOwner();
        });

        if(entityHit != null){
            this.onHitEntity(entityHit);
            if(!this.level().isClientSide()) this.discard();
            return;
        }

        this.setPos(this.getX() + this.getDeltaMovement().x,
                    this.getY() + this.getDeltaMovement().y,
                    this.getZ() + this.getDeltaMovement().z);



    }





    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void onHit(HitResult result) {
    }

    protected void onHitBlock(HitResult result) {
    }

    protected void onHitEntity(Entity entity){
    }

    protected void onExpire(){
    }


    @Override
    public boolean isNoGravity() {
        return false;
    }
}
