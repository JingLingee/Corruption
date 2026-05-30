package com.Mods.corruption.world;

import com.Mods.corruption.entity.CorruptionEntities;
import com.Mods.corruption.entity.custom.WatcherEntity;
import com.Mods.corruption.server.CorruptionState;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WatcherSpawner {

    // 플레이어별 1분 쿨타임 저장
    private static final Map<UUID, Long> lastCheckTime = new HashMap<>();

    // 1분 = 1200틱
    private static final long CHECK_INTERVAL = 1200;

    public static void register() {

        ServerTickEvents.END_SERVER_TICK.register(server -> {

            long currentTick = server.getTicks();

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

                if (player.isSpectator()) continue;

                UUID uuid = player.getUuid();

                long lastTick = lastCheckTime.getOrDefault(uuid, 0L);

                // 1분 안 지났으면 스킵
                if (currentTick - lastTick < CHECK_INTERVAL) continue;

                // 마지막 체크 시간 갱신
                lastCheckTime.put(uuid, currentTick);

                float corruption = CorruptionState.get(server).getCorruptionPercent();

                if (corruption >= 15f) {

                    // 기본 3%
                    float chance = 0.03f;

                    // 15 초과분만 계산
                    float extra = (corruption - 15f) * 0.005f;

                    chance += extra;

                    if (player.getRandom().nextFloat() < chance) {
                        spawnWatcher((ServerWorld) player.getWorld(), player);
                    }
                }
            }
        });
    }

    private static void spawnWatcher(ServerWorld world, ServerPlayerEntity player) {

        double distance = 20 + world.random.nextDouble() * 10;
        double angle = world.random.nextDouble() * Math.PI * 2;

        double offsetX = Math.cos(angle) * distance;
        double offsetZ = Math.sin(angle) * distance;

        Vec3d spawnPos = player.getPos().add(offsetX, 20, offsetZ);

        WatcherEntity watcher = new WatcherEntity(CorruptionEntities.WATCHER, world);

        watcher.refreshPositionAndAngles(
                spawnPos.x,
                spawnPos.y,
                spawnPos.z,
                0,
                0
        );

        world.spawnEntity(watcher);
    }
}