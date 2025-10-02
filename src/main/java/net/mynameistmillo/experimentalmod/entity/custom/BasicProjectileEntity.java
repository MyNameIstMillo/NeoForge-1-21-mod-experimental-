package net.mynameistmillo.experimentalmod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.entity.ModEntities;
import net.mynameistmillo.experimentalmod.spellLogic.ISpell;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class BasicProjectileEntity extends Projectile {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);
    private static final EntityDataAccessor<String> DATA_NAME = SynchedEntityData.defineId(BasicProjectileEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> PROJ_WIDTH = SynchedEntityData.defineId(BasicProjectileEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PROJ_HEIGHT = SynchedEntityData.defineId(BasicProjectileEntity.class, EntityDataSerializers.FLOAT);

    private float initWidth = 0.25f;
    private float initHeight = 0.25f;

    private float gravity = 0f;
    private float drag = 1f;
    private float minSpeed = 0.001f;

    private float lifeTime = 60;
    private Vec3 prevDelta = Vec3.ZERO;

    private ItemStack spellStack = ItemStack.EMPTY;
    private ItemStack wandStack = ItemStack.EMPTY;
    private UUID casterUUID = null;

    private static final double MAX_STEP = 0.75D;
    private static final int MAX_STEPS = 5;

    
    public BasicProjectileEntity(EntityType<? extends  BasicProjectileEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = false;
    }

    public BasicProjectileEntity(Level level, LivingEntity shooter, float width, float height){
        this(ModEntities.BASIC_PROJECTILE.get(), level);
        this.initWidth = width;
        this.initHeight = height;
        this.entityData.set(PROJ_WIDTH, width);
        this.entityData.set(PROJ_HEIGHT, height);
        this.refreshDimensions();
    }

    //public void setAcceleration(Vec3 acceleration) { this.acceleration = acceleration;}

    public void setGravity(float gravity) { this.gravity = gravity;}

    public void setDrag(float drag) { this.drag = drag;}

    public void setLifeTime(float lifeTime) { this.lifeTime = lifeTime;}

    public void setMinSpeed(float minSpeed){this.minSpeed = minSpeed;}

    public void setSpellStack(ItemStack stack){ this.spellStack = stack == null? ItemStack.EMPTY :stack.copy();}

    public void setWandStack(ItemStack stack){ this.wandStack = stack == null ? ItemStack.EMPTY : stack.copy();}

    public void setCasterUUID(UUID id){ this.casterUUID = id; }




    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat("ProjGravity", this.gravity);
        nbt.putFloat("ProjDrag", this.drag);
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
        nbt.putString("proj_name", getProjName());

    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("ProjGravity")) this.gravity = nbt.getFloat("ProjGravity");
        if (nbt.contains("ProjDrag")) this.drag = nbt.getFloat("ProjDrag");
        if (nbt.contains("lifeTime")) this.lifeTime = nbt.getFloat("lifeTime");

        if(nbt.contains("SpellStack", Tag.TAG_COMPOUND)){
            this.spellStack = ItemStack.parseOptional(level().registryAccess(), nbt.getCompound("SpellStack"));
        } else this.spellStack = ItemStack.EMPTY;

        if(nbt.hasUUID("CasterUUID")){
            this.casterUUID = nbt.getUUID("CasterUUID");
        }
        if (nbt.contains("proj_name")) setProjName(nbt.getString("proj_name"));
    }

    @Override
    public void tick() {
        super.tick();

        if(!this.level().isClientSide()){
            this.lifeTime--;
            if(this.lifeTime<=0){
                this.handleOnExpire(this.blockPosition(), this.getDeltaMovement().normalize());
                return;
            }
        }
        //acceleration
        Vec3 vector = this.getDeltaMovement();
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
        if(this.minSpeed >= 0) {
            if (distance <= this.minSpeed)
                this.handleOnExpire(this.blockPosition(), this.getDeltaMovement().normalize());
        }

        int steps = (int)Math.ceil(distance / MAX_STEP);
        steps = Math.max(1, Math.min(steps, MAX_STEPS));

        Vec3 currentPos = start;
        Vec3 stepDelta = delta.scale(1.0 / steps);

        for (int s = 0; s < steps; s++) {
            Vec3 end = currentPos.add(stepDelta);

//            HitResult blockHit = this.level().clip(new ClipContext(currentPos, end,
//                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            AABB aabb = this.getBoundingBox().expandTowards(stepDelta).inflate(0.04D);
            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(this.level(), this, currentPos, end, aabb, this::canHit);
//
//            double blockDist = Double.POSITIVE_INFINITY;
//            double entityDist = Double.POSITIVE_INFINITY;
//
//            if (blockHit != null && blockHit.getType() == HitResult.Type.BLOCK) {
//                blockDist = blockHit.getLocation().distanceTo(currentPos);
//                LOGGER.info("blockHit");
//            }
//            else { blockHit = null; }
            if (entityHit != null) {
                //entityDist = entityHit.getLocation().distanceTo(currentPos);
                //LOGGER.info("entity?");
                this.handleSpellHit(entityHit.getEntity(), null, null);
                return;
            }else {
                entityHit = null;
            }
//            if (blockHit != null && blockDist <= entityDist) {
//                BlockHitResult bhr = (BlockHitResult) blockHit;
//                this.onBlockHit(bhr);
//                return;
//
//            } els
            this.move(MoverType.SELF, stepDelta);
            currentPos = this.position();

            if(entityHit == null) {
                if (checkBounceGuessAndHandle(this.getDeltaMovement(), this.prevDelta)) {
                    return;
                }
            }

            this.prevDelta = this.getDeltaMovement();
        }
    }

    private boolean canHit(Entity e){
        if(e == this) return false;
        if(e.isSpectator() || !e.isAlive()) return false;
        //if(this.casterUUID != null && e.getUUID().equals(this.casterUUID)) return false;
        return !(e instanceof BasicProjectileEntity);
    }

    private boolean handledHit = false;
    private static final double EPS = 1e-8;
    private boolean checkBounceGuessAndHandle(Vec3 delta, Vec3 prevDelta) {
        //if (this.tickCount <= 2) return false;


        boolean xHit = Math.abs(delta.x) < EPS && Math.abs(prevDelta.x) > EPS;
        boolean yHit = Math.abs(delta.y) < EPS && Math.abs(prevDelta.y) > EPS;
        boolean zHit = Math.abs(delta.z) < EPS && Math.abs(prevDelta.z) > EPS;

        if (!(xHit || yHit || zHit)) return false;

        Vec3 currentPos = this.position();
        Vec3 prevPos = currentPos.subtract(prevDelta);
        Vec3 start = prevPos;
        Vec3 end = currentPos;

        AABB aabb = this.getBoundingBox().expandTowards(prevDelta).inflate(0.05D);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(this.level(), this, start, end, aabb, this::canHit);
        if(entityHit != null) {
            //LOGGER.info("perhaps entity? -> {}, start -> {} , end -> {}", entityHit, start, end);
            this.handleSpellHit(entityHit.getEntity(), null, null);
            return true;
        }

        BlockPos pos = this.blockPosition();

        Vec3 normal;
        Direction faceGuess;

        if (xHit) {
            double sign = Math.signum(prevDelta.x);
            normal = new Vec3(-sign, 0.0, 0.0);
            faceGuess = sign > 0 ? Direction.WEST : Direction.EAST;
        } else if (yHit) {
            double sign = Math.signum(prevDelta.y);
            normal = new Vec3(0.0, -sign, 0.0);
            faceGuess = sign > 0 ? Direction.DOWN : Direction.UP;
        } else {
            double sign = Math.signum(prevDelta.z);
            normal = new Vec3(0.0, 0.0, -sign);
            faceGuess = sign > 0 ? Direction.NORTH : Direction.SOUTH;
        }

        Vec3 hitVec = estimateHitVecForFace(pos, faceGuess);
        Vec3 safePos = hitVec.subtract(normal.scale(0.001));
        this.setPos(safePos.x, safePos.y, safePos.z);
        this.setDeltaMovement(Vec3.ZERO);

        BlockPos hitPos = pos.relative(faceGuess.getOpposite());
        BlockHitResult bhr = new BlockHitResult(hitVec, faceGuess, hitPos, false);
        //LOGGER.info("perhaps?");
        this.onBlockHit(bhr);

        return true;
    }
    private Vec3 estimateHitVecForFace(BlockPos pos, Direction face) {
        double x = Mth.clamp(this.getX(), pos.getX(), pos.getX() + 1.0);
        double y = Mth.clamp(this.getY(), pos.getY(), pos.getY() + 1.0);
        double z = Mth.clamp(this.getZ(), pos.getZ(), pos.getZ() + 1.0);

        switch (face) {
            case WEST:  return new Vec3(pos.getX(), y, z);
            case EAST:  return new Vec3(pos.getX() + 1.0, y, z);
            case DOWN:  return new Vec3(x, pos.getY(), z);
            case UP:    return new Vec3(x, pos.getY() + 1.0, z);
            case NORTH: return new Vec3(x, y, pos.getZ());
            case SOUTH: return new Vec3(x, y, pos.getZ() + 1.0);
            default:    return new Vec3(x, y, z);
        }
    }

    private void onBlockHit(BlockHitResult result){
        if(this.handledHit) return;
        this.handledHit  = true;
        Vec3 hitVec = result.getLocation();
        Direction face = result.getDirection();
        Vec3 normal = new Vec3(face.getStepX(), face.getStepY(), face.getStepZ());

        Vec3 safePos = hitVec.subtract(this.getDeltaMovement().normalize().scale(0.001));
        this.setPos(safePos.x, safePos.y, safePos.z);
        this.setDeltaMovement(Vec3.ZERO);

        BlockPos hit = result.getBlockPos();
        BlockPos beforeHit = hit.relative(face);

        this.handleSpellHit(null, beforeHit, normal);
    }


    private void handleSpellHit(@Nullable Entity hitEntity,
                                @Nullable BlockPos hitBlock,
                                @Nullable Vec3 normal){
        this.discard();
        if(level().isClientSide()) return;

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

                spellLogic.onHit(this.level(), hitEntity, hitBlock, caster, normal, this.wandStack);
                handleOnExpire(hitBlock, normal);


            }
        }
    }

    private void handleOnExpire(BlockPos pos, Vec3 normal){
        this.discard();
        if(level().isClientSide()) return;

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
                spellLogic.onExpire(this.level(), pos, caster, normal, this.wandStack);
            }
        }
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(PROJ_WIDTH, 0.25f);
        builder.define(PROJ_HEIGHT, 0.25f);
        builder.define(DATA_NAME, "");
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key == PROJ_WIDTH || key == PROJ_HEIGHT) {
            this.initWidth = this.entityData.get(PROJ_WIDTH);
            this.initHeight = this.entityData.get(PROJ_HEIGHT);
            this.refreshDimensions();
        }else if (key == DATA_NAME){
            this.name = this.entityData.get(DATA_NAME);
        }
    }
    private String name = "";
    public void setProjName(String name){
        this.entityData.set(DATA_NAME, name == null? "" : name);
        this.name = name == null? "": name;
    }
    public String getProjName(){return this.entityData.get(DATA_NAME);}

    @Override
    public EntityDimensions getDimensions(Pose pose) { return EntityDimensions.scalable(this.initWidth, this.initHeight); }

    @Override
    public boolean canCollideWith(Entity entity) {
        return !(entity instanceof BasicProjectileEntity);
    }

    @Override
    public boolean isPickable() {
        return false; //for standing true?
    }

    @Override
    public boolean isPushable() {
        return false; //for standing true?
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false; // true for to be able to stand
    }
}
