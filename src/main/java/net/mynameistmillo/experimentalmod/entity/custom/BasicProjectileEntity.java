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
import net.mynameistmillo.experimentalmod.Enum.TriggerType;
import net.mynameistmillo.experimentalmod.entity.ModEntities;
import net.mynameistmillo.experimentalmod.Interface.IProjectile;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class BasicProjectileEntity extends Projectile {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);
    private static final EntityDataAccessor<String> DATA_NAME = SynchedEntityData.defineId(BasicProjectileEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> PROJ_WIDTH = SynchedEntityData.defineId(BasicProjectileEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PROJ_HEIGHT = SynchedEntityData.defineId(BasicProjectileEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DRAG = SynchedEntityData.defineId(BasicProjectileEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> GRAVITY = SynchedEntityData.defineId(BasicProjectileEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> LIFE_TIME = SynchedEntityData.defineId(BasicProjectileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PIERCING = SynchedEntityData.defineId(BasicProjectileEntity.class, EntityDataSerializers.INT);

    private float initWidth = 0.25f;
    private float initHeight = 0.25f;

    private Vec3 prevDelta = Vec3.ZERO;

    private ItemStack projStack = ItemStack.EMPTY;
    private ItemStack wandStack = ItemStack.EMPTY;
    private UUID casterUUID = null;

    private static final double MAX_STEP = 0.75D;
    private static final int MAX_STEPS = 5;

    public BasicProjectileEntity(EntityType<? extends  BasicProjectileEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = false;
    }

    public BasicProjectileEntity(Level level, float width, float height){
        this(ModEntities.BASIC_PROJECTILE.get(), level);
        this.initWidth = width;
        this.initHeight = height;
        this.entityData.set(PROJ_WIDTH, width);
        this.entityData.set(PROJ_HEIGHT, height);
        this.refreshDimensions();
    }

    public void setGravity(float gravity) {
        this.entityData.set(GRAVITY, gravity);
    }

    public float getGravityE(){
        return this.entityData.get(GRAVITY);
    }

    public void setDrag(float drag) {
        this.entityData.set(DRAG, drag);
    }

    public float getDrag(){
        return this.entityData.get(DRAG);
    }

    public void setLifeTime(int lifeTime) {
        this.entityData.set(LIFE_TIME, lifeTime);
    }

    public int getLifeTime(){
        return this.entityData.get(LIFE_TIME);
    }

    public void setProjStack(ItemStack stack){ this.projStack = stack == null? ItemStack.EMPTY :stack.copy();}

    public void setWandStack(ItemStack stack){ this.wandStack = stack == null ? ItemStack.EMPTY : stack.copy();}

    public void setCasterUUID(UUID id){ this.casterUUID = id; }

    private String name = "";

    public void setProjName(String name){
        this.entityData.set(DATA_NAME, name == null? "" : name);
        this.name = name == null? "": name;
    }

    public String getProjName(){return this.entityData.get(DATA_NAME);}


    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putFloat("ProjGravity", this.entityData.get(GRAVITY));
        nbt.putFloat("ProjDrag", this.entityData.get(DRAG));
        nbt.putInt("lifeTime", this.entityData.get(LIFE_TIME));

        if(!this.projStack.isEmpty()) {
            CompoundTag spellTag = new CompoundTag();
            this.projStack.save(level().registryAccess(), spellTag);
            nbt.put("ProjStack", spellTag);
        }
        if(!this.wandStack.isEmpty()) {
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
        if (nbt.contains("ProjGravity")) this.entityData.set(GRAVITY, nbt.getFloat("ProjGravity"));
        if (nbt.contains("ProjDrag")) this.entityData.set(DRAG, nbt.getFloat("ProjDrag"));
        if (nbt.contains("lifeTime")) this.entityData.set(LIFE_TIME, nbt.getInt("lifeTime"));

        if(nbt.contains("ProjStack", Tag.TAG_COMPOUND)){
            this.projStack = ItemStack.parseOptional(level().registryAccess(), nbt.getCompound("ProjStack"));
        } else this.projStack = ItemStack.EMPTY;

        if(nbt.hasUUID("CasterUUID")){
            this.casterUUID = nbt.getUUID("CasterUUID");
        }
        if (nbt.contains("proj_name")) setProjName(nbt.getString("proj_name"));
    }
    private static final double MIN_SPEED = 0.003;


    @Override
    public void tick() {
        super.tick();

        if(!this.level().isClientSide()){
            int lifeTime = getLifeTime();
            lifeTime--;
            if(lifeTime<=0){
                Vec3 pos = this.position();
                Vec3 dir = this.getDeltaMovement().normalize().reverse();
                handleProjHit(null, pos, dir, TriggerType.EXPIRE);
                return;
            }
            this.entityData.set(LIFE_TIME, lifeTime);
        }

        Vec3 vector = this.getDeltaMovement();

        if (vector.lengthSqr()<MIN_SPEED){
            Vec3 pos = this.position();
            Vec3 dir = this.getDeltaMovement().normalize().reverse();
            handleProjHit(null, pos, dir, TriggerType.BEFORE);
        }

        //gravity and drag
        float drag = getDrag();
        float gravity = getGravityE();

        vector = vector.add(0, -gravity, 0).scale(drag);

        //apply changes
        this.setDeltaMovement(vector);

        this.moveDesc();
    }

    private void moveDesc() {
        Vec3 start = this.position();
        Vec3 delta = this.getDeltaMovement();
        double distance = delta.length();


        int steps = (int)Math.ceil(distance / MAX_STEP);
        steps = Math.max(1, Math.min(steps, MAX_STEPS));

        Vec3 currentPos = start;
        Vec3 stepDelta = delta.scale(1.0 / steps);

        for (int s = 0; s < steps; s++) {
            Vec3 end = currentPos.add(stepDelta);


            AABB aabb = this.getBoundingBox().expandTowards(stepDelta).inflate(0.06D);
            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(this.level(), this, currentPos, end, aabb, this::canHit);

            if (entityHit != null) {
                Vec3 pos = this.position();
                handleProjHit(entityHit.getEntity(), pos, delta.normalize(), TriggerType.TRIGGER);
                return;
            }else {
                entityHit = null;
            }
            this.move(MoverType.SELF, stepDelta);
            currentPos = this.position();

            if(entityHit == null) {
                if (checkBounceGuessAndHandle(this.getDeltaMovement(), this.prevDelta, delta.normalize())) {
                    return;
                }
            }

            this.prevDelta = this.getDeltaMovement();
        }
    }

    private boolean checkBounceGuessAndHandle(Vec3 delta, Vec3 prevDelta, Vec3 normal) {
        double EPS = 1e-8;

        boolean xHit = Math.abs(delta.x) < EPS && Math.abs(prevDelta.x) > EPS;
        boolean yHit = Math.abs(delta.y) < EPS && Math.abs(prevDelta.y) > EPS;
        boolean zHit = Math.abs(delta.z) < EPS && Math.abs(prevDelta.z) > EPS;

        if (!(xHit || yHit || zHit)) return false;

        Vec3 currentPos = this.position();
        Vec3 start = currentPos.subtract(prevDelta);
        Vec3 end = currentPos;

        AABB aabb = this.getBoundingBox().expandTowards(prevDelta).inflate(0.05D);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(this.level(), this, start, end, aabb, this::canHit);
        if(entityHit != null) {
            Vec3 pos = this.position();
            handleProjHit(entityHit.getEntity(), pos, normal, TriggerType.TRIGGER);
            return true;
        }

        BlockPos pos = this.blockPosition();

        Direction faceGuess;

        if (xHit) {
            double sign = Math.signum(prevDelta.x);
            faceGuess = sign > 0 ? Direction.WEST : Direction.EAST;
        } else if (yHit) {
            double sign = Math.signum(prevDelta.y);
            faceGuess = sign > 0 ? Direction.DOWN : Direction.UP;
        } else {
            double sign = Math.signum(prevDelta.z);
            faceGuess = sign > 0 ? Direction.NORTH : Direction.SOUTH;
        }

        Vec3 hitVec = estimateHitVecForFace(pos, faceGuess);
        Vec3 safePos = hitVec.subtract(normal.scale(0.001));
        this.setPos(safePos.x, safePos.y, safePos.z);
        this.setDeltaMovement(Vec3.ZERO);

        BlockPos hitPos = pos.relative(faceGuess.getOpposite());
        BlockHitResult bhr = new BlockHitResult(hitVec, faceGuess, hitPos, false);

        //onBlockHit(bhr, normal, currentPos);
        handleProjHit(null, currentPos, normal, TriggerType.TRIGGER);

        return true;
    }

    private Vec3 estimateHitVecForFace(BlockPos pos, Direction face) {
        double x = Mth.clamp(this.getX(), pos.getX(), pos.getX() + 1.0);
        double y = Mth.clamp(this.getY(), pos.getY(), pos.getY() + 1.0);
        double z = Mth.clamp(this.getZ(), pos.getZ(), pos.getZ() + 1.0);

        return switch (face) {
            case WEST -> new Vec3(pos.getX(), y, z);
            case EAST -> new Vec3(pos.getX() + 1.0, y, z);
            case DOWN -> new Vec3(x, pos.getY(), z);
            case UP -> new Vec3(x, pos.getY() + 1.0, z);
            case NORTH -> new Vec3(x, y, pos.getZ());
            case SOUTH -> new Vec3(x, y, pos.getZ() + 1.0);
            default -> new Vec3(x, y, z);
        };
    }




    private void handleProjHit(@Nullable Entity hitEntity,
                               @Nullable Vec3 hitPos,
                               @Nullable Vec3 normal,
                               TriggerType type){
        if(level().isClientSide()) return;

        if(!this.projStack.isEmpty()){
            Item item = this.projStack.getItem();
            if(item instanceof IProjectile spellLogic){

                Player caster = null;
                if(this.casterUUID != null){
                    Entity e = ((ServerLevel)this.level()).getEntity(this.casterUUID);
                    if(e instanceof Player p) caster =p;
                }
                if(caster == null && this.getOwner() instanceof Player p) caster = p;

                spellLogic.onHit(this.level(), hitEntity, hitPos, caster, normal, this.wandStack, this.projStack);

                spellLogic.triggerAction(this.level(), hitEntity, hitPos, caster, normal, this.wandStack, this.projStack, type);

            }
        }

        maybeDiscard();
    }

    private void maybeDiscard() {
          this.discard();
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(PROJ_WIDTH, 3.0f);
        builder.define(PROJ_HEIGHT, 3.0f);
        builder.define(DATA_NAME, "");
        builder.define(DRAG, 0.0f);
        builder.define(GRAVITY, 0.0f);
        builder.define(LIFE_TIME, 0);
        builder.define(PIERCING, 0);
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
    
    private boolean canHit(Entity e){
        if(e == this) return false;
        if(e.isSpectator() || !e.isAlive()) return false;
        if(this.casterUUID != null && e.getUUID().equals(this.casterUUID)) return true;
        return !(e instanceof BasicProjectileEntity);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose){
        return EntityDimensions.scalable(this.initWidth, this.initHeight);
    }

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
