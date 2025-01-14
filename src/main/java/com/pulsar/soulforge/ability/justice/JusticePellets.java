package com.pulsar.soulforge.ability.justice;

import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.ability.AbilityType;
import com.pulsar.soulforge.entity.JusticePelletProjectile;
import com.pulsar.soulforge.sounds.SoulForgeSounds;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.event.GameEvent;

public class JusticePellets extends AbilityBase {
    @Override
    public boolean cast(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        JusticePelletProjectile projectile = new JusticePelletProjectile(world, player);
        projectile.setPos(player.getEyePos());
        projectile.setVelocity(player.getRotationVector().multiply(8));
        projectile.velocityModified = true;
        world.spawnEntity(projectile);
        world.playSoundFromEntity(null, player, SoulForgeSounds.PELLET_SUMMON_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        world.emitGameEvent(GameEvent.ENTITY_PLACE, projectile.getPos(), GameEvent.Emitter.of(projectile));
        return super.cast(player);
    }

    public int getLV() { return 1; }

    public int getCost() { return 4; }

    public int getCooldown() { return 2; }

    public AbilityType getType() { return AbilityType.CAST; }

    @Override
    public AbilityBase getInstance() {
        return new JusticePellets();
    }
}
