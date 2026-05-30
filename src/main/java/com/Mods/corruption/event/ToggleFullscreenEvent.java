package com.Mods.corruption.event;

import com.Mods.corruption.network.CorruptionNetworking;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;

import java.util.Random;

public class ToggleFullscreenEvent implements CorruptionEvent{
    private final Random random = new Random();

    @Override
    public void execute(ServerWorld world) {

        if (world.getPlayers().isEmpty()) return;

        var player = world.getPlayers()
                .get(random.nextInt(world.getPlayers().size()));


        CorruptionNetworking.toggleFullscreen(player);
        LogManager.getLogger().info(Text.literal("풀스크린 이벤트 발생됨"));
    }
}
