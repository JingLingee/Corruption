package com.Mods.corruption.client.system;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.Window;

public class OffFullScreenMode {

    public static void turnOffFullScreen() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            GameOptions options = client.options;
            Window window = client.getWindow();

            // 현재 전체 화면이면 toggleFullscreen 사용
            if (options != null && window != null && window.isFullscreen()) {
                window.toggleFullscreen();
            }
        }
    }
}