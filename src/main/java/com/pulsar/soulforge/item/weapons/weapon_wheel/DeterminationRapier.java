package com.pulsar.soulforge.item.weapons.weapon_wheel;

import com.pulsar.soulforge.item.weapons.MagicSwordItem;
import com.pulsar.soulforge.networking.SoulForgeNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class DeterminationRapier extends MagicSwordItem {
    public DeterminationRapier() {
        super(3, 3f, 0.1428f);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            if (user.getServer() != null) {
                PacketByteBuf buf = PacketByteBufs.create().writeUuid(user.getUuid()).writeString("parry");
                buf.writeBoolean(false);
                SoulForgeNetworking.broadcast(null, user.getServer(), SoulForgeNetworking.PERFORM_ANIMATION, buf);
            }
        }
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(this, 20);
            return stack;
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 5;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }
}
