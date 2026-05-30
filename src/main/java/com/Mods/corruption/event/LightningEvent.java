package com.Mods.corruption.event;

import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class LightningEvent implements CorruptionEvent {

    private final Random random = new Random();

    @Override
    public void execute(ServerWorld world) {

        if (world.getPlayers().isEmpty()) return;

        var player = world.getPlayers()
                .get(random.nextInt(world.getPlayers().size()));

        BlockPos pos = player.getBlockPos()
                .add(random.nextInt(10) - 5, 0,
                        random.nextInt(10) - 5);

        LightningEntity lightning =
                EntityType.LIGHTNING_BOLT.create(world);

        if (lightning != null) {
            lightning.refreshPositionAfterTeleport(
                    pos.getX(), pos.getY(), pos.getZ()
            );
            world.spawnEntity(lightning);
        }
    }
}