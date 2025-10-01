package net.mynameistmillo.experimentalmod.spellProjectile.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import net.mynameistmillo.experimentalmod.spellLogic.ISpell;
import net.mynameistmillo.experimentalmod.spellLogic.stats.SpellStats;
import net.mynameistmillo.experimentalmod.spellLogic.stats.StatsKey;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class bubbleSpark extends Item implements ISpell {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);
    public bubbleSpark(Properties properties) {
        super(properties);
        this.baseStats = new SpellStats();
        this.baseStats.set(StatsKey.GRAVITY, 0.00f);
        this.baseStats.set(StatsKey.DRAG, 0.80f);
        this.baseStats.set(StatsKey.SPEED, 0.7f);
        this.baseStats.set(StatsKey.LIFETIME, 200);
        this.baseStats.set(StatsKey.DAMAGE, 1.0f);
        this.baseStats.set(StatsKey.DISPLACEMENT, -0.2f);

    }

    private final SpellStats baseStats;
    @Override
    public SpellStats getBaseStats() {
        return baseStats;
    }

    @Override
    public Entity spawnSpell(Level level, BlockPos pos, Player caster, Vec3 normal, ItemStack wandStack, ItemStack thisSpell, int index) {
        if (level.isClientSide()) return null;

        BasicProjectileEntity proj = new BasicProjectileEntity(level, caster, 0.25f, 0.25f);

        String name = "bubble_spark";
        proj.setProjName(name);

        proj.setSpellStack(thisSpell.copy());
        proj.setWandStack(wandStack.copy());
        proj.setCasterUUID(caster.getUUID());


        this.baseStats.applyToProjectile(proj, pos, normal, caster);

        level.addFreshEntity(proj);



        return proj;
    }

    @Override
    public void onHit(Level level, @Nullable Entity hitEntity, @Nullable BlockPos hitBlock, Player caster, Vec3 normal, ItemStack wandStack) {

    }

    @Override
    public void onExpire(Level level, BlockPos pos, Player caster, Vec3 normal, ItemStack wandStack) {

    }

    @Override
    public Explosion createExplosion(Level level, BlockPos pos, Player caster, ItemStack wandStack) {
        return null;
    }
}
