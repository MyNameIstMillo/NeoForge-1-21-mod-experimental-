package net.mynameistmillo.experimentalmod.entity.custom;


import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.Enum.physicRelated.Plane;
import net.mynameistmillo.experimentalmod.Enum.physicRelated.ProjectionMode;

public class HelperPhysic {




    public static Vec3 projectToPlane(Vec3 vec, Plane plane, ProjectionMode mode){
        Vec3 base;

        switch (plane){
            case XY -> base = new Vec3(vec.x, vec.y, 0);
            case XZ -> base = new Vec3(vec.x, 0, vec.z);
            case YX -> base = new Vec3(0, vec.y, vec.z);
            default -> throw new  IllegalStateException();
        }

        if (base.lengthSqr()==0) return Vec3.ZERO;

        switch (mode){
            case PROJECT -> {
                return base;
            }
            case ROTATE -> {
                return base.normalize().scale(vec.length());
            }
            default -> throw new  IllegalStateException();
        }
    }

    public static Vec3 rotateAround(Vec3 vec, double angleDeg, double strength){
        Vec3 dir = vec.normalize();
        Vec3 prep = perpendicularDirection(vec, angleDeg);

        Vec3 result = dir.add(prep.scale(strength).normalize());
        return result.scale(vec.length());

    }

    public static Vec3 perpendicularDirection(Vec3 vec, double angleDeg){
        Vec3 forward = vec.normalize();
        Vec3 worldUp = new Vec3(0,1,0);

        if (Math.abs(forward.dot(worldUp))>0.999) worldUp = new Vec3(1,0,0);

        Vec3 right = forward.cross(worldUp).normalize();
        Vec3 up = right.cross(forward).normalize();

        double red = Math.toRadians(angleDeg);

        return up.scale(Math.cos(red)).add(right.scale(Math.sin(red)));
    }



}
