package com.pulsar.soulforge.client.entity;

import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.client.render.SphereRenderer;
import com.pulsar.soulforge.entity.JusticePelletProjectile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class JusticePelletRenderer extends EntityRenderer<JusticePelletProjectile> {
    private static final Identifier TEXTURE = new Identifier(SoulForge.MOD_ID, "textures/white.png");

    public JusticePelletRenderer(Context context) {
        super(context);
    }

    public void render(JusticePelletProjectile pelletEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(TEXTURE));
        matrixStack.push();
        //matrixStack.peek().getPositionMatrix().setTranslation(0f, 0f, 0f);
        //Vec3d position = pelletEntity.getPos().add(pelletEntity.getVelocity().multiply(tickDelta));
        //matrixStack.translate(position.x/2f, position.y/2f, position.z/2f);
        SphereRenderer.renderSphere(matrixStack.peek().getPositionMatrix(), vertexConsumer, 0.125f, pelletEntity.getDamage() > 0 ? new Color(255, 255, 0) : new Color(0, 255, 0));
        matrixStack.pop();
    }

    public Identifier getTexture(JusticePelletProjectile pelletEntity) {
        return TEXTURE;
    }
}
