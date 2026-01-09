package com.andrewgaming.APUtilsPaper;

import com.destroystokyo.paper.entity.Pathfinder;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.ArgumentResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.math.FinePosition;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.permissions.Permission;
import org.bukkit.util.Vector;


import java.util.List;
import java.util.Objects;

import static com.andrewgaming.APUtilsPaper.APUtilsPaper.GAME_SERVER;
import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static io.papermc.paper.command.brigadier.Commands.*;

public class SetupCommands {
    public static Vector lerpVector(Vector v1, Vector v2, double factor) {
        return v1.add(v2.subtract(v1).multiply(factor));
    }

    public static LiteralCommandNode<CommandSourceStack> registerCommands() {
        LiteralArgumentBuilder<CommandSourceStack> root = literal("aputils");
        LiteralArgumentBuilder<CommandSourceStack> subcommands = root.then(literal("loader").executes(context -> {
            context.getSource().getSender().sendMessage("This command exists only to allow datapacks the knowledge of what version of APUtils is being used. Returns 0 on fabric, 1 on NeoForge, and 2 on Paper.");
            return 2;
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
                );
        
        return subcommands.build();
    }

    public static void pathfind(Mob mob, Vector pos) {
        try {
            mob.getPathfinder().moveTo(pos.toLocation(Objects.requireNonNull(GAME_SERVER.getWorld("overworld"))));
        } catch (NullPointerException ignored) {}
    }

    public static int calcDist(CommandContext<CommandSourceStack> ctx,Vector v1, Vector v2) {
        ctx.getSource().getSender().sendMessage("Distance: " + v1.distance(v2));
        return (int) v1.distance(v2);
    }
    public static int calcDist(CommandContext<CommandSourceStack> ctx,Vector v1, Vector v2, double scale) {
        ctx.getSource().getSender().sendMessage("Distance: " + v1.distance(v2));
        return (int) (v1.distance(v2) * scale);
    }

}

