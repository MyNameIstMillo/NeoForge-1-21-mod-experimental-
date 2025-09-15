package net.mynameistmillo.experimentalmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import org.joml.Matrix4f;

public class BasicProjectileRenderer extends EntityRenderer<BasicProjectileEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath
            (ExperimentalMod.MOD_ID, "textures/entity/basic_projectile/basic_projectile.png");

    public BasicProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BasicProjectileEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // rozmiar pocisku
        float size = 0.5f;
        poseStack.scale(size, size, size);

        // tu ustawiamy obrót tak, żeby "płaska" tekstura patrzyła na kamerę
        Minecraft mc = Minecraft.getInstance();
        // bierzemy rotację kamery
        poseStack.mulPose(mc.gameRenderer.getMainCamera().rotation());

        // pobierz buffer
        VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        Matrix4f mat = poseStack.last().pose();

        // narysuj prosty kwadrat (jedna płaszczyzna)
        vc.addVertex(mat, -1, -1, 0).setColor(255, 255, 255, 255)
                .setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 0, 1);
        vc.addVertex(mat, -1,  1, 0).setColor(255, 255, 255, 255)
                .setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 0, 1);
        vc.addVertex(mat,  1,  1, 0).setColor(255, 255, 255, 255)
                .setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 0, 1);
        vc.addVertex(mat,  1, -1, 0).setColor(255, 255, 255, 255)
                .setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0, 0, 1);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }


//    @Override
//    public void render(BasicProjectileEntity entity, float entityYaw, float partialTicks,
//                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
//        poseStack.pushPose();
//
//        // rozmiar "kwadratu"
//        float size = 0.5f;
//        poseStack.scale(size, size, size);
//
//
//        // pobierz buffer (tu wybierasz RenderType zamiast this.renderType)
//        VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
//
//        // narysuj 3 kwadraty (XY, XZ, ZY) — "billboard sphere"
//        Matrix4f mat = poseStack.last().pose();
//
//        // XY
//        vc.addVertex(mat, -1, -1, 0).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0,0,1).setUv(0, 1).setUv2(15, 15);
//        vc.addVertex(mat, -1,  1, 0).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0,0,1).setUv(0, 0).setUv2(15, 15);
//        vc.addVertex(mat,  1,  1, 0).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0,0,1).setUv(1, 0).setUv2(15, 15);
//        vc.addVertex(mat,  1, -1, 0).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0,0,1).setUv(1, 1).setUv2(15, 15);
//
//        // XZ
//        vc.addVertex(mat, -1, 0, -1).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0,1,0).setUv(0, 1).setUv2(15, 15);
//        vc.addVertex(mat, -1, 0,  1).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0,1,0).setUv(0, 0).setUv2(15, 15);
//        vc.addVertex(mat,  1, 0,  1).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0,1,0).setUv(1, 0).setUv2(15, 15);
//        vc.addVertex(mat,  1, 0, -1).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(0,1,0).setUv(1, 1).setUv2(15, 15);
//
//        // ZY
//        vc.addVertex(mat, 0, -1, -1).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(1,0,0).setUv(0, 1).setUv2(15, 15);
//        vc.addVertex(mat, 0, -1,  1).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(1,0,0).setUv(0, 0).setUv2(15, 15);
//        vc.addVertex(mat, 0,  1,  1).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(1,0,0).setUv(1, 0).setUv2(15, 15);
//        vc.addVertex(mat, 0,  1, -1).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(1,0,0).setUv(1, 1).setUv2(15, 15);
//
//        poseStack.popPose();
//        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
//    }

    @Override
    public ResourceLocation getTextureLocation(BasicProjectileEntity entity) {
        return TEXTURE;
    }
}
