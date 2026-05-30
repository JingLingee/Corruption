package com.Mods.corruption.client.system;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SearchStringInBrowser {

    public static void searchString(String text) {
        try {
            String encoded = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String url = "https://www.google.com/search?q=" + encoded;

            // Minecraft 전용 브라우저 열기 방식
            Util.getOperatingSystem().open(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}