package com.Mods.corruption.event;

import com.Mods.corruption.network.packet.FakeCrashPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

public class FakeCrashEvent implements CorruptionEvent {

    @Override
    public void execute(ServerWorld world) {
        List<ServerPlayerEntity> players = world.getPlayers();
        if (players.isEmpty()) return;
        ServerPlayerEntity target = players.get(world.getRandom().nextInt(players.size()));
        ServerPlayNetworking.send(target, new FakeCrashPayload());
    }
}