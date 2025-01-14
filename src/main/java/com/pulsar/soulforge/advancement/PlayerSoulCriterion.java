package com.pulsar.soulforge.advancement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.components.SoulComponent;
import com.pulsar.soulforge.trait.TraitBase;
import com.pulsar.soulforge.trait.Traits;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PlayerSoulCriterion extends AbstractCriterion<PlayerSoulCriterion.Conditions> {
    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate predicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        boolean strong = false;
        boolean pure = false;
        int lv = 1;
        if (obj.has("strong")) strong = obj.get("strong").getAsBoolean();
        if (obj.has("pure")) strong = pure = obj.get("pure").getAsBoolean();
        if (obj.has("lv")) lv = obj.get("lv").getAsInt();
        if (obj.has("trait")) {
            String trait = obj.get("trait").getAsString();
            return new Conditions(trait, strong, pure, lv, getId(), predicate);
        } else if (obj.has("traits")) {
            JsonArray traits = obj.get("traits").getAsJsonArray();
            return new Conditions(traits.get(0).getAsString(), traits.get(1).getAsString(), strong, pure, lv, getId(), predicate);
        } else if (obj.has("count")) {
            int count = obj.get("count").getAsInt();
            return new Conditions(count, strong, pure, lv, getId(), predicate);
        } else {
            return new Conditions(strong, pure, lv, getId(), predicate);
        }
    }

    @Override
    public Identifier getId() {
        return new Identifier(SoulForge.MOD_ID, "player_soul");
    }

    public static class Conditions extends AbstractCriterionConditions {
        TraitBase trait1 = null;
        TraitBase trait2 = null;
        int count = -1;
        int lv = 1;
        boolean strong = false;
        boolean pure = false;

        public Conditions(String traitName, boolean strong, boolean pure, int lv, Identifier id, LootContextPredicate predicate) {
            super(id, predicate);
            this.trait1 = Traits.get(traitName);
            this.strong = strong;
            this.pure = pure;
            this.lv = lv;
        }
        public Conditions(String trait1, String trait2, boolean strong, boolean pure, int lv, Identifier id, LootContextPredicate predicate) {
            super(id, predicate);
            this.trait1 = Traits.get(trait1);
            this.trait2 = Traits.get(trait2);
            this.strong = strong;
            this.pure = pure;
            this.lv = lv;
        }
        public Conditions(int count, boolean strong, boolean pure, int lv, Identifier id, LootContextPredicate predicate) {
            super(id, predicate);
            this.count = count;
            this.strong = strong;
            this.pure = pure;
            this.lv = lv;
        }

        public Conditions(boolean strong, boolean pure, int lv, Identifier id, LootContextPredicate predicate) {
            super(id, predicate);
            this.strong = strong;
            this.pure = pure;
            this.lv = lv;
        }
        boolean requirementsMet(SoulComponent playerSoul) {
            boolean allMatches = lv <= playerSoul.getLV();
            if (strong) allMatches = playerSoul.isStrong() || playerSoul.isPure() || playerSoul.hasTrait(Traits.determination);
            if (pure) allMatches = allMatches && (playerSoul.isPure() || playerSoul.hasTrait(Traits.determination));
            if (trait1 != null) allMatches = allMatches && playerSoul.hasTrait(trait1);
            if (trait2 != null) allMatches = allMatches && playerSoul.hasTrait(trait2);
            if (count != -1) allMatches = allMatches && playerSoul.getTraitCount() >= count;
            return allMatches;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer serializer) {
            JsonObject json = super.toJson(serializer);
            if (trait1 != null) {
                if (trait2 == null) {
                    json.addProperty("trait", trait1.getName());
                } else {
                    JsonArray traits = new JsonArray();
                    traits.add(trait1.getName());
                    traits.add(trait2.getName());
                    json.add("traits", traits);
                }
            } else if (count != -1) {
                json.addProperty("count", count);
            }
            if (strong) json.addProperty("strong", true);
            if (pure) json.addProperty("pure", true);
            if (lv != 1) json.addProperty("lv", lv);
            return json;
        }
    }

    public void trigger(ServerPlayerEntity player, SoulComponent playerSoul) {
        trigger(player, conditions -> conditions.requirementsMet(playerSoul));
    }
}
