package com.pulsar.soulforge.entity;

import com.pulsar.soulforge.damage_type.SoulForgeDamageTypes;
import com.pulsar.soulforge.item.SoulForgeItems;
import com.pulsar.soulforge.util.TeamUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DeterminationSpearProjectile extends PersistentProjectileEntity implements GeoEntity {
    public DeterminationSpearProjectile(World world, LivingEntity owner) {
        super(SoulForgeEntities.DETERMINATION_SPEAR_ENTITY_TYPE, owner, world);
        this.pickupType = PickupPermission.ALLOWED;
    }
    public DeterminationSpearProjectile(EntityType<DeterminationSpearProjectile> determinationSpearProjectileEntityType, World world) {
        super(determinationSpearProjectileEntityType, world);
    }

    public boolean canUsePortals() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        if (this.isOnGround()) {
            return null;
        }
        return super.getEntityCollision(currentPosition, nextPosition);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity target && this.getOwner() instanceof PlayerEntity player) {
            if (!TeamUtils.canDamageEntity(this.getServer(), player, target)) return;
        }
        float f = 5.0f;
        DamageSource damageSource = SoulForgeDamageTypes.of(getOwner(), getWorld(), SoulForgeDamageTypes.SUMMON_WEAPON_DAMAGE_TYPE);
        entity.damage(damageSource, f);
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(SoulForgeItems.DETERMINATION_SPEAR);
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (!this.isOwner(player)) {
            super.onPlayerCollision(player);
        }
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "main", 0, (event) -> PlayState.STOP));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
