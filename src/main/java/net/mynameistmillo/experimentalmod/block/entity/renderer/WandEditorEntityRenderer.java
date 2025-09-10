package net.mynameistmillo.experimentalmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.mynameistmillo.experimentalmod.block.entity.custom.WandEditorEntity;

import javax.swing.*;

public class WandEditorEntityRenderer implements BlockEntityRenderer<WandEditorEntity> {
    public WandEditorEntityRenderer(BlockEntityRendererProvider.Context context){

    }

    @Override
    public void render(WandEditorEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = blockEntity.inventory.getStackInSlot(27);

        poseStack.pushPose();
        poseStack.translate(0.5f,1.5f,0.5f);
        poseStack.scale(0.5f,0.5f,0.5f);
        poseStack.mulPose(Axis.XN.rotationDegrees(blockEntity.getRenderingRotation()));
        poseStack.mulPose(Axis.YN.rotationDegrees(blockEntity.getRenderingRotation()));
        poseStack.mulPose(Axis.ZN.rotationDegrees(blockEntity.getRenderingRotation()));

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, getLightLevel(blockEntity.getLevel(),
                blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 1);
        poseStack.popPose();

    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
