package com.Mods.corruption.command;

import com.Mods.corruption.event.CorruptionEventManager;
import com.Mods.corruption.network.packet.CorruptionSyncPayload;
import com.Mods.corruption.server.CorruptionState;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class CorruptionCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("corruption")
                .requires(source -> source.hasPermissionLevel(2))

                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("value",
                                        FloatArgumentType.floatArg(0f, 100f))
                                .executes(context -> {

                                    float value = FloatArgumentType.getFloat(context, "value");

                                    ServerCommandSource source = context.getSource();
                                    ServerWorld world = source.getWorld();

                                    CorruptionState state =
                                            CorruptionState.get(world.getServer());
                                    state.setCorruptionPercent(value);
                                    world.getServer().getOverworld().save(null, true, false);

                                    source.sendFeedback(() ->
                                                    Text.literal("Corruption set to "
                                                            + value + "%"),
                                            true
                                    );

                                    for (ServerPlayerEntity player : world.getServer().getPlayerManager().getPlayerList()) {
                                        ServerPlayNetworking.send(player, new CorruptionSyncPayload(value));
                                    }

                                    return 1;
                                })
                        )
                )

                .then(CommandManager.literal("check")
                        .executes(context -> {

                            ServerCommandSource source = context.getSource();
                            ServerWorld world = source.getWorld();

                            float corruption = CorruptionState
                                    .get(world.getServer())
                                    .getCorruptionPercent();

                            source.sendFeedback(() ->
                                            Text.literal("Current Corruption: "
                                                    + String.format("%.1f", corruption)
                                                    + "%"),
                                    false
                            );

                            return 1;
                        })
                )
                .then(CommandManager.literal("event")

                        // /corruption event
                        .executes(context -> {

                            ServerWorld world = context.getSource().getWorld();

                            CorruptionEventManager.triggerRandomEvent(world);

                            context.getSource().sendFeedback(
                                    () -> Text.literal("Random corruption event triggered."),
                                    true
                            );

                            return 1;
                        })

                        // /corruption event <name>
                        .then(CommandManager.argument("type",
                                        StringArgumentType.word())
                                .executes(context -> {

                                    String type =
                                            StringArgumentType.getString(context, "type");

                                    ServerWorld world =
                                            context.getSource().getWorld();

                                    CorruptionEventManager
                                            .triggerEventByName(world, type);

                                    context.getSource().sendFeedback(
                                            () -> Text.literal(
                                                    "Triggered event: " + type),
                                            true
                                    );

                                    return 1;
                                })
                        )
                )
        );
    }
}