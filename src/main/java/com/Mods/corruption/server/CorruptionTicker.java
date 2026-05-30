package com.Mods.corruption.server;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CorruptionTicker {

    private static long lastDay = -1;
    private static final String STATUE_DIMENSION_ID = "corruption:statue_dimension";

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();

            CorruptionState state = CorruptionState.get(server);

            if (!state.isHelperShown()) {

                state.setHelperShown(true);

                player.sendMessage(Text.translatable("messages.corruption.helper.first_join1"), false);
                player.sendMessage(Text.translatable("messages.corruption.helper.first_join2"), false);
            }
        });
        ServerTickEvents.START_SERVER_TICK.register(server -> {

            ServerWorld world = server.getOverworld();
            long time = world.getTimeOfDay();
            long currentDay = time / 24000L;

            // 서버 시작 첫 틱은 초기화만 (부패도 증가 방지)
            if (lastDay == -1) {
                lastDay = currentDay;
                return;
            }

            if (currentDay != lastDay) {
                lastDay = currentDay;

                CorruptionState state = CorruptionState.get(server);
                float current = state.getCorruptionPercent();
                float newValue = Math.min(current + 2f, 100f);
                state.setCorruptionPercent(newValue);
                state.markDirty();
                System.out.println("Corruption increased to: " + newValue + "%");
            }

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

                String dimId = player.getWorld().getRegistryKey().getValue().toString();

                if (!dimId.equals(STATUE_DIMENSION_ID))
                    continue;

                if (player.getRandom().nextInt(80) != 0)
                    continue;

                double offsetX = (player.getRandom().nextDouble() - 0.5) * 10;
                double offsetZ = (player.getRandom().nextDouble() - 0.5) * 10;

                double x = player.getX() + offsetX;
                double y = player.getY();
                double z = player.getZ() + offsetZ;

                player.getServerWorld().playSound(
                        null,
                        x, y, z,
                        SoundEvents.BLOCK_METAL_PLACE,
                        SoundCategory.HOSTILE,
                        1.5f,
                        0.6f + player.getRandom().nextFloat() * 0.4f
                );
            }
        });
    }
}