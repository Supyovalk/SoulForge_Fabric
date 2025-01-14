package com.pulsar.soulforge.client.ui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.MinecraftClientHttpException;
import com.pulsar.soulforge.networking.SoulForgeNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.minecraft.client.gui.screen.ingame.InventoryScreen.drawEntity;

public class WormholeScreen extends Screen {
    public static final Identifier TEXTURE = new Identifier("soulforge","textures/ui/wormhole_menu.png");

    public int x;
    public int y;

    public UUID selectedUUID;

    public WormholeScreen() {
        super(Text.literal("Wormhole"));
    }

    public List<Drawable> widgets = List.of();

    @Override
    protected void init() {
        this.x = (this.width - 176) / 2;
        this.y = (this.height - 98) / 2;
        updateWidgets();
    }

    public void updateWidgets() {
        widgets = new ArrayList<>();
        clearChildren();

        List<UUID> uuids = this.client.getNetworkHandler().getPlayerUuids().stream().toList();

        int i = 0;
        for (UUID uuid : uuids) {
            if (uuid.compareTo(this.client.player.getUuid()) != 0) {
                ItemStack skull = new ItemStack(Items.PLAYER_HEAD);
                NbtCompound nbt = skull.getOrCreateNbt();
                try {
                    nbt.putString("SkullOwner", this.client.getNetworkHandler().getPlayerListEntry(uuid).getProfile().getName());
                } catch (MinecraftClientHttpException exception) {
                    nbt.putString("SkullOwner", this.client.getNetworkHandler().getPlayerListEntry(uuid).getDisplayName().getString());
                }
                skull.setNbt(nbt);
                SlotButtonWidget slotWidget = new SlotButtonWidget(skull, x + 62+(i%6)*18, y + 8+(MathHelper.floor(i/6f)*18), () -> {
                    if (selectedUUID == uuid) {
                        ClientPlayNetworking.send(SoulForgeNetworking.CAST_WORMHOLE, PacketByteBufs.create().writeUuid(selectedUUID));
                    }
                    selectedUUID = uuid;
                });
                widgets.add(slotWidget);
                addSelectableChild(slotWidget);
                i++;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        assert this.client != null;
        context.drawTexture(TEXTURE, this.x, this.y, 176, 98, 0, 0, 176, 98, 176, 98);
        for (Drawable widget : widgets) {
            widget.render(context, mouseX, mouseY, delta);
        }
        if (selectedUUID != null && this.client.world != null) {
            PlayerEntity selected = new OtherClientPlayerEntity(this.client.world, new GameProfile(selectedUUID, "player"));
            float i = x + 31.5f;
            float j = y + 43;
            drawEntity(context, x + 7, y + 8, 30, i, j, selected);
        }
    }

    public static class SlotButtonWidget extends PressableWidget {
        public final PressAction onPress;
        public final ItemStack stack;

        public SlotButtonWidget(ItemStack stack, int x, int y, PressAction onPress) {
            super(x, y, 18, 18, Text.empty());
            this.onPress = onPress;
            this.stack = stack;
        }

        @Override
        public void onPress() {
            this.onPress.onPress();
        }

        public interface PressAction {
            void onPress();
        }

        @Override
        protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            context.drawItem(this.stack, this.getX(), this.getY());
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {}
    }
}
