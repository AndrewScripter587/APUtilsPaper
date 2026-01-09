package com.andrewgaming.APUtilsPaper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.ArgumentResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.math.FinePosition;
import org.bukkit.entity.Entity;
import org.bukkit.permissions.Permission;
import org.bukkit.util.Vector;


import java.util.List;

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
                })))));
        return subcommands.build();
    }
}

