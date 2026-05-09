package net.mynameistmillo.experimentalmod.UsefullFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TeleportInSomeWay {

    public static void checkAndResetFallSpeed(Level level, BlockPos pos, Vec3 normal, Player caster){

        if(pos != null && normal != null) {
            double x = pos.getX() ;
            double y = pos.getY() - 1;
            double z = pos.getZ() ;


            BlockState state = level.getBlockState(BlockPos.containing(x, y, z));
            if (!state.blocksMotion()){
                caster.teleportTo(x +0.5f, y, z +0.5f);
                caster.resetFallDistance();
            }
        }
    }

    public static void JustTeleport(Level level, BlockPos pos, Vec3 normal, Player caster){

        if(pos != null && normal != null) {
            double x = pos.getX() ;
            double y = pos.getY() - 1;
            double z = pos.getZ() ;

            caster.teleportTo(x +0.5f, y, z +0.5f);

        }
    }


}
