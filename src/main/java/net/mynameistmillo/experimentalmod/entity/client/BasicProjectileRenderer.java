package net.mynameistmillo.experimentalmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.mynameistmillo.experimentalmod.ExperimentalMod;
import net.mynameistmillo.experimentalmod.entity.custom.BasicProjectileEntity;
import org.joml.Matrix4f;
import com.mojang.math.Axis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BasicProjectileRenderer extends EntityRenderer<BasicProjectileEntity> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExperimentalMod.MOD_ID);

    public BasicProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(BasicProjectileEntity entity) {
        return EntityDataTextures.getTxtPathFront("spark_bolt");
    }

    @Override
    public void render(BasicProjectileEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        ResourceLocation side = EntityDataTextures.getTxtPathSide(entity.getProjName());
        ResourceLocation front = EntityDataTextures.getTxtPathFront(entity.getProjName());

        float shiftFrontZ = EntityDataTextures.getFrontAxisShift(entity.getProjName());

        float size = 0.25f;
        poseStack.scale(size, size, size);

        Vec3 motion = entity.getDeltaMovement();
        double vx = motion.x;
        double vy = motion.y;
        double vz = motion.z;

        if (vx * vx + vy * vy + vz * vz < 1e-6) {
            float yawFallback = entity.getYRot();
            float pitchFallback = entity.getXRot();
            poseStack.mulPose(Axis.YP.rotationDegrees(yawFallback));
            poseStack.mulPose(Axis.XP.rotationDegrees(-pitchFallback));
        } else {
            double horizontal = Math.sqrt(vx * vx + vz * vz);
            float yaw = (float) Math.toDegrees(Math.atan2(vx, vz));
            float pitch = (float) Math.toDegrees(Math.atan2(vy, horizontal));

            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));

        }

        float half = 1.0f;

        //front
        poseStack.pushPose();
        poseStack.translate(0.f, 0.0f, shiftFrontZ);
        drawQuad(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(front)),
                packedLight, -half, -half, half, half, 0f);
        poseStack.popPose();

        //side
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90f));
        drawQuad(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(side)),
                packedLight, -half, -half, half, half, 0f);
        poseStack.popPose();

        //top
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90f));
        poseStack.mulPose(Axis.ZN.rotationDegrees(-90f));
        drawQuad(poseStack, buffer.getBuffer(RenderType.entityCutoutNoCull(side)),
                packedLight, -half, -half, half, half, 0f);
        poseStack.popPose();

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private void drawQuad(PoseStack poseStack, VertexConsumer vc, int packedLight, float x1, float y1, float x2, float y2, float z) {
        Matrix4f mat = poseStack.last().pose();
        int packed = packedLight;
        int uv2_low  = packed & 0xFFFF;
        int uv2_high = (packed >> 16) & 0xFFFF;
        vc.addVertex(mat, x1, y1, z)
                .setColor(255,255,255,255)
                .setUv(0f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(uv2_low, uv2_high)
                .setNormal(0f, 0f, 1f);

        vc.addVertex(mat, x1, y2, z)
                .setColor(255,255,255,255)
                .setUv(0f, 0f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(uv2_low, uv2_high)
                .setNormal(0f, 0f, 1f);

        vc.addVertex(mat, x2, y2, z)
                .setColor(255,255,255,255)
                .setUv(1f, 0f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(uv2_low, uv2_high)
                .setNormal(0f, 0f, 1f);

        vc.addVertex(mat, x2, y1, z)
                .setColor(255,255,255,255)
                .setUv(1f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(uv2_low, uv2_high)
                .setNormal(0f, 0f, 1f);

    }
}
