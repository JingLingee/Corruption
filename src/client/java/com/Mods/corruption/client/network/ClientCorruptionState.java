package com.Mods.corruption.client.network;

public class ClientCorruptionState {
    private static float corruptionPercent = 0.0f;

    public static void setCorruptionPercent(float value) {
        corruptionPercent = value;
    }

    public static float getCorruptionPercent() {
        return corruptionPercent;
    }
}