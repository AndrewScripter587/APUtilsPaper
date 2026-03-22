package com.andrewgaming.APUtilsPaper;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.Objects;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static io.papermc.paper.command.brigadier.Commands.*;

public class SetupCommands {
    private static final Logger log = LoggerFactory.getLogger(SetupCommands.class);

    public static Vector lerpVector(Vector v1, Vector v2, double factor) {
        return v1.add(v2.subtract(v1).multiply(factor));
    }

    public static LiteralCommandNode<CommandSourceStack> registerCommands() {
        LiteralArgumentBuilder<CommandSourceStack> root = literal("aputils");
        LiteralArgumentBuilder<CommandSourceStack> subcommands = root.then(literal("loader").executes(context -> {
            context.getSource().getSender().sendMessage("This command exists only to allow datapacks the knowledge of what version of APUtils is being used. Returns 0 on fabric, 1 on NeoForge, 2 on Forge, and 3 on Paper.");
            return 3;
        }))
                .then(literal("velocity").requires(source -> source.getSender().isOp()).then(argument("entities", ArgumentTypes.entities()).then(argument("vector", ArgumentTypes.finePosition(false))
                .executes(context -> {
                    EntitySelectorArgumentResolver entityResolver = context.getArgument("entities", EntitySelectorArgumentResolver.class);
                    FinePositionResolver vectorResolver = context.getArgument("vector", FinePositionResolver.class);
                    Vector vel = vectorResolver.resolve(context.getSource()).toVector();
                    List<Entity> entities = entityResolver.resolve(context.getSource());
                    for (Entity entity : entities) {
                        entity.setVelocity(vel);
                    }
                    return SINGLE_SUCCESS;
                })
                .then(literal("set").executes(context -> {
                    EntitySelectorArgumentResolver entityResolver = context.getArgument("entities", EntitySelectorArgumentResolver.class);
                    FinePositionResolver vectorResolver = context.getArgument("vector", FinePositionResolver.class);
                    Vector vel = vectorResolver.resolve(context.getSource()).toVector();
                    List<Entity> entities = entityResolver.resolve(context.getSource());
                    for (Entity entity : entities) {
                        entity.setVelocity(vel);
                    }
                    return SINGLE_SUCCESS;
                }))
                .then(literal("add").executes(context -> {
                    EntitySelectorArgumentResolver entityResolver = context.getArgument("entities", EntitySelectorArgumentResolver.class);
                    FinePositionResolver vectorResolver = context.getArgument("vector", FinePositionResolver.class);
                    Vector vel = vectorResolver.resolve(context.getSource()).toVector();
                    List<Entity> entities = entityResolver.resolve(context.getSource());
                    for (Entity entity : entities) {
                        entity.setVelocity(entity.getVelocity().add(vel));
                    }
                    return SINGLE_SUCCESS;
                }))
                .then(literal("multiply").executes(context -> {
                    EntitySelectorArgumentResolver entityResolver = context.getArgument("entities", EntitySelectorArgumentResolver.class);
                    FinePositionResolver vectorResolver = context.getArgument("vector", FinePositionResolver.class);
                    Vector vel = vectorResolver.resolve(context.getSource()).toVector();
                    List<Entity> entities = entityResolver.resolve(context.getSource());
                    for (Entity entity : entities) {
                        entity.setVelocity(entity.getVelocity().multiply(vel));
                    }
                    return SINGLE_SUCCESS;
                }))
                .then(literal("divide").executes(context -> {
                    EntitySelectorArgumentResolver entityResolver = context.getArgument("entities", EntitySelectorArgumentResolver.class);
                    FinePositionResolver vectorResolver = context.getArgument("vector", FinePositionResolver.class);
                    Vector vel = vectorResolver.resolve(context.getSource()).toVector();
                    List<Entity> entities = entityResolver.resolve(context.getSource());
                    for (Entity entity : entities) {
                        entity.setVelocity(entity.getVelocity().divide(vel));
                    }
                    return SINGLE_SUCCESS;
                }))
                .then(literal("lerp").then(argument("factor", DoubleArgumentType.doubleArg()).executes(context -> {
                    EntitySelectorArgumentResolver entityResolver = context.getArgument("entities", EntitySelectorArgumentResolver.class);
                    FinePositionResolver vectorResolver = context.getArgument("vector", FinePositionResolver.class);
                    Vector vel = vectorResolver.resolve(context.getSource()).toVector();
                    double factor = DoubleArgumentType.getDouble(context, "factor");
                    List<Entity> entities = entityResolver.resolve(context.getSource());
                    for (Entity entity : entities) {
                        entity.setVelocity(lerpVector(entity.getVelocity(), vel, factor));
                    }
                    return SINGLE_SUCCESS;
                })).executes(context -> {
                    EntitySelectorArgumentResolver entityResolver = context.getArgument("entities", EntitySelectorArgumentResolver.class);
                    FinePositionResolver vectorResolver = context.getArgument("vector", FinePositionResolver.class);
                    Vector vel = vectorResolver.resolve(context.getSource()).toVector();
                    double factor = 0.5;
                    List<Entity> entities = entityResolver.resolve(context.getSource());
                    for (Entity entity : entities) {
                        entity.setVelocity(lerpVector(entity.getVelocity(), vel, factor));
                    }
                    return SINGLE_SUCCESS;
                })))))
                .then(literal("calc")
                        .then(literal("add")
                                .then(argument("value1", DoubleArgumentType.doubleArg())
                                        .then(argument("value2", DoubleArgumentType.doubleArg())
                                                .executes(context -> {
                                                    // For versions below 1.19, replace "Text.literal" with "new LiteralText".
                                                    // For versions below 1.20, remode "() ->" directly.
                                                    final double value1 = DoubleArgumentType.getDouble(context, "value1");
                                                    final double value2 = DoubleArgumentType.getDouble(context, "value2");
                                                    final double result = value1 + value2;
                                                    context.getSource().getSender().sendMessage(value1 + " + " + value2 + " = " + result);

                                                    return (int) result;
                                                })
                                        )
                                ))
                        .then(literal("sub")
                                .then(argument("value1", DoubleArgumentType.doubleArg())
                                        .then(argument("value2", DoubleArgumentType.doubleArg())
                                                .executes(context -> {
                                                    // For versions below 1.19, replace "Text.literal" with "new LiteralText".
                                                    // For versions below 1.20, remode "() ->" directly.
                                                    final double value1 = DoubleArgumentType.getDouble(context, "value1");
                                                    final double value2 = DoubleArgumentType.getDouble(context, "value2");
                                                    final double result = value1 + value2;
                                                    context.getSource().getSender().sendMessage(String.valueOf(value1) + " - " + String.valueOf(value2) + " = " + result);

                                                    return (int) result;
                                                })
                                        )
                                )
                        )
                        .then(literal("mul")
                                .then(argument("value1", DoubleArgumentType.doubleArg())
                                        .then(argument("value2", DoubleArgumentType.doubleArg())
                                                .executes(context -> {
                                                    // For versions below 1.19, replace "Text.literal" with "new LiteralText".
                                                    // For versions below 1.20, remode "() ->" directly.
                                                    final double value1 = DoubleArgumentType.getDouble(context, "value1");
                                                    final double value2 = DoubleArgumentType.getDouble(context, "value2");
                                                    final double result = value1 * value2;
                                                    context.getSource().getSender().sendMessage(String.valueOf(value1) + " * " + String.valueOf(value2) + " = " + result);

                                                    return (int) result;
                                                })
                                        )
                                )
                        )
                        .then(literal("div")
                                .then(argument("value1", DoubleArgumentType.doubleArg())
                                        .then(argument("value2", DoubleArgumentType.doubleArg())
                                                .executes(context -> {
                                                    // For versions below 1.19, replace "Text.literal" with "new LiteralText".
                                                    // For versions below 1.20, remode "() ->" directly.
                                                    final double value1 = DoubleArgumentType.getDouble(context, "value1");
                                                    final double value2 = DoubleArgumentType.getDouble(context, "value2");
                                                    final double result = value1 / value2;
                                                    context.getSource().getSender().sendMessage(String.valueOf(value1) + " / " + String.valueOf(value2) + " = " + result);

                                                    return (int) result;
                                                })
                                        )
                                )
                        )
                        .then(literal("power")
                                .then(argument("value1", DoubleArgumentType.doubleArg())
                                        .then(argument("value2", DoubleArgumentType.doubleArg())
                                                .executes(context -> {
                                                    // For versions below 1.19, replace "Text.literal" with "new LiteralText".
                                                    // For versions below 1.20, remode "() ->" directly.
                                                    final double value1 = DoubleArgumentType.getDouble(context, "value1");
                                                    final double value2 = DoubleArgumentType.getDouble(context, "value2");
                                                    final double result = Math.pow((double) value1, value2);
                                                    context.getSource().getSender().sendMessage(value1 + " ^ " + value2 + " = " + result);

                                                    return (int) result;
                                                })
                                        )
                                )
                        )
                        .then(literal("sqrt")
                                .then(argument("value1", DoubleArgumentType.doubleArg())
                                        .executes(context -> {
                                            // For versions below 1.19, replace "Text.literal" with "new LiteralText".
                                            // For versions below 1.20, remode "() ->" directly.
                                            final double value1 = DoubleArgumentType.getDouble(context, "value1");
                                            final double result = Math.sqrt(value1);
                                            context.getSource().getSender().sendMessage("Sqrt of " + value1 + " = " + result);

                                            return (int) result;
                                        })
                                )
                        )
                        .then(literal("distance")
                                .requires(serverCommandSource -> serverCommandSource.getSender().isOp())
                        .then(argument("pos1", ArgumentTypes.finePosition(true))
                                .then(argument("pos2", ArgumentTypes.finePosition(true))
                                        .executes(context -> calcDist(context, context.getArgument("pos1", FinePositionResolver.class).resolve(context.getSource()).toVector(), context.getArgument("pos2", FinePositionResolver.class).resolve(context.getSource()).toVector()))
                                        .then(argument("scale",DoubleArgumentType.doubleArg())
                                                .executes(context -> calcDist(context, context.getArgument("pos1", FinePositionResolver.class).resolve(context.getSource()).toVector(), context.getArgument("pos2", FinePositionResolver.class).resolve(context.getSource()).toVector(),DoubleArgumentType.getDouble(context,"scale")))
                                        )
                                )
                        ).then(literal("entities")
                                .then(argument("ent1", ArgumentTypes.entity())
                                        .then(argument("ent2", ArgumentTypes.entity())
                                                .executes(context -> calcDist(context, context.getArgument("ent1", EntitySelectorArgumentResolver.class).resolve(context.getSource()).get(0).getLocation().toVector(), context.getArgument("ent2", EntitySelectorArgumentResolver.class).resolve(context.getSource()).get(0).getLocation().toVector()))
                                                .then(argument("scale",DoubleArgumentType.doubleArg())
                                                        .executes(context -> calcDist(context, context.getArgument("ent1", EntitySelectorArgumentResolver.class).resolve(context.getSource()).get(0).getLocation().toVector(), context.getArgument("ent2", EntitySelectorArgumentResolver.class).resolve(context.getSource()).get(0).getLocation().toVector(),DoubleArgumentType.getDouble(context,"scale")))
                                                )
                                        )
                                )
                        )
                        )
                        // .then(literal("pathfind").then(argument("mob",ArgumentTypes.entity()).))
                ).then(literal("attack_cooldown").requires(commandSourceStack -> commandSourceStack.getSender().isOp()).then(argument("player",ArgumentTypes.player()).executes(context -> {
                    Player player = context.getArgument("player",Player.class);
                    float cooldown = player.getAttackCooldown();
                    context.getSource().getSender().sendMessage(Component.text("The attack cooldown of %s is %f".formatted(player.getName(),cooldown)));
                    return (int) (cooldown * 100);
                }))).then(literal("despawn").requires(commandSourceStack -> commandSourceStack.getSender().isOp()).then(argument("entities",ArgumentTypes.entities())
                        .executes(context -> {
                            EntitySelectorArgumentResolver entityResolver = context.getArgument("entities", EntitySelectorArgumentResolver.class);
                            List<Entity> entities = entityResolver.resolve(context.getSource());

                            for (Entity entity : entities) {
                                if (entity instanceof Player) {
                                    context.getSource().getSender().sendMessage("§cPlayers are not allowed, but the provided selector references one or more player(s).");
                                    return -1;
                                }

                            }
                            for (Entity entity : entities) {
                                entity.remove();
                            }
                            context.getSource().getSender().sendMessage("Removed %d ".formatted(entities.size()) + (entities.size() == 1 ? "entity." : "entities."));
                            return 1;
                        })
                        .then(literal("force").executes(context -> {
                            EntitySelectorArgumentResolver entityResolver = context.getArgument("entities", EntitySelectorArgumentResolver.class);
                            List<Entity> entities = entityResolver.resolve(context.getSource());
                            int playerCount = 0;
                            for (Entity entity : entities) {
                                if (entity instanceof Player player) {player.kick(Component.text("Kicked due to player entity despawn"));playerCount++;} else entity.remove();
                            }
                            context.getSource().getSender().sendMessage("Forcefully removed %d ".formatted(entities.size()) + (entities.size() == 1 ? "entity" : "entities") + ", kicking %d player(s)".formatted(playerCount));
                            return 1;
                        }))
                )).then(literal("check_damage").requires(commandSourceStack -> commandSourceStack.getSender().isOp()).then(argument("entity",ArgumentTypes.entity()).executes(context -> {

                    Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();

                        if (entity instanceof LivingEntity livingEntity && livingEntity.getNoDamageTicks() == livingEntity.getMaximumNoDamageTicks()) {
                            context.getSource().getSender().sendMessage("The entity took damage!");
                            return 1;
                        } else {
                            context.getSource().getSender().sendMessage("The entity did not take damage.");
                            return 0;
                        }
                }).then(argument("damage_type",StringArgumentType.string()).executes(context -> {

                    Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                    String type = StringArgumentType.getString(context,"damage_type");
                    if (entity instanceof LivingEntity livingEntity && livingEntity.getNoDamageTicks() == livingEntity.getMaximumNoDamageTicks() && livingEntity.getLastDamageCause().getDamageSource().getDamageType().key().asString().equals(type)) {
                        context.getSource().getSender().sendMessage("The entity took damage of the specified type!");
                        return 1;
                    } else if (entity instanceof LivingEntity livingEntity && livingEntity.getNoDamageTicks() == livingEntity.getMaximumNoDamageTicks() && livingEntity.getLastDamageCause() != null) {
                        context.getSource().getSender().sendMessage("The entity did not take damage of the specified type.");
                        context.getSource().getSender().sendMessage("%s was taken instead.".formatted(livingEntity.getLastDamageCause().getDamageSource().getDamageType().key().asString()));
                        return 0;
                    } else {
                        context.getSource().getSender().sendMessage("The entity did not take damage of the specified type.");
                        return 0;
                    }
                }))))
                .then(literal("pathfind").requires(source -> source.getSender().isOp()).then(argument("mob",ArgumentTypes.entity())
                        .then(argument("pos",ArgumentTypes.blockPosition()).executes(context -> {
                            Entity entity = context.getArgument("mob", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                            BlockPosition pos = context.getArgument("pos", BlockPositionResolver.class).resolve(context.getSource());

                            if (entity instanceof Mob mob) {
                                pathfind(mob,pos.toVector());
                                return 1;
                            } else {
                                return 0;
                            }
                        }).then(argument("speed",DoubleArgumentType.doubleArg()).executes(context -> {
                            Entity entity = context.getArgument("mob", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                            BlockPosition pos = context.getArgument("pos", BlockPositionResolver.class).resolve(context.getSource());
                            double speed = DoubleArgumentType.getDouble(context,"speed");
                            if (entity instanceof Mob mob) {
                                pathfind(mob,pos.toVector(),speed);
                                return 1;
                            } else {
                                return 0;
                            }

                        })))
                        .then(literal("entity").then(argument("target",ArgumentTypes.entity()).executes(context -> {
                            Entity entity = context.getArgument("mob", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                            Entity target = context.getArgument("target", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();

                            if (entity instanceof Mob mob) {
                                pathfind(mob,mob.getLocation().toVector());
                                return 1;
                            } else {
                                return 0;
                            }
                        }).then(argument("speed",DoubleArgumentType.doubleArg()).executes(context -> {
                            Entity entity = context.getArgument("mob", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                            Entity target = context.getArgument("target", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                            double speed = DoubleArgumentType.getDouble(context,"speed");
                            if (entity instanceof Mob mob) {
                                pathfind(mob,target.getLocation().toVector(),speed);
                                return 1;
                            } else {
                                return 0;
                            }

                        }))))
                )).then(literal("target").requires(source -> source.getSender().isOp()).then(argument("mob",ArgumentTypes.entity())
                        .then(argument("target",ArgumentTypes.entity()).executes(context -> {
                            Entity entity = context.getArgument("mob", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                            Entity target = context.getArgument("target", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                            if (entity instanceof Mob mob && target instanceof LivingEntity livingEntity) {
                                mob.setTarget(livingEntity);
                                return 1;
                            } else return 0;
                        })).then(literal("null").executes(context -> {
                            Entity entity = context.getArgument("mob", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                            if (entity instanceof Mob mob) {
                                mob.setTarget(null);

                                return 1;
                            } else return 0;
                        }))
                        )
                ).then(literal("try_attack").requires(source -> source.getSender().isOp()).then(argument("attacker",ArgumentTypes.entity()).then(argument("target",ArgumentTypes.entity())
                        .executes(context -> {
                            Entity entity = context.getArgument("attacker", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                            Entity target = context.getArgument("target", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                            if (entity instanceof LivingEntity livingEntity) {
                                livingEntity.attack(target);
                                return 1;
                            } else return 0;
                        })
                )))

                .then(literal("check_entity").requires(source -> source.getSender().isOp()).then(argument("entity",ArgumentTypes.entity()).then(argument("check",StringArgumentType.string()).executes(context -> {
                    Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                    String check = StringArgumentType.getString(context,"check");
                    boolean success;
                    switch (check) {
                        case "living" -> success = entity instanceof LivingEntity;
                        case "mob" -> success = entity instanceof Mob;
                        case "player" -> success = entity instanceof Player;
                        case "pickup" -> success = entity instanceof Item || entity instanceof ExperienceOrb;
                        case "projectile" -> success = entity instanceof Projectile;
                        case "monster" -> success = entity instanceof Monster;
                        case "hostile","monster_no_neutral" -> success = entity instanceof Monster monster && !isNeutral(monster);
                        case "neutral" -> success = isNeutral(entity);
                        case "animal" -> success = entity instanceof Animals;
                        case "tamable" -> success = entity instanceof Tameable;
                        case "passive", "animal_no_neutral" -> success = entity instanceof Animals && !isNeutral(entity);
                        case "has_navigation", "path_aware" -> success = entity instanceof Mob && !(entity instanceof Slime);
                        case "aquatic", "water_animal" -> success = entity instanceof WaterMob;
                        case "fire_immune" -> success = entity.getMaxFireTicks() <= 0;
                        case "boss" -> success = entity instanceof Wither || entity instanceof EnderDragonPart || entity instanceof EnderDragon;
                        case "explosive" -> success = entity instanceof Creeper || entity instanceof EnderCrystal || entity instanceof Explosive || entity instanceof Firework;
                        case "fireball" -> success = entity instanceof Fireball;
                        case "arrow" -> success = entity instanceof AbstractArrow;
                        case "is_at_full_health","full_hp" -> success = (entity instanceof LivingEntity livingEntity && livingEntity.getHealth() >= Objects.requireNonNull(livingEntity.getAttribute(Attribute.MAX_HEALTH)).getValue());
                        case "is_baby" -> success = (entity instanceof Ageable ageableMob && !ageableMob.isAdult());
                        case "is_adult" -> success = (entity instanceof Ageable ageableMob && ageableMob.isAdult()) || !(entity instanceof Ageable);
                        case null, default -> {
                            context.getSource().getSender().sendMessage(Component.translatable("Unknown entity check: '%s'. Please use a valid check.",check));
                            return -1;
                        }


                    }
                    return success ? 1 : 0;
                }))))
                .then(literal("manipulate").requires(src -> src.getSender().isOp())
                        .then(argument("entity",ArgumentTypes.entity())
                                .then(literal("generic")
                                        .then(literal("extinguish").executes(context -> {
                                            Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                            entity.setFireTicks(0);
                                            context.getSource().getSender().sendMessage(Component.text("Extinguished the entity."));
                                            return 1;
                                        }))
                                        .then(literal("eject_passengers").executes(context -> {
                                            Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                            boolean success = entity.eject();
                                            if (success) {
                                                context.getSource().getSender().sendMessage(Component.text("Ejected any and all passengers off of the entity."));
                                            } else {
                                                context.getSource().getSender().sendMessage(Component.text("Could not eject passengers, there are likely none or something else is preventing ejection."));
                                            }
                                            return success ? 1 : 0;
                                        }))
                                )
                                .then(literal("living_entity")
                                        .then(literal("health").then(argument("value", FloatArgumentType.floatArg())
                                                .executes(context -> {
                                                        Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                        float value = FloatArgumentType.getFloat(context, "value");
                                                        if (entity instanceof LivingEntity livingEntity) {
                                                            livingEntity.setHealth(value);
                                                            context.getSource().getSender().sendMessage(Component.text("Changed health successfully."));
                                                            return (int) livingEntity.getHealth();
                                                        } else return 0;
                                                })
                                                .then(literal("add")
                                                        .executes(context -> {
                                                            Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                            float value = FloatArgumentType.getFloat(context, "value");
                                                            if (entity instanceof LivingEntity livingEntity) {
                                                                livingEntity.setHealth(livingEntity.getHealth() + value);
                                                                context.getSource().getSender().sendMessage(Component.text("Changed health successfully."));
                                                                return (int) livingEntity.getHealth();
                                                            } else return 0;
                                                        })
                                                )
                                                .then(literal("remove")
                                                        .executes(context -> {
                                                            Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                            float value = FloatArgumentType.getFloat(context, "value");
                                                            if (entity instanceof LivingEntity livingEntity) {
                                                                livingEntity.setHealth(livingEntity.getHealth() - value);
                                                                context.getSource().getSender().sendMessage(Component.text("Changed health successfully."));
                                                                return (int) livingEntity.getHealth();
                                                            } else return 0;
                                                        })
                                                )
                                                .then(literal("multiply")
                                                        .executes(context -> {
                                                            Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                            float value = FloatArgumentType.getFloat(context, "value");
                                                            if (entity instanceof LivingEntity livingEntity) {
                                                                livingEntity.setHealth(livingEntity.getHealth() * value);
                                                                context.getSource().getSender().sendMessage(Component.text("Changed health successfully."));
                                                                return (int) livingEntity.getHealth();
                                                            } else return 0;
                                                        })
                                                )
                                                .then(literal("divide")
                                                        .executes(context -> {
                                                            Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                            float value = FloatArgumentType.getFloat(context, "value");
                                                            if (entity instanceof LivingEntity livingEntity) {
                                                                livingEntity.setHealth(livingEntity.getHealth() / value);
                                                                context.getSource().getSender().sendMessage(Component.text("Changed health successfully."));
                                                                return (int) livingEntity.getHealth();
                                                            } else return 0;
                                                        })
                                                )

                                        ))
                                        .then(literal("heal").then(argument("value", FloatArgumentType.floatArg()).executes(context -> {
                                            Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                            float value = FloatArgumentType.getFloat(context, "value");
                                            if (entity instanceof LivingEntity livingEntity) {
                                                livingEntity.heal(value, EntityRegainHealthEvent.RegainReason.CUSTOM);
                                                context.getSource().getSender().sendMessage(Component.text("Healed successfully."));
                                                return (int) livingEntity.getHealth();
                                            } else return 0;

                                        })))
                                )
                                .then(literal("player")
                                        .then(literal("food_level").then(argument("value", IntegerArgumentType.integer()))
                                                .executes(context -> {
                                                    Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                    int value = IntegerArgumentType.getInteger(context, "value");
                                                    if (entity instanceof Player player) {
                                                        player.setFoodLevel(value);
                                                        context.getSource().getSender().sendMessage(Component.text("Changed food level successfully."));
                                                        return player.getFoodLevel();
                                                    } else return 0;
                                                })
                                                .then(literal("add").executes(context -> {
                                                            Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                            int value = IntegerArgumentType.getInteger(context, "value");
                                                            if (entity instanceof Player player) {
                                                                player.setFoodLevel(player.getFoodLevel() + value);
                                                                context.getSource().getSender().sendMessage(Component.text("Changed food level successfully."));
                                                                return player.getFoodLevel();
                                                            } else return 0;
                                                }))
                                                .then(literal("remove").executes(context -> {
                                                    Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                    int value = IntegerArgumentType.getInteger(context, "value");
                                                    if (entity instanceof Player player) {
                                                        player.setFoodLevel(player.getFoodLevel() - value);
                                                        context.getSource().getSender().sendMessage(Component.text("Changed food level successfully."));
                                                        return player.getFoodLevel();
                                                    } else return 0;
                                                }))
                                                .then(literal("multiply").executes(context -> {
                                                    Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                    int value = IntegerArgumentType.getInteger(context, "value");
                                                    if (entity instanceof Player player) {
                                                        player.setFoodLevel(player.getFoodLevel() * value);
                                                        context.getSource().getSender().sendMessage(Component.text("Changed food level successfully."));
                                                        return player.getFoodLevel();
                                                    } else return 0;
                                                }))
                                                .then(literal("divide").executes(context -> {
                                                    Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                    int value = IntegerArgumentType.getInteger(context, "value");
                                                    if (entity instanceof Player player) {
                                                        player.setFoodLevel(player.getFoodLevel() / value);
                                                        context.getSource().getSender().sendMessage(Component.text("Changed food level successfully."));
                                                        return player.getFoodLevel();
                                                    } else return 0;
                                                }))

                                        )
                                        .then(literal("food_saturation_level").then(argument("value",FloatArgumentType.floatArg()))
                                                .executes(context -> {
                                                    Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                    float value = FloatArgumentType.getFloat(context, "value");
                                                    if (entity instanceof Player player) {
                                                        player.setSaturation(value);
                                                        context.getSource().getSender().sendMessage(Component.text("Changed food saturation successfully."));
                                                        return (int) player.getSaturation();
                                                    } else return 0;
                                                })
                                                .then(literal("add").executes(context -> {
                                                    Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                    float value = FloatArgumentType.getFloat(context, "value");
                                                    if (entity instanceof Player player) {
                                                        player.setSaturation(player.getSaturation() + value);
                                                        context.getSource().getSender().sendMessage(Component.text("Changed food saturation successfully."));
                                                        return (int) player.getSaturation();
                                                    } else return 0;
                                                }))
                                                .then(literal("remove").executes(context -> {
                                                    Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                    float value = FloatArgumentType.getFloat(context, "value");
                                                    if (entity instanceof Player player) {
                                                        player.setSaturation(player.getSaturation() - value);
                                                        context.getSource().getSender().sendMessage(Component.text("Changed food saturation successfully."));
                                                        return (int) player.getSaturation();
                                                    } else return 0;
                                                }))
                                                .then(literal("multiply").executes(context -> {
                                                    Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                    float value = FloatArgumentType.getFloat(context, "value");
                                                    if (entity instanceof Player player) {
                                                        player.setSaturation(player.getSaturation() * value);
                                                        context.getSource().getSender().sendMessage(Component.text("Changed food saturation successfully."));
                                                        return (int) player.getSaturation();
                                                    } else return 0;
                                                }))
                                                .then(literal("divide").executes(context -> {
                                                    Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                    float value = FloatArgumentType.getFloat(context, "value");
                                                    if (entity instanceof Player player) {
                                                        player.setSaturation(player.getSaturation() / value);
                                                        context.getSource().getSender().sendMessage(Component.text("Changed food saturation successfully."));
                                                        return (int) player.getSaturation();
                                                    } else return 0;
                                                }))

                                        )
                                        .then(literal("add_exhaustion").then(argument("value",FloatArgumentType.floatArg())
                                                .executes(context -> {
                                                    Entity entity = context.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                    float value = FloatArgumentType.getFloat(context, "value");
                                                    if (entity instanceof Player player) {
                                                        player.setExhaustion(player.getExhaustion() + value);
                                                        context.getSource().getSender().sendMessage(Component.text("Increased exhaustion successfully."));
                                                        return (int) player.getExhaustion();
                                                    } else return 0;
                                                })
                                        ))
                                )
                                .then(literal("ender_dragon")
                                        .then(literal("fly_speed").then(argument("value",DoubleArgumentType.doubleArg()).executes(context -> {
                                            context.getSource().getSender().sendMessage(Component.text("Unavailable on Paper."));
                                            return 0;
                                        })))
                                        .then(literal("vertical_speed").then(argument("value",DoubleArgumentType.doubleArg()).executes(context -> {
                                            context.getSource().getSender().sendMessage(Component.text("Unavailable on Paper."));
                                            return 0;
                                        })))

                                )
                        )

                );
        
        return subcommands.build();
    }

    public static void pathfind(Mob mob, Vector pos) {
        try {
            mob.getPathfinder().moveTo(pos.toLocation(mob.getLocation().getWorld()));
        } catch (NullPointerException e) {
            log.error("error: ", e);
        }
    }
    public static void pathfind(Mob mob, Vector pos, double speed) {
        try {

            mob.getPathfinder().moveTo(pos.toLocation(mob.getLocation().getWorld()),speed);
        } catch (NullPointerException e) {
            log.error("error: ", e);
        }
    }

    public static int calcDist(CommandContext<CommandSourceStack> ctx,Vector v1, Vector v2) {
        ctx.getSource().getSender().sendMessage("Distance: " + v1.distance(v2));
        return (int) v1.distance(v2);
    }
    public static int calcDist(CommandContext<CommandSourceStack> ctx,Vector v1, Vector v2, double scale) {
        ctx.getSource().getSender().sendMessage("Distance: " + v1.distance(v2));
        return (int) (v1.distance(v2) * scale);
    }
    public static boolean isNeutral(Entity entity) {
        return (entity instanceof PigZombie || entity instanceof Enderman || entity instanceof Bee || entity instanceof Wolf);
    }
}

