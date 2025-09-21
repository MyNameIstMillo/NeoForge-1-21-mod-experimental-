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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import net.mynameistmillo.experimentalmod.items.ModItems;
import net.mynameistmillo.experimentalmod.spells.ISpell;
import net.mynameistmillo.experimentalmod.spells.stats.SpellStats;
import net.mynameistmillo.experimentalmod.spells.stats.StatsKey;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class boltTrigger extends Item implements ISpell {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    private final SpellStats baseStats;

    public boltTrigger(Properties properties) {
        super(properties);
        this.baseStats = new SpellStats();
        this.baseStats.set(StatsKey.GRAVITY, 0.03f);
        this.baseStats.set(StatsKey.DRAG, 1.0f);
        this.baseStats.set(StatsKey.SPEED, 0.8f);
        this.baseStats.set(StatsKey.LIFETIME, 60);
        this.baseStats.set(StatsKey.DAMAGE, 1.0f);
        this.baseStats.set(StatsKey.DISPLACEMENT, 0);


    }

    @Override
    public SpellStats getBaseStats() {
        return baseStats;
    }

    @Override
    public Entity spawnSpell(Level level, BlockPos pos, Player caster, Vec3 normal, ItemStack wandStack) {
        if(level.isClientSide()) return null;
        //create projectile
        BasicProjectileEntity projectile = new BasicProjectileEntity(level, caster, 0.25f, 0.25f);
        //connect spellItem to the projectile
        projectile.setSpellStack(new ItemStack(ModItems.BOLT_TRIGGER.get()));
        projectile.setWandStack(wandStack.copy());
        projectile.setCasterUUID(caster.getUUID());
        //here you can decide final stats on the spells, afer this player can't change them
        this.baseStats.set(StatsKey.GRAVITY, 0.03f);
        //LOGGER.info("normal -> {}", normal);

        //apply stats
        this.baseStats.applyToProjectile(projectile, pos, normal, caster);
        //add projectile to the world
        level.addFreshEntity(projectile);

        return projectile;
    }

    @Override
    public void onHit(Level level,
                      @Nullable Entity hitEntity,
                      @Nullable BlockPos hitBlock,
                      Player caster,
                      Vec3 normal,
                      ItemStack wandStack) {
        if(level.isClientSide()) return;
        LOGGER.info("onHit boltTrigger -> block -> {} , entyti -> {} , normal -> {}", hitBlock, hitEntity, normal);






    }

    @Override
    public void onExpire(Level level, BlockPos pos, Player caster, Vec3 normal, ItemStack wandStack) {
        LOGGER.info("expire!");


    }

    @Override
    public Explosion createExplosion(Level level, BlockPos pos, Player caster, ItemStack wandStack) {
        return null;
    }

    @Override
    public boolean spawnNextOnHit() {
        return false;
    }

    @Override
    public boolean spawnNextOnExpire() {
        return false;
    }

}
