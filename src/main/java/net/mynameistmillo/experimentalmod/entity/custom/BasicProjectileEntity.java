package net.mynameistmillo.experimentalmod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.entity.ModEntities;
import net.mynameistmillo.experimentalmod.spells.ISpell;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class BasicProjectileEntity extends Projectile {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);
    private float initWidth;
    private float initHeight;

    //acceleration should be in 0.0 -> no, 0.2 -> big gravity
    private Vec3 acceleration = Vec3.ZERO;
    //gravity should bo in 0.0 -> no gravity, 0.2 -> bigger gravity
    private float gravity = 0f;
    //drag should be in 0.~8 -> big drag, close to 1 0.999 -> no drag
    private float drag = 1f;
    //energyLoss should be in 0 -> stop, 1 -> no Loss, 1.2 -> max?
    private float energyLoss = 0.0f;

    private float MIN_SPEED = 0.05f;

    private float lifeTime = 60;

    private ItemStack spellStack = ItemStack.EMPTY;
    private ItemStack wandStack = ItemStack.EMPTY;
    private UUID casterUUID = null;

    private static final double MAX_STEP = 0.5;
    private static final int MAX_STEPS = 8;

    
    public BasicProjectileEntity(EntityType<? extends  BasicProjectileEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = false;
    }

    public BasicProjectileEntity(Level level, LivingEntity shooter, float width, float height){
        this(ModEntities.BASIC_PROJECTILE.get(), level);
        this.initWidth = width;
        this.initHeight = height;
        //this.refreshDimensions();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(this.initWidth, this.initHeight);
    }

    public void setAcceleration(Vec3 acceleration) { this.acceleration = acceleration;}

    public void setGravity(float gravity) { this.gravity = gravity;}

    public void setDrag(float drag) { this.drag = drag;}

    public void setEnergyLoss(float energyLoss) { this.energyLoss = energyLoss;}

    public void setLifeTime(float lifeTime) { this.lifeTime = lifeTime;}

    public void setSpellStack(ItemStack stack){ this.spellStack = stack == null? ItemStack.EMPTY :stack.copy();}

    public void setWandStack(ItemStack stack){ this.wandStack = stack == null ? ItemStack.EMPTY : stack.copy();}

    public void setCasterUUID(UUID id){ this.casterUUID = id; }


    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat("ProjGravity", this.gravity);
        nbt.putFloat("ProjDrag", this.drag);
        nbt.putFloat("EnergyLoss", this.energyLoss);
        nbt.putDouble("AccelX", this.acceleration.x);
        nbt.putDouble("AccelY", this.acceleration.y);
        nbt.putDouble("AccelZ", this.acceleration.z);
        nbt.putDouble("lifeTime", this.lifeTime);

        if(this.spellStack.isEmpty()) {
            CompoundTag spellTag = new CompoundTag();
            this.spellStack.save(level().registryAccess(), spellTag);
            nbt.put("SpellStack", spellTag);
        }
        if(this.wandStack.isEmpty()) {
            CompoundTag wandTag = new CompoundTag();
            this.wandStack.save(level().registryAccess(), wandTag);
            nbt.put("WandStack", wandTag);
        }
        if(this.casterUUID != null) {
            nbt.putUUID("CasterUUID", this.casterUUID);
        }

    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("ProjGravity")) this.gravity = nbt.getFloat("ProjGravity");
        if (nbt.contains("ProjDrag")) this.drag = nbt.getFloat("ProjDrag");
        if (nbt.contains("EnergyLoss")) this.energyLoss = nbt.getFloat("EnergyLoss");
        this.acceleration = new Vec3(nbt.getDouble("AccelX"), nbt.getDouble("AccelY"), nbt.getDouble("AccelZ"));
        if (nbt.contains("lifeTime")) this.lifeTime = nbt.getFloat("lifeTime");

        if(nbt.contains("SpellStack", Tag.TAG_COMPOUND)){
            this.spellStack = ItemStack.parseOptional(level().registryAccess(), nbt.getCompound("SpellStack"));
        } else this.spellStack = ItemStack.EMPTY;

        if(nbt.hasUUID("CasterUUID")){
            this.casterUUID = nbt.getUUID("CasterUUID");
        }
    }

    @Override
    public void tick() {
        super.tick();
        //lifeTime logic
        if(!this.level().isClientSide()){
            this.lifeTime--;
            if(this.lifeTime<=0){
                this.handleOnExpire(this.blockPosition());
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
        int steps = (int)Math.ceil(distance / MAX_STEP); // podziel na kawałki max ~0.75 bloku
        steps = Math.max(1, Math.min(steps, MAX_STEPS)); // ogranicz max steps do 5 dla perfomansu

        Vec3 currentPos = start;
        Vec3 stepDelta = delta.scale(1.0 / steps);

        for (int s = 0; s < steps; s++) {
            Vec3 end = currentPos.add(stepDelta);

            // block raytrace
            HitResult blockHit = this.level().clip(new ClipContext(currentPos, end,
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

            // entity raytrace: używamy AABB małego inflate zależnego od rozmiaru
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
                BlockHitResult bhr = (BlockHitResult) blockHit;
                Direction face = bhr.getDirection();
                Vec3 hitVec = bhr.getLocation();

                BlockPos hit = bhr.getBlockPos();
                BlockPos beforeHit = hit.relative(face);

                Vec3 offset = new Vec3(face.getStepX(), face.getStepY(), face.getStepZ()).scale(0.01);
                Vec3 placePos = hitVec.subtract(this.getDeltaMovement().normalize().scale(0.01));
                //this.setPos(placePos.x, placePos.y, placePos.z);

                this.handleSpellHit(null, beforeHit);

                return;

            } else if (entityHit != null) {
                this.handleSpellHit(entityHit.getEntity(), null);
                return;

            }

            // no hit in this substep -> move
            this.move(MoverType.SELF, stepDelta);
            //this.setDeltaMovement(stepDelta);
            currentPos = this.position(); // zaktualizowana po move()
        }
    }

    private boolean canHit(Entity e){
        if(e == this) return false;
        if(e.isSpectator() || !e.isAlive()) return false;
        if(this.casterUUID != null && e.getUUID().equals(this.casterUUID)) return false;
        return !(e instanceof BasicProjectileEntity);
    }


    private void handleSpellHit(@Nullable Entity hitEntity, @Nullable BlockPos hitBlock){
        if(level().isClientSide()) return;
        this.discard();
        LOGGER.info("                             hit!");
        if(!this.spellStack.isEmpty()){
            Item item = this.spellStack.getItem();
            if(item instanceof ISpell){

                Player caster = null;
                if(this.casterUUID != null){
                    Entity e = ((ServerLevel)this.level()).getEntity(this.casterUUID);
                    if(e instanceof Player p) caster =p;
                }
                if(caster == null && this.getOwner() instanceof Player p) caster = p;

                ISpell spellLogic = (ISpell) item;

                spellLogic.onHit(this.level(), hitEntity, hitBlock, caster, this.wandStack);


            }
        }
    }

    private void handleOnExpire(BlockPos pos){
        if(level().isClientSide()) return;
        this.discard();

        if(!this.spellStack.isEmpty()){
            Item item = this.spellStack.getItem();
            if(item instanceof ISpell){

                Player caster = null;
                if(this.casterUUID != null){
                    Entity e = ((ServerLevel)this.level()).getEntity(this.casterUUID);
                    if(e instanceof Player p) caster =p;
                }
                if(caster == null && this.getOwner() instanceof Player p) caster = p;

                ISpell spellLogic = (ISpell) item;
                spellLogic.onExpire(this.level(), pos, caster,this.wandStack);
            }
        }
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public boolean canCollideWith(Entity entity) {
        return !(entity instanceof BasicProjectileEntity);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }
}
