package com.pulsar.soulforge.ability.pures;

import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.ability.AbilityType;
import com.pulsar.soulforge.entity.FireTornadoProjectile;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext;

public class ResplendentPhoenix extends AbilityBase {
    @Override
    public boolean cast(ServerPlayerEntity player) {
        BlockHitResult hitResult = player.getWorld().raycast(new RaycastContext(player.getEyePos(), player.getEyePos().add(player.getRotationVector().multiply(40f)), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
        if (hitResult != null) {
            FireTornadoProjectile tornado = new FireTornadoProjectile(player.getWorld(), player, hitResult.getPos());
            tornado.owner = player;
            tornado.setPosition(hitResult.getPos());
            player.getWorld().spawnEntity(tornado);
        }
        return super.cast(player);
    }

    public int getLV() { return 15; }

    public int getCost() { return 80; }

    public int getCooldown() { return 400; }

    public AbilityType getType() { return AbilityType.CAST; }

    @Override
    public AbilityBase getInstance() {
        return new ResplendentPhoenix();
    }
}
