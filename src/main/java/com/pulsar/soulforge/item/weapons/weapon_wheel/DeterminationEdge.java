package com.pulsar.soulforge.item.weapons.weapon_wheel;

import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.client.item.GeoMagicItemRenderer;
import com.pulsar.soulforge.components.SoulComponent;
import com.pulsar.soulforge.item.weapons.MagicSweepingSwordItem;
import com.pulsar.soulforge.util.TeamUtils;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DeterminationEdge extends MagicSweepingSwordItem implements GeoItem {
    public DeterminationEdge() {
        // attack damage, attack speed
        super(6f, 1.6f, 0.25f);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        SoulComponent playerSoul = SoulForge.getPlayerSoul((PlayerEntity)attacker);
        for (Entity entity : attacker.getEntityWorld().getOtherEntities(attacker, Box.of(attacker.getPos(), 4, 4, 4))) {
            if (entity instanceof LivingEntity living && living != target) {
                if (entity instanceof PlayerEntity targetPlayer && attacker instanceof PlayerEntity player) {
                    if (!TeamUtils.canDamageEntity(player.getServer(), player, targetPlayer)) continue;
                }
                if (living.damage(attacker.getDamageSources().playerAttack((PlayerEntity)attacker), (this.baseAttackDamage + this.lvIncrease * playerSoul.getLV()))) {
                    playerSoul.setStyle(playerSoul.getStyle() + (int)(this.baseAttackDamage + this.lvIncrease * playerSoul.getLV()));
                }
            }
        }
        return super.postHit(stack, target, attacker);
    }

    public AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private final GeoItemRenderer<DeterminationEdge> renderer = new GeoMagicItemRenderer<>("edge", "determination");

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, (animationState) -> PlayState.STOP));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
