package com.pulsar.soulforge.client.entity;

import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.client.render.CylinderRenderer;
import com.pulsar.soulforge.entity.DTHarpoonProjectile;
import com.pulsar.soulforge.entity.PVHarpoonProjectile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;

@Environment(value=EnvType.CLIENT)
public class DTHarpoonRenderer extends GeoEntityRenderer<DTHarpoonProjectile> {
    public static final Identifier TEXTURE = new Identifier(SoulForge.MOD_ID, "textures/item/determination.png");

    public DTHarpoonRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DTHarpoonModel());
    }

    @Override
    public void render(DTHarpoonProjectile projectile, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (projectile.getOwner() != null) {
            if (projectile.distanceTo(projectile.getOwner()) <= 0.3f) return;
            VertexConsumer consumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(TEXTURE));
            Vector3f offset = projectile.getOwner().getPos().add(0f, 1f, 0f).toVector3f().sub(projectile.getPos().toVector3f());
            CylinderRenderer.renderCylinder(matrixStack.peek().getPositionMatrix(), consumer, new Vector3f(), offset, 0.02f, Color.GRAY, 0, 255, false);
        }
        super.render(projectile, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(DTHarpoonProjectile projectile) {
        return TEXTURE;
    }
}
