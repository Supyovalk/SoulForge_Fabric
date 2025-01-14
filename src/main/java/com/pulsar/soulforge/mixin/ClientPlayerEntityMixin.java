package com.pulsar.soulforge.mixin;

import com.mojang.authlib.GameProfile;
import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.SoulForgeClient;
import com.pulsar.soulforge.components.SoulComponent;
import com.pulsar.soulforge.siphon.Siphon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) { super(world, profile); }

    @Shadow public abstract boolean isUsingItem();

    @Shadow public Input input;

    @Shadow @Final protected MinecraftClient client;

    @Shadow public abstract boolean isSneaking();

    @Inject(method = "dropSelectedItem", at=@At("HEAD"))
    public void dropSelectedItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;
        SoulComponent playerSoul = SoulForgeClient.getPlayerData();
        if (playerSoul != null) {
            if (player.getInventory().selectedSlot == 9) {
                playerSoul.removeWeapon();
            }
        }
    }

    @ModifyConstant(method = "tickMovement", constant = @Constant(floatValue = 0.2f))
    private float modifySlowdown(float value) {
        if (this.isUsingItem() && !this.hasVehicle() && (this.getMainHandStack().isOf(Items.BOW) || this.getMainHandStack().isOf(Items.CROSSBOW) || this.getMainHandStack().isOf(Items.TRIDENT))) {
            if (this.getMainHandStack().getNbt() != null && this.getMainHandStack().getNbt().contains("Siphon")) {
                Siphon.Type type = Siphon.Type.getSiphon(this.getMainHandStack().getNbt().getString("Siphon"));
                if (type == Siphon.Type.INTEGRITY || type == Siphon.Type.SPITE) {
                    return 0.4f;
                }
            }
        }
        return 0.2f;
    }

    @Redirect(method = "tickMovement", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/input/Input;hasForwardMovement()Z"))
    private boolean dualBravtegTickMovement(Input instance) {
        ClientPlayerEntity clientPlayer = (ClientPlayerEntity)(Object)this;
        SoulComponent playerSoul = SoulForge.getPlayerSoul(clientPlayer);
        if (playerSoul != null) {
            if (playerSoul.hasCast("Fearless Instincts")) {
                return Math.abs(instance.movementForward) > 0.01f || Math.abs(instance.movementSideways) > 0.01f;
            }
        }
        return instance.hasForwardMovement();
    }

    @Inject(method = "isWalking", at=@At("HEAD"), cancellable = true)
    private void dualBravtegIsWalking(CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity clientPlayer = (ClientPlayerEntity)(Object)this;
        SoulComponent playerSoul = SoulForge.getPlayerSoul(clientPlayer);
        if (playerSoul != null) {
            if (playerSoul.hasCast("Fearless Instincts")) {
                cir.setReturnValue(Math.abs(this.input.movementForward) > 0.01f || Math.abs(this.input.movementSideways) > 0.01f);
            }
        }
    }
}
