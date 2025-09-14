package net.mynameistmillo.experimentalmod.event;


import net.minecraft.client.renderer.entity.EntityRenderers;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.entity.ModEntities;
import net.mynameistmillo.experimentalmod.entity.client.BasicProjectileRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = ExperimentalMod.MOD_ID,value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(final net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
        EntityRenderers.register(ModEntities.BASIC_PROJECTILE.get(), BasicProjectileRenderer::new);
    }
}
