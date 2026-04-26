package net.mynameistmillo.experimentalmod.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.mynameistmillo.experimentalmod.ExperimentalMod;

public class EntityDataTextures {

    // I assume that EVERY proj have OWN side png
    // gets name of the projectile and return resource location
    public static ResourceLocation getTxtPathSide(String name){
        String res = "textures/entity/basic_projectile/"+name+"_side.png";

        return ResourceLocation.fromNamespaceAndPath(ExperimentalMod.MOD_ID, res);
    }



    // add only if proj have own FRONT png
    // if not add then front == side
    // gets name of the projectile and return resource location
    public static ResourceLocation getTxtPathFront(String name){
        String ending;

        switch (name){
            case "pin_point",
                 "slime_ball",
                 "spin_spark",
                 "spark_bolt" -> ending = "_front";

            default -> ending = "_side";
        }

        String res = "textures/entity/basic_projectile/"+name+ending+".png";

        return ResourceLocation.fromNamespaceAndPath(ExperimentalMod.MOD_ID, res);
    }


    // if proj needs front png to be shifted
    // gets name of the projectile and return shift
    public static Float getFrontAxisShift(String name){
        float shift;

        switch (name){
            case "spark_bolt",
                 "slime_ball" -> shift = 0.5f;

            case "pin_point" -> shift = 1.0f;
            default -> shift = 0f;
        }
        return shift;
    }




}
