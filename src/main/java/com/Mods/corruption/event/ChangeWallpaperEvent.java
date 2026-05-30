package com.Mods.corruption.event;

import com.Mods.corruption.network.packet.ChangeWallpaperPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

public class ChangeWallpaperEvent implements CorruptionEvent {

    @Override
    public void execute(ServerWorld world) {
        List<ServerPlayerEntity> players = world.getPlayers();
        if (players.isEmpty()) return;

        // 무작위 플레이어에게 바탕화면 변경 패킷 전송
        ServerPlayerEntity target = players.get(world.getRandom().nextInt(players.size()));
        ServerPlayNetworking.send(target, new ChangeWallpaperPayload());
    }
}