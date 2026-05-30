package com.Mods.corruption.entity;

import com.Mods.corruption.entity.custom.WatcherEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class CorruptionEntitySpawner {

    public static void spawnWatcher(ServerPlayerEntity player) {

        WatcherEntity watcher =
                CorruptionEntities.WATCHER.create(player.getWorld());

        if (watcher == null) return;

        double distance = 20;
        double angle = player.getRandom().nextDouble() * Math.PI * 2;

        double x = player.getX() + Math.cos(angle) * distance;
        double z = player.getZ() + Math.sin(angle) * distance;

        watcher.refreshPositionAndAngles(
                x,
                player.getY(),
                z,
                0,
                0
        );

        player.getWorld().spawnEntity(watcher);
    }
}