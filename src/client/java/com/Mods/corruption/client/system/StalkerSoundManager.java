package com.Mods.corruption.client.system;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class StalkerSoundManager {
    private static boolean isActive = false;
    private static int ticksActive = 0;
    private static float initialYaw = 0;
    private static int nextStepTick = 0;

    public static void start() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            isActive = true;
            ticksActive = 0;
            initialYaw = client.player.getYaw();
            nextStepTick = 5;
        }
    }


    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!isActive || client.player == null || client.world == null) return;

            ticksActive++;
            ClientPlayerEntity player = client.player;

            float yawDiff = Math.abs(MathHelper.wrapDegrees(player.getYaw() - initialYaw));

            if (yawDiff > 50.0f || ticksActive > 200) {
                isActive = false;
                return;
            }

            if (ticksActive >= nextStepTick) {

                Vec3d lookVec = Vec3d.fromPolar(0, player.getYaw());
                double x = player.getX() - (lookVec.x * 2.5);
                double z = player.getZ() - (lookVec.z * 2.5);
                double y = player.getY();


                client.world.playSound(
                        x, y, z,
                        SoundEvents.BLOCK_GRAVEL_STEP,
                        SoundCategory.HOSTILE,
                        1.5f,
                        0.7f + (client.world.random.nextFloat() * 0.2f),
                        false
                );


                nextStepTick = ticksActive + 6 + client.world.random.nextInt(3);
            }
        });
    }
}