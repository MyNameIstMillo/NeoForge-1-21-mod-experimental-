package net.mynameistmillo.experimentalmod.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.entity.ModEntities;

public class BasicProjectileEntity extends Projectile {

    //acceleration should be in 0.0 -> no 0.2 -> big gravity
    private Vec3 acceleration = Vec3.ZERO;
    //gravity should bo in 0.0 -> no gravity 0.2 -> bigger gravity
    private float gravity = 0.02f;
    //drag should be in 0.~8 -> big drag close to 1 0.999 -> no drag
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

    private void moveDesc() {
        Vec3 start = this.position();
        Vec3 delta = this.getDeltaMovement();
        double distance = delta.length();

        // sub-stepping for very fast projectiles (prevents tunneling)
        int steps = (int)Math.ceil(distance / 0.75); // podziel na kawałki max ~0.75 bloku
        steps = Math.max(1, Math.min(steps, 5)); // ogranicz max steps do 5 dla perfomansu

        Vec3 currentPos = start;
        for (int s = 0; s < steps; s++) {
            Vec3 stepDelta = delta.scale(1.0 / steps);
            Vec3 end = currentPos.add(stepDelta);

            // block raytrace
            HitResult blockHit = this.level().clip(new ClipContext(currentPos, end,
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

            // entity raytrace: używamy AABB małego inflatu zależnego od rozmiaru
            AABB aabb = this.getBoundingBox().expandTowards(stepDelta).inflate(0.3D);
            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(this.level(), this, currentPos, end, aabb, this::canHit);

            // wybierz najbliższe trafienie (jeśli oba istnieją)
            double blockDist = Double.POSITIVE_INFINITY;
            double entityDist = Double.POSITIVE_INFINITY;
            if (blockHit != null && blockHit.getType() == HitResult.Type.BLOCK) {
                blockDist = blockHit.getLocation().distanceTo(currentPos);
            } else {
                blockHit = null;
            }
            if (entityHit != null) {
                entityDist = entityHit.getLocation().distanceTo(currentPos);
            } else {
                entityHit = null;
            }

            if (blockHit != null && blockDist <= entityDist) {
                this.onHitBlock(blockHit);
                if (!this.level().isClientSide()) this.discard();
                return;
            } else if (entityHit != null) {
                this.onHitEntity(entityHit);
                if (!this.level().isClientSide()) this.discard();
                return;
            }

            // no hit in this substep -> move
            this.move(MoverType.SELF, stepDelta);
            currentPos = this.position(); // zaktualizowana po move()
        }
    }

    private boolean canHit(Entity e){
        return !e.isSpectator() && e.isAlive() && e != this.getOwner();
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
