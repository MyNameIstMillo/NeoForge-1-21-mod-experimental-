package net.mynameistmillo.experimentalmod.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.mynameistmillo.experimentalmod.ExperimentalMod;

public class EntityDataTextures {

    // gets name of the projectile ane return resource location
    public static ResourceLocation getTxtPathSide(String name){
        String  res;

        switch (name){
            case "spark_bolt" -> res = "spark_bolt_side";
            case "bubble_spark" -> res = "bubble_spark_side";
            case "teleport_bolt" -> res = "teleport_bolt_side";
            default -> res = "default_side";
        }
        res = "textures/entity/basic_projectile/"+res+".png";

        return ResourceLocation.fromNamespaceAndPath(ExperimentalMod.MOD_ID, res);
    }




    // gets name of the projectile ane return resource location
    public static ResourceLocation getTxtPathFront(String name){
        String  res;

        switch (name){
            case "spark_bolt" -> res = "spark_bolt_front";
            case "bubble_spark" -> res = "bubble_spark_side";
            case "teleport_bolt" -> res = "teleport_bolt_side";
            default -> res = "default_front";
        }
        res = "textures/entity/basic_projectile/"+res+".png";

        return ResourceLocation.fromNamespaceAndPath(ExperimentalMod.MOD_ID, res);
    }



    // gets name of the projectile ane return shift
    public static Float getFrontAxisShift(String name){
        float shift;

        switch (name){
            case "spark_bolt" -> shift = 0.5f;
            default -> shift = 0f;

        }

        return shift;
    }




}
