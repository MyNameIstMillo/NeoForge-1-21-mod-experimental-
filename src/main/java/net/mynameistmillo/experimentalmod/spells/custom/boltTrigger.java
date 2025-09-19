package net.mynameistmillo.experimentalmod.spells.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import net.mynameistmillo.experimentalmod.items.ModItems;
import net.mynameistmillo.experimentalmod.spells.ISpell;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class boltTrigger extends Item implements ISpell {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public boltTrigger(Properties properties) {
        super(properties);
    }

    @Override
    public Entity spawnSpell(Level level, BlockPos pos, Player caster, ItemStack wandStack) {
        if(level.isClientSide()) return null;

        Vec3 look = caster.getLookAngle();

        double x = caster.getX() + look.x * 1.2;
        double y = caster.getY() + 1.25 + look.y * 1.2;
        double z = caster.getZ() + look.z * 1.2;

        float scale = 0.001f;
        //Vec3 acceleration = new Vec3(look.x, look.y, look.z).scale(scale);
        float speed = 0.8f;
        float gravity = 0.03f;
        float drag = 1.0f;
        float lifeTime = 200;

        BasicProjectileEntity projectile = new BasicProjectileEntity(level, caster, 1.0f, 1.0f);

        projectile.setDeltaMovement(look.x * speed, look.y * speed, look.z * speed);
        //projectile.setAcceleration(acceleration);
        projectile.setGravity(gravity);
        projectile.setDrag(drag);
        projectile.setLifeTime(lifeTime);
        projectile.setPos(x,y,z);

        projectile.setSpellStack(new ItemStack(ModItems.BOLT_TRIGGER.get()));
        projectile.setWandStack(wandStack.copy());
        projectile.setCasterUUID(caster.getUUID());

        level.addFreshEntity(projectile);
        projectile.refreshDimensions();

        return projectile;
    }


    @Override
    public Explosion spawnExplosion(Level level, BlockPos pos, Player caster, ItemStack wandStack) {
        return null;
    }

    @Override
    public void onHit(Level level, @Nullable Entity hitEntity, @Nullable BlockPos hitBlock, Player caster, Vec3 normal, ItemStack wandStack) {
        if(level.isClientSide() || hitBlock == null) return;
        LOGGER.info("onHit boltTrigger -> block -> {} , entyti -> {} , normal -> {}", hitBlock, hitEntity, normal);

        ItemStack held = caster.getMainHandItem();

        if(held.getItem() instanceof BlockItem blockItem){
            if(level.isEmptyBlock(hitBlock)){
                UseOnContext ctx = new UseOnContext(caster, InteractionHand.MAIN_HAND,
                        new BlockHitResult(Vec3.atCenterOf(hitBlock), Direction.UP, hitBlock, false));
                InteractionResult result = blockItem.useOn(ctx);
                if (result.consumesAction()) {
                    held.shrink(1);
                }
            }
        }

    }

    @Override
    public void onExpire(Level level, BlockPos pos, Player caster, Vec3 normal, ItemStack wandStack) {
        LOGGER.info("expire!");

    }
}
