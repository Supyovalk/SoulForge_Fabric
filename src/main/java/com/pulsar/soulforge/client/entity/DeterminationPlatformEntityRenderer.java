package com.pulsar.soulforge.client.entity;

import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.client.render.CylinderRenderer;
import com.pulsar.soulforge.entity.DeterminationPlatformEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class DeterminationPlatformEntityRenderer extends EntityRenderer<DeterminationPlatformEntity> {
    public static Identifier TEXTURE = new Identifier(SoulForge.MOD_ID, "textures/item/determination.png");

    public DeterminationPlatformEntityRenderer(Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(DeterminationPlatformEntity platformEntity) {
        return TEXTURE;
    }

    protected int getBlockLight(DeterminationPlatformEntity platformEntity, BlockPos blockPos) {
        return 1;
    }

    public void render(DeterminationPlatformEntity platformEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
        CylinderRenderer.renderCylinder(matrix, vertexConsumer,  new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0.25f, 0f), 1.25f, new Color(1f, 0f, 0f, 0.2f), 0, 255, false);
        if (platformEntity.getStack() >= 1) CylinderRenderer.renderCylinder(matrix, vertexConsumer,  new Vector3f(0f, -0.1f, 0f), new Vector3f(0f, 0.15f, 0f), 1.75f, new Color(1f, 0f, 0f, 0.2f), 0, 255, false);
        if (platformEntity.getStack() == 2) CylinderRenderer.renderCylinder(matrix, vertexConsumer,  new Vector3f(0f, -0.2f, 0f), new Vector3f(0f, 0.05f, 0f), 2.25f, new Color(1f, 0f, 0f, 0.2f), 0, 255, false);
    }
}
