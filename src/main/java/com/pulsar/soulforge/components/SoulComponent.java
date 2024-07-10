package com.pulsar.soulforge.components;

import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.data.AbilityLayout;
import com.pulsar.soulforge.event.EventType;
import com.pulsar.soulforge.trait.TraitBase;
import com.pulsar.soulforge.util.ResetData;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface SoulComponent extends AutoSyncedComponent, CommonTickingComponent {
    TraitBase getTrait(int num);
    List<TraitBase> getTraits();
    int getTraitCount();
    void setTrait(int num, TraitBase trait);
    void setTraits(List<TraitBase> traits);

    void setResetValues(List<TraitBase> traits, boolean strong, boolean pure);

    ResetData getResetData();
    void setResetData(ResetData resetData);

    List<String> getTags();
    void addTag(String tag);
    boolean hasTag(String tag);
    void removeTag(String tag);

    HashMap<String, Float> getValues();
    float getValue(String value);
    void setValue(String key, float value);
    boolean hasValue(String value);
    void removeValue(String value);

    void handleEvent(EventType type);

    int getLV();
    int getEffectiveLV();
    void setLV(int lv);
    int getEXP();
    void setEXP(int exp);
    int getExpRequirement();
    int getStyle();
    void setStyle(int style);
    int getStyleRequirement();
    int getStyleRank();
    void setStyleRank(int styleRank);
    int getHate();
    void setHate(int hate);
    boolean getInverted();
    void setInverted(boolean inverted);

    float getMagic();
    void setMagic(float magic);

    List<AbilityBase> getAbilities();
    List<AbilityBase> getActiveAbilities();
    AbilityBase getAbility(String abilityName);

    boolean onCooldown(AbilityBase ability);
    boolean onCooldown(String abilityName);
    float cooldownPercent(AbilityBase ability);
    void setCooldown(AbilityBase ability, int cooldown);
    void setCooldown(String abilityName, int cooldown);

    void onDeath();

    int lastCastTime();
    void resetLastCastTime();
    void magicTick();

    boolean hasCast(String abilityName);
    boolean hasAbility(String abilityName);
    boolean hasWeapon();
    ItemStack getWeapon();
    void setWeapon(ItemStack weapon);
    void removeWeapon();
    void setWeapon(ItemStack weapon, boolean sound);
    void removeWeapon(boolean sound);

    HashMap<String, Integer> getMonsterSouls();
    HashMap<String, Integer> getPlayerSouls();
    void addMonsterSoul(String type, int amount);
    void addMonsterSoul(Entity entity, int amount);
    void addPlayerSoul(String playerName, int amount);
    int getSoulCount(String type);
    boolean canReset();

    AbilityLayout getAbilityLayout();
    AbilityBase getLayoutAbility(int row, int column);
    AbilityLayout.AbilityRow getLayoutRow(int row);
    void setAbilityLayout(AbilityLayout layout);
    void setLayoutAbility(AbilityBase ability, int row, int column);
    int getAbilityRow();
    void setAbilityRow(int i);
    int getAbilitySlot();
    void setAbilitySlot(int i);
    void toggleMagicMode();
    boolean magicModeActive();

    String toString();
    void reset();
    void softReset();

    void sync();
    static void sync(PlayerEntity player) {
        EntityInitializer.SOUL.sync(player);
    }

    PacketByteBuf toBuffer();
    void fromBuffer(PacketByteBuf buf);

    void castAbility(int index);
    void castAbility(AbilityBase ability);

    boolean hasDiscovered(AbilityBase ability);
    void discover(AbilityBase ability);
    void undiscover(AbilityBase ability);
    List<AbilityBase> getDiscovered();
    void clearDiscovered();

    boolean isStrong();
    boolean isPure();
    void setStrong(boolean strong);
    void setPure(boolean pure);

    void createWormholeRequest(PlayerEntity from);
    void removeWormholeRequest();
    Pair<UUID, Integer> getWormholeRequest();
    PlayerEntity getWormholeTarget();
    int getWormholeTime();
    boolean hasWormholeRequest();

    void setDisguise(PlayerEntity target);
    void removeDisguise();
    PlayerEntity getDisguise();
}