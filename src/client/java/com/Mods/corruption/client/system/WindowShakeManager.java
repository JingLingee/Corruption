package com.Mods.corruption.client.system;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;

public class WindowShakeManager {
    private static boolean isShaking = false;
    private static int shakeTicks = 0;
    private static int originalX = 0;
    private static int originalY = 0;


    public static void startShake() {
        MinecraftClient client = MinecraftClient.getInstance();
        Window window = client.getWindow();

        if (window.isFullscreen()) {
            window.toggleFullscreen();
        }

        if (!isShaking) {

            originalX = window.getX();
            originalY = window.getY();
            isShaking = true;
            shakeTicks = 30;
        }
    }

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!isShaking || client.world == null) return;

            Window window = client.getWindow();
            long handle = window.getHandle();

            if (shakeTicks > 0) {

                int offsetX = (client.world.random.nextInt(61) - 30);
                int offsetY = (client.world.random.nextInt(61) - 30);

                GLFW.glfwSetWindowPos(handle, originalX + offsetX, originalY + offsetY);
                shakeTicks--;
            } else {

                GLFW.glfwSetWindowPos(handle, originalX, originalY);
                isShaking = false;
            }
        });
    }
}