package com.Mods.corruption.client.shader;

import com.Mods.corruption.mixin.client.GameRendererMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class DistortionShaderManager {
    public static float currentIntensity = 0.0f; // 현재 강도 저장용

    public static void enable(float intensity) {
        currentIntensity = intensity; // 값 업데이트

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.gameRenderer.getPostProcessor() == null) { // 이미 켜져있으면 로드 안 함
            ((GameRendererMixin) client.gameRenderer)
                    .corruption$loadPostProcessor(Identifier.of("corruption", "shaders/post/distortion.json"));
        }
    }

    public static void disable() {
        currentIntensity = 0.0f;
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.gameRenderer != null) {
            client.gameRenderer.disablePostProcessor();


            if (client.gameRenderer.getPostProcessor() != null) {
                client.gameRenderer.getPostProcessor().setupDimensions(
                        client.getWindow().getFramebufferWidth(),
                        client.getWindow().getFramebufferHeight()
                );
            }


        }
    }
}