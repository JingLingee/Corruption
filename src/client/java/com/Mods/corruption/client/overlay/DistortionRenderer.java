package com.Mods.corruption.client.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;

import java.util.Random;

public class DistortionRenderer {

    private static float intensity = 0f;
    private static final Random random = new Random();

    public static void setIntensity(float value) {
        intensity = value;
    }

    public static void register() {

        WorldRenderEvents.START.register(context -> {

            if (intensity <= 0f) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            float shake = intensity * 0.6f;

            float yawOffset = (random.nextFloat() - 0.5f) * shake;
            float pitchOffset = (random.nextFloat() - 0.5f) * shake;

            client.player.setYaw(client.player.getYaw() + yawOffset);
            client.player.setPitch(client.player.getPitch() + pitchOffset);
        });
    }
}