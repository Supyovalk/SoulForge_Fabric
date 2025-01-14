package com.pulsar.soulforge.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.components.SoulComponent;
import com.pulsar.soulforge.components.WorldComponent;
import com.pulsar.soulforge.trait.TraitBase;
import com.pulsar.soulforge.trait.Traits;
import com.pulsar.soulforge.util.Utils;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;
import static com.mojang.brigadier.arguments.FloatArgumentType.getFloat;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.pulsar.soulforge.command.SoulForgeCommand.TraitArgumentType.trait;
import static com.pulsar.soulforge.util.Utils.canAccessInverteds;
import static net.minecraft.command.argument.EntityArgumentType.getPlayer;
import static net.minecraft.command.argument.EntityArgumentType.player;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SoulForgeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("overwrite")
                        .requires(source -> source.hasPermissionLevel(4) || !source.isExecutedByPlayer())
                        .then(literal("player")
                                .then(argument("playerName", player())
                                        .then(literal("get")
                                                .then(literal("trait")
                                                        .executes(context -> {
                                                            SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                            String str = Utils.getTraitText(data).getString();
                                                            context.getSource().sendMessage(Text.literal("Your trait is: " + str));
                                                            return 1;
                                                        })
                                                )
                                                .then(literal("lv")
                                                        .executes(context -> {
                                                            SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                            context.getSource().sendMessage(Text.literal("Your LV is: " + data.getLV()));
                                                            return 1;
                                                        })
                                                )
                                                .then(literal("elv")
                                                        .executes(context -> {
                                                            SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                            context.getSource().sendMessage(Text.literal("Your effective LV is: " + data.getEffectiveLV()));
                                                            return 1;
                                                        })
                                                )
                                                .then(literal("exp")
                                                        .executes(context -> {
                                                            SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                            context.getSource().sendMessage(Text.literal("Your EXP is: " + data.getEXP()));
                                                            return 1;
                                                        })
                                                )
                                        ).then(literal("set")
                                                .then(literal("trait")
                                                        .then(argument("trait1", trait())
                                                                .then(argument("trait2", trait())
                                                                        .executes(context -> {
                                                                            TraitBase trait1 = Traits.get(getString(context, "trait1"));
                                                                            TraitBase trait2 = Traits.get(getString(context, "trait2"));
                                                                            if (trait1 == Traits.fear || trait1 == Traits.ineptitude || trait1 == Traits.misery ||
                                                                                trait1 == Traits.anxiety || trait1 == Traits.paranoia || trait1 == Traits.despair) {
                                                                                if (!canAccessInverteds(context)) {
                                                                                    context.getSource().sendMessage(Text.literal("No trait of name " + getString(context, "trait1") + " exists!"));
                                                                                    return 1;
                                                                                }
                                                                            }
                                                                            if (trait2 == Traits.fear || trait2 == Traits.ineptitude || trait2 == Traits.misery ||
                                                                                trait2 == Traits.anxiety || trait2 == Traits.paranoia || trait2 == Traits.despair) {
                                                                                if (!canAccessInverteds(context)) {
                                                                                    context.getSource().sendMessage(Text.literal("No trait of name " + getString(context, "trait2") + " exists!"));
                                                                                    return 1;
                                                                                }
                                                                            }
                                                                            if (trait1 != null && trait2 != null) {
                                                                                SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                                                data.setTraits(List.of(trait1, trait2));
                                                                                context.getSource().sendMessage(Text.literal("Your trait has been changed to: " + Utils.getTraitText(data).getString()));
                                                                            } else if (trait1 != null) {
                                                                                context.getSource().sendMessage(Text.literal("No trait of name " + getString(context, "trait1") + " exists!"));
                                                                            } else if (trait2 != null) {
                                                                                context.getSource().sendMessage(Text.literal("No trait of name " + getString(context, "trait2") + " exists!"));
                                                                            }
                                                                            return 1;
                                                                        })
                                                                )
                                                                .executes(context -> {
                                                                    TraitBase trait1 = Traits.get(getString(context, "trait1"));
                                                                    if (trait1 == Traits.fear || trait1 == Traits.ineptitude || trait1 == Traits.misery ||
                                                                        trait1 == Traits.anxiety || trait1 == Traits.paranoia || trait1 == Traits.despair) {
                                                                        if (!canAccessInverteds(context)) {
                                                                            context.getSource().sendMessage(Text.literal("No trait of name " + getString(context, "trait1") + " exists!"));
                                                                            return 1;
                                                                        }
                                                                    }
                                                                    if (trait1 != null) {
                                                                        SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                                        data.setTraits(List.of(trait1));
                                                                        context.getSource().sendMessage(Text.literal("Your trait has been changed to: " + Utils.getTraitText(data).getString()));
                                                                    } else {
                                                                        context.getSource().sendMessage(Text.literal("No trait of name " + getString(context, "trait1") + " exists!"));
                                                                    }
                                                                    return 1;
                                                                })
                                                        )
                                                )
                                                .then(literal("lv")
                                                        .then(argument("amount", integer(1, 20))
                                                                .executes(context -> {
                                                                    SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                                    data.setLV(getInteger(context, "amount"));
                                                                    context.getSource().sendMessage(Text.literal("Set LV to " + data.getLV()));
                                                                    return 1;
                                                                })
                                                        )
                                                )
                                                .then(literal("exp")
                                                        .then(argument("amount", integer(0))
                                                                .executes(context -> {
                                                                    SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                                    data.setEXP(getInteger(context, "amount"));
                                                                    context.getSource().sendMessage(Text.literal("Set EXP to " + data.getEXP()));
                                                                    return 1;
                                                                })
                                                        )
                                                )
                                                .then(literal("power")
                                                        .then(literal("normal")
                                                                .executes(context -> {
                                                                    SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                                    data.setStrong(false);
                                                                    data.setPure(false);
                                                                    context.getSource().sendMessage(Text.literal("Set your power to NORMAL."));
                                                                    return 1;
                                                                })
                                                        )
                                                        .then(literal("strong")
                                                                .executes(context -> {
                                                                    SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                                    data.setStrong(true);
                                                                    data.setPure(false);
                                                                    context.getSource().sendMessage(Text.literal("Set your power to STRONG."));
                                                                    return 1;
                                                                })
                                                        )
                                                        .then(literal("pure")
                                                                .executes(context -> {
                                                                    SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                                    data.setStrong(true);
                                                                    data.setPure(true);
                                                                    context.getSource().sendMessage(Text.literal("Set your power to PURE."));
                                                                    return 1;
                                                                })
                                                        )
                                                )
                                                .then(literal("styleRank")
                                                        .then(argument("amount", integer(0, 5))
                                                                .executes(context -> {
                                                                    SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                                    data.setStyleRank(getInteger(context, "amount"));
                                                                    data.setStyle(data.getStyleRequirement() - 1);
                                                                    context.getSource().sendMessage(Text.literal("Set style rank to " + data.getStyleRank()));
                                                                    return 1;
                                                                })
                                                        )
                                                )
                                        ).then(literal("reset")
                                                .executes(context -> {
                                                    SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                    data.reset();
                                                    context.getSource().sendMessage(Text.literal("You have been reset!"));
                                                    return 1;
                                                })
                                                .then(literal("discoveredAbilities")
                                                        .executes(context -> {
                                                            SoulComponent data = SoulForge.getPlayerSoul(getPlayer(context, "playerName"));
                                                            data.clearDiscovered();
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                        .then(literal("world")
                                .then(literal("config")
                                        .then(literal("expMultiplier")
                                                .executes(context -> {
                                                    WorldComponent data = SoulForge.getWorldComponent(context.getSource().getWorld());
                                                    context.getSource().sendMessage(Text.literal(String.valueOf(data.getExpMultiplier())));
                                                    return 1;
                                                })
                                                .then(argument("multiplier", floatArg(0f, 1024f))
                                                        .executes(context -> {
                                                            WorldComponent data = SoulForge.getWorldComponent(context.getSource().getWorld());
                                                            data.setExpMultiplier(getFloat(context, "multiplier"));
                                                            context.getSource().sendMessage(Text.literal("Value modified."));
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
        );
    }

    public static class TraitArgumentType implements ArgumentType<String> {
        private TraitArgumentType() {}

        public static TraitArgumentType trait() {
            return new TraitArgumentType();
        }

        public static String getString(final CommandContext<?> context, final String name) {
            return context.getArgument(name, String.class);
        }

        @Override
        public String parse(final StringReader reader) throws CommandSyntaxException {
            return reader.readString();
        }

        @Override
        public String toString() {
            return "trait()";
        }

        @Override
        public Collection<String> getExamples() {
            return Traits.all().stream().map(TraitBase::getName).toList();
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            CommandSource.suggestMatching(Traits.all().stream().map(TraitBase::getName).toList(), builder);
            return builder.buildFuture();
        }
    }
}
