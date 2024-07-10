package com.pulsar.soulforge.mixin;

import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.ability.patience.BlindingSnowstorm;
import com.pulsar.soulforge.components.SoulComponent;
import com.pulsar.soulforge.effects.SoulForgeEffects;
import com.pulsar.soulforge.trait.Traits;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method = "hasOutline", at=@At("HEAD"), cancellable = true)
    public void hasOutline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (this.player != null) {
            if (this.player.hasStatusEffect(SoulForgeEffects.SNOWED_VISION)) {
                SoulComponent playerSoul = SoulForge.getPlayerSoul(this.player);
                if (playerSoul.getTraits().contains(Traits.patience) && playerSoul.getTraits().contains(Traits.perseverance)) {
                    if (entity instanceof LivingEntity) {
                        for (AbilityBase ability : playerSoul.getActiveAbilities()) {
                            if (ability instanceof BlindingSnowstorm snowstorm) {
                                if (snowstorm.location.toCenterPos().distanceTo(entity.getPos()) < 140f) {
                                    cir.setReturnValue(true);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}