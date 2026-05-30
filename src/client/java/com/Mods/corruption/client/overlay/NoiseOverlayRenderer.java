package com.Mods.corruption.client.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class NoiseOverlayRenderer {

    private static int noiseTicks = 0;

    private static final Identifier NOISE_TEXTURE =
            Identifier.of("corruption", "textures/gui/noise.png");


    public static void init() {

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {

            if (noiseTicks > 0) {

                MinecraftClient client = MinecraftClient.getInstance();

                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();

                drawContext.drawTexture(
                        NOISE_TEXTURE,
                        0, 0,
                        0, 0,
                        width, height,
                        width, height
                );

                noiseTicks--;
            }
        });
    }


    public static void trigger(int durationTicks) {
        noiseTicks = durationTicks;
    }
}