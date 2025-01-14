package com.pulsar.soulforge.entity;

import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.components.SoulComponent;
import com.pulsar.soulforge.damage_type.SoulForgeDamageTypes;
import com.pulsar.soulforge.item.SoulForgeItems;
import com.pulsar.soulforge.util.TeamUtils;
import net.minecraft.enchantment.EnchantmentHelper;
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

public class BraverySpearProjectile extends PersistentProjectileEntity implements GeoEntity {
    private LivingEntity stuckEntity = null;

    public BraverySpearProjectile(World world, LivingEntity owner) {
        super(SoulForgeEntities.BRAVERY_SPEAR_ENTITY_TYPE, owner, world);
        this.pickupType = PickupPermission.ALLOWED;
    }

    public BraverySpearProjectile(EntityType<BraverySpearProjectile> braverySpearProjectileEntityType, World world) {
        super(braverySpearProjectileEntityType, world);
        this.pickupType = PickupPermission.ALLOWED;
    }

    public boolean canUsePortals() {
        return false;
    }

    private int hitCount = 0;

    @Override
    public void tick() {
        if (!this.getWorld().isClient) {
            if (stuckEntity != null) {
                if (this.age % 20 == 0) {
                    DamageSource source;
                    if (getOwner() != null) source = SoulForgeDamageTypes.of(getOwner().getWorld(), SoulForgeDamageTypes.STUCK_SPEAR_DAMAGE_TYPE);
                    else source = SoulForgeDamageTypes.of(getOwner(), this.getWorld(), SoulForgeDamageTypes.STUCK_SPEAR_DAMAGE_TYPE);
                    int timeUntilRegen = stuckEntity.timeUntilRegen;
                    stuckEntity.timeUntilRegen = 0;
                    int maxHitCount = 8;
                    if (getOwner() instanceof PlayerEntity player) {
                        SoulComponent playerSoul = SoulForge.getPlayerSoul(player);
                        maxHitCount = playerSoul.hasCast("Furioso") ? 16 : 8;
                    }
                    if (stuckEntity.damage(source, 2f)) {
                        if (getOwner() instanceof PlayerEntity player) {
                            SoulComponent playerSoul = SoulForge.getPlayerSoul(player);
                            playerSoul.setStyle(playerSoul.getStyle() + 2);
                        }
                    }
                    stuckEntity.timeUntilRegen = timeUntilRegen;
                    hitCount++;
                    if (hitCount == maxHitCount) {
                        this.kill();
                    }
                }
                this.setPosition(stuckEntity.getPos().add(0f, stuckEntity.getHeight()/2f, 0f).add(getRotationVector()));
                this.setVelocity(Vec3d.ZERO);
                if (this.stuckEntity.isDead() || this.stuckEntity.isRemoved()) {
                    this.setNoGravity(false);
                    this.stuckEntity = null;
                }
            }
        }
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
        Entity entity2 = this.getOwner();
        DamageSource damageSource = SoulForgeDamageTypes.of(getOwner(), getWorld(), SoulForgeDamageTypes.SUMMON_WEAPON_DAMAGE_TYPE);
        if (!entity.damage(damageSource, f)) return;
        if (entity.getType() == EntityType.ENDERMAN) return;
        if (!(entity instanceof LivingEntity livingEntity2)) return;
        if (entity2 instanceof LivingEntity) {
            EnchantmentHelper.onUserDamaged(livingEntity2, entity2);
            EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity2);
        }
        stuckEntity = livingEntity2;
        this.setNoGravity(true);
        this.onHit(livingEntity2);
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(SoulForgeItems.BRAVERY_SPEAR);
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
