package com.Mods.corruption.event;

import com.Mods.corruption.network.CorruptionNetworking;
import net.minecraft.server.world.ServerWorld;

import java.util.Random;

public class NoiseOverlayEvent implements CorruptionEvent{

    private final Random random = new Random();

    @Override
    public void execute(ServerWorld world) {

        if (world.getPlayers().isEmpty()) return;

        var player = world.getPlayers()
                .get(random.nextInt(world.getPlayers().size()));

        CorruptionNetworking.sendNoise(player, 40);
    }
}
