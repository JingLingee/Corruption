package com.Mods.corruption.client.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.util.Random;

public class ScreenOverlayRenderer {

    private static float intensity = 0f;
    private static final Random random = new Random();

    private static final Identifier[] IMAGES = new Identifier[]{
            Identifier.of("corruption", "textures/gui/near.png"),
            Identifier.of("corruption", "textures/gui/trustus.png"),
            Identifier.of("corruption", "textures/gui/whyareyouhere.png")
    };

    public static void setIntensity(float value) {
        float t = Math.max(0f, Math.min(1f, value));
        intensity = t * t; // 가까울수록 급격히 증가
    }

    public static void register() {

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {

            if (intensity <= 0f) return;

            MinecraftClient client = MinecraftClient.getInstance();
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();



            int layers = 35;
            int maxThickness = 250;

            for (int i = 0; i < layers; i++) {

                float progress = (float) i / layers;
                float layerStrength = intensity * (1f - progress);

                int alpha = (int)(layerStrength * 140);
                if (alpha <= 0) continue;

                int color = (alpha << 24) | 0xFF00FF;
                int thickness = (int)(progress * maxThickness);

                drawContext.fill(0, 0, width, thickness, color);
                drawContext.fill(0, height - thickness, width, height, color);
                drawContext.fill(0, 0, thickness, height, color);
                drawContext.fill(width - thickness, 0, width, height, color);
            }



            int count = (int)(intensity * 8); // intensity 높을수록 많아짐

            for (int i = 0; i < count; i++) {


                if (random.nextBoolean()) {

                    Identifier img = IMAGES[random.nextInt(IMAGES.length)];

                    int size = 100 + random.nextInt(60); // 랜덤 크기

                    int x = random.nextInt(Math.max(1, width - size));
                    int y = random.nextInt(Math.max(1, height - size));

                    float alpha = 0.4f + random.nextFloat() * 0.6f;

                    drawContext.setShaderColor(1f, 1f, 1f, alpha);

                    drawContext.drawTexture(
                            img,
                            x,
                            y,
                            0f,
                            0f,
                            size,
                            size,
                            size,
                            size
                    );
                }
            }

            drawContext.setShaderColor(1f, 1f, 1f, 1f);
        });
    }
}