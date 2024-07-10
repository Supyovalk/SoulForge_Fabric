package com.pulsar.soulforge.util;

import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.ability.bravery.BraveryBoost;
import com.pulsar.soulforge.ability.bravery.ValiantHeart;
import com.pulsar.soulforge.ability.determination.DeterminationDome;
import com.pulsar.soulforge.ability.duals.*;
import com.pulsar.soulforge.ability.integrity.RepulsionField;
import com.pulsar.soulforge.ability.justice.JusticeBow;
import com.pulsar.soulforge.ability.justice.JusticeCrossbow;
import com.pulsar.soulforge.ability.justice.JusticeRevolver;
import com.pulsar.soulforge.ability.justice.Launch;
import com.pulsar.soulforge.ability.kindness.AllyHeal;
import com.pulsar.soulforge.ability.kindness.Immobilization;
import com.pulsar.soulforge.ability.kindness.KindnessDome;
import com.pulsar.soulforge.ability.kindness.SelfHeal;
import com.pulsar.soulforge.ability.perseverance.MorphingWeaponry;
import com.pulsar.soulforge.ability.perseverance.PerseveranceAura;
import com.pulsar.soulforge.ability.pures.*;
import com.pulsar.soulforge.components.SoulComponent;
import com.pulsar.soulforge.effects.SoulForgeEffects;
import com.pulsar.soulforge.item.SoulForgeItems;
import com.pulsar.soulforge.trait.TraitBase;
import com.pulsar.soulforge.trait.Traits;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

import java.util.*;

import static java.util.Map.entry;

public class Constants {
    public static List<String> hostiles = new ArrayList<>(List.of(
            "blaze",
            "creeper",
            "cave_spider",
            "drowned",
            "elder_guardian",
            "ender_dragon",
            "enderman",
            "endermite",
            "evoker",
            "ghast",
            "guardian",
            "hoglin",
            "husk",
            "magma_cube",
            "phantom",
            "piglin",
            "piglin_brute",
            "pillager",
            "polar_bear",
            "ravager",
            "shulker",
            "silverfish",
            "skeleton",
            "slime",
            "spider",
            "stray",
            "vex",
            "vindicator",
            "warden",
            "witch",
            "wither",
            "wither_skeleton",
            "zoglin",
            "zombie",
            "zombie_villager",
            "zombified_piglin"
    ));

    public static List<AbilityBase> endsOnDisconnect = new ArrayList<>(List.of(
            new Immobilization(),
            new KindnessDome(),
            new DeterminationDome()
    ));

    public static HashMap<EntityType<?>, Item> essenceDrops = new HashMap<>(Map.ofEntries(
            entry(EntityType.BLAZE, SoulForgeItems.BRAVERY_ESSENCE),
            entry(EntityType.STRIDER, SoulForgeItems.BRAVERY_ESSENCE),
            entry(EntityType.GUARDIAN, SoulForgeItems.JUSTICE_ESSENCE),
            entry(EntityType.AXOLOTL, SoulForgeItems.JUSTICE_ESSENCE),
            entry(EntityType.IRON_GOLEM, SoulForgeItems.KINDNESS_ESSENCE),
            entry(EntityType.TURTLE, SoulForgeItems.KINDNESS_ESSENCE),
            entry(EntityType.SILVERFISH, SoulForgeItems.PATIENCE_ESSENCE),
            entry(EntityType.SNOW_GOLEM, SoulForgeItems.PATIENCE_ESSENCE),
            entry(EntityType.VEX, SoulForgeItems.INTEGRITY_ESSENCE),
            entry(EntityType.ALLAY, SoulForgeItems.INTEGRITY_ESSENCE),
            entry(EntityType.PIGLIN, SoulForgeItems.PERSEVERANCE_ESSENCE),
            entry(EntityType.SNIFFER, SoulForgeItems.PERSEVERANCE_ESSENCE)
    ));

    public static HashMap<StatusEffect, StatusEffect> effectInversion = new HashMap<>(Map.ofEntries(
            entry(StatusEffects.SPEED, StatusEffects.SLOWNESS),
            entry(StatusEffects.SLOWNESS, StatusEffects.SPEED),
            entry(StatusEffects.DOLPHINS_GRACE, StatusEffects.SLOWNESS),
            entry(StatusEffects.STRENGTH, StatusEffects.WEAKNESS),
            entry(StatusEffects.WEAKNESS, StatusEffects.STRENGTH),
            entry(StatusEffects.REGENERATION, StatusEffects.POISON),
            entry(StatusEffects.POISON, StatusEffects.REGENERATION),
            entry(StatusEffects.WITHER, StatusEffects.REGENERATION),
            entry(SoulForgeEffects.VULNERABILITY, StatusEffects.RESISTANCE),
            entry(StatusEffects.RESISTANCE, SoulForgeEffects.VULNERABILITY),
            entry(StatusEffects.LUCK, StatusEffects.UNLUCK),
            entry(StatusEffects.HUNGER, StatusEffects.SATURATION),
            entry(StatusEffects.SATURATION, StatusEffects.HUNGER),
            entry(StatusEffects.HASTE, StatusEffects.MINING_FATIGUE),
            entry(StatusEffects.MINING_FATIGUE, StatusEffects.HASTE),
            entry(StatusEffects.GLOWING, StatusEffects.INVISIBILITY),
            entry(StatusEffects.INVISIBILITY, StatusEffects.GLOWING),
            entry(StatusEffects.JUMP_BOOST, StatusEffects.SLOW_FALLING),
            entry(StatusEffects.SLOW_FALLING, StatusEffects.LEVITATION),
            entry(StatusEffects.LEVITATION, StatusEffects.SLOW_FALLING),
            entry(StatusEffects.NIGHT_VISION, StatusEffects.DARKNESS),
            entry(StatusEffects.DARKNESS, StatusEffects.NIGHT_VISION),
            entry(StatusEffects.BLINDNESS, StatusEffects.NIGHT_VISION)
    ));

    public static HashMap<StatusEffect, Integer> effectHighest = new HashMap<>(Map.ofEntries(
            entry(StatusEffects.ABSORPTION, 3),
            entry(StatusEffects.BAD_OMEN, 4),
            entry(StatusEffects.BLINDNESS, 0),
            entry(StatusEffects.CONDUIT_POWER, 0),
            entry(StatusEffects.DARKNESS, 0),
            entry(StatusEffects.DOLPHINS_GRACE, 2),
            entry(StatusEffects.FIRE_RESISTANCE, 0),
            entry(StatusEffects.GLOWING, 0),
            entry(StatusEffects.HASTE, 1),
            entry(StatusEffects.HEALTH_BOOST, 0),
            entry(StatusEffects.HERO_OF_THE_VILLAGE, 0),
            entry(StatusEffects.HUNGER, 0),
            entry(StatusEffects.INSTANT_DAMAGE, 15),
            entry(StatusEffects.INSTANT_HEALTH, 82),
            entry(StatusEffects.INVISIBILITY, 0),
            entry(StatusEffects.JUMP_BOOST, 1),
            entry(StatusEffects.LEVITATION, 0),
            entry(StatusEffects.LUCK, 69),
            entry(StatusEffects.MINING_FATIGUE, 2),
            entry(StatusEffects.NAUSEA, 0),
            entry(StatusEffects.NIGHT_VISION, 0),
            entry(StatusEffects.POISON, 3),
            entry(StatusEffects.REGENERATION, 1),
            entry(StatusEffects.RESISTANCE, 3),
            entry(StatusEffects.SATURATION, 0),
            entry(StatusEffects.SLOW_FALLING, 1),
            entry(StatusEffects.SLOWNESS, 5),
            entry(StatusEffects.SPEED, 1),
            entry(StatusEffects.STRENGTH, 1),
            entry(StatusEffects.UNLUCK, -1),
            entry(StatusEffects.WATER_BREATHING, 0),
            entry(StatusEffects.WEAKNESS, 0),
            entry(StatusEffects.WITHER, 1)
    ));

    public static void invertStatusEffects(LivingEntity entity, float durationModifier, float amplifierModifier) {
        Collection<StatusEffectInstance> effects = entity.getStatusEffects();
        List<StatusEffectInstance> newEffects = new ArrayList<>();
        if (entity.getFireTicks() > 0) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, (int)(entity.getFireTicks()*amplifierModifier), 0));
        }
        if (entity instanceof PlayerEntity player) {
            SoulComponent playerSoul = SoulForge.getPlayerSoul(player);
            if (playerSoul.hasValue("antiheal") && playerSoul.hasValue("antihealDuration")) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, (int) (playerSoul.getValue("antihealDuration")), (int) (playerSoul.getValue("antiheal") * 0.06f)));
            }
        }
        for (StatusEffectInstance effect : effects) {
            if (effect.getEffectType() == SoulForgeEffects.MANA_SICKNESS) newEffects.add(effect);
            if (!effectInversion.containsKey(effect.getEffectType())) continue;
            newEffects.add(new StatusEffectInstance(effectInversion.get(effect.getEffectType()), (int)(effect.getDuration() * durationModifier), (int)(effect.getAmplifier() * amplifierModifier)));
        }
        entity.clearStatusEffects();
        for (StatusEffectInstance effect : newEffects) {
            entity.addStatusEffect(effect);
        }
    }

    public static void invertStatusEffects(LivingEntity entity, float durationModifier) {
        invertStatusEffects(entity, durationModifier, 0.6f);
    }

    public static void invertStatusEffects(LivingEntity entity) {
        invertStatusEffects(entity, 1f, 0.6f);
    }

    public static boolean isAllowedForDualTrait(AbilityBase ability, List<TraitBase> traits, int lv) {
        if (traits.contains(Traits.bravery) && traits.contains(Traits.integrity)) {
            return !(ability instanceof BraveryBoost) && !(ability instanceof RepulsionField && lv >= 15);
        }
        if (traits.contains(Traits.bravery) && traits.contains(Traits.perseverance)) {
            return !(ability instanceof BraveryBoost) && !(ability instanceof PerseveranceAura) && !(ability instanceof ValiantHeart);
        }
        if (traits.contains(Traits.justice) && traits.contains(Traits.integrity)) {
            return !(ability instanceof RepulsionField && lv >= 15) && !(ability instanceof Launch);
        }
        if (traits.contains(Traits.justice) && traits.contains(Traits.perseverance)) {
            return !(ability instanceof JusticeBow) && !(ability instanceof JusticeCrossbow) && !(ability instanceof JusticeRevolver) && !(ability instanceof MorphingWeaponry);
        }
        if (traits.contains(Traits.kindness) && traits.contains(Traits.perseverance)) {
            return !(ability instanceof AllyHeal) && !(ability instanceof SelfHeal);
        }
        return true;
    }

    public static List<AbilityBase> getDualTraitAbilities(List<TraitBase> traits) {
        List<AbilityBase> extras = new ArrayList<>();
        if ((traits.contains(Traits.bravery) && traits.contains(Traits.justice)) || traits.contains(Traits.spite)) {
            extras.add(new LightningRod());
        }
        if ((traits.contains(Traits.bravery) && traits.contains(Traits.kindness)) || traits.contains(Traits.spite)) {
            extras.add(new HestiasHearth());
        }
        if ((traits.contains(Traits.bravery) && traits.contains(Traits.integrity)) || traits.contains(Traits.spite)) {
            extras.add(new FearlessInstincts());
        }
        if ((traits.contains(Traits.bravery) && traits.contains(Traits.perseverance)) || traits.contains(Traits.spite)) {
            extras.add(new PerfectedAuraTechnique());
            extras.add(new Stockpile());
        }
        if ((traits.contains(Traits.justice) && traits.contains(Traits.kindness)) || traits.contains(Traits.spite)) {
            extras.add(new FriendlinessPellets());
        }
        if ((traits.contains(Traits.justice) && traits.contains(Traits.patience)) || traits.contains(Traits.spite)) {
            extras.add(new Reload());
        }
        if ((traits.contains(Traits.justice) && traits.contains(Traits.integrity)) || traits.contains(Traits.spite)) {
            extras.add(new AcceleratedPelletAura());
        }
        if ((traits.contains(Traits.justice) && traits.contains(Traits.perseverance)) || traits.contains(Traits.spite)) {
            extras.add(new Armory());
        }
        if ((traits.contains(Traits.kindness) && traits.contains(Traits.patience)) || traits.contains(Traits.spite)) {
            extras.add(new StatusInversion());
        }
        if ((traits.contains(Traits.kindness) && traits.contains(Traits.perseverance)) || traits.contains(Traits.spite)) {
            extras.add(new EnduringHeal());
            extras.add(new Nanomachines());
            extras.add(new YourShield());
        }
        if ((traits.contains(Traits.patience) && traits.contains(Traits.integrity)) || traits.contains(Traits.spite)) {
            extras.add(new Wormhole());
            extras.add(new WarpPortal());
        }
        return extras;
    }

    public static HashMap<TraitBase, AbilityBase> pureAbilities = new HashMap<>(Map.ofEntries(
            entry(Traits.patience, new AngelsTempest()),
            entry(Traits.kindness, new MartyrsTouch()),
            entry(Traits.bravery, new ResplendentPhoenix()),
            entry(Traits.justice, new BFRCMG()),
            entry(Traits.integrity, new BoogieWoogie())
    ));
}