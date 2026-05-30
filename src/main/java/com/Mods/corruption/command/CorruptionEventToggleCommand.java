package com.Mods.corruption.command;

import com.Mods.corruption.event.CorruptionEventManager;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

public class CorruptionEventToggleCommand {

    public static void register() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            dispatcher.register(CommandManager.literal("corruption_event")
                    .requires(source -> source.hasPermissionLevel(2))

                    .then(CommandManager.literal("on")
                            .executes(context -> {

                                CorruptionEventManager.setEnabled(true);

                                context.getSource().sendFeedback(
                                        () -> Text.literal("Corruption events enabled."),
                                        true
                                );

                                return 1;
                            })
                    )

                    .then(CommandManager.literal("off")
                            .executes(context -> {

                                CorruptionEventManager.setEnabled(false);

                                context.getSource().sendFeedback(
                                        () -> Text.literal("Corruption events disabled."),
                                        true
                                );

                                return 1;
                            })
                    )
            );
        });
    }
}