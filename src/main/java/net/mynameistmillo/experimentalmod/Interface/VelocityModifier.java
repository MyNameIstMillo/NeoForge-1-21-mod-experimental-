package net.mynameistmillo.experimentalmod.Interface;

import net.minecraft.world.phys.Vec3;

public interface VelocityModifier {
    Vec3 apply(int tick, Vec3 vec);
}
