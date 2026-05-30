package com.Mods.corruption.event;

import com.Mods.corruption.sound.ModSounds;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;

import java.util.Random;

public class WhisperEvent implements CorruptionEvent {

    private final Random random = new Random();

    @Override
    public void execute(ServerWorld world) {

        if (world.getPlayers().isEmpty()) return;

        var player = world.getPlayers()
                .get(random.nextInt(world.getPlayers().size()));

        world.playSound(
                null,                      // 모든 플레이어에게
                player.getBlockPos(),
                ModSounds.WHISPER,
                SoundCategory.AMBIENT,
                1.0f,
                1.0f
        );
    }
}