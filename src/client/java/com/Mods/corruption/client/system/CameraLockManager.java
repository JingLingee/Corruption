package com.Mods.corruption.client.system;

public class CameraLockManager {

    private static boolean locked = false;

    public static void lock() {
        locked = true;
    }

    public static void unlock() {
        locked = false;
    }

    public static boolean isLocked() {
        return locked;
    }
}