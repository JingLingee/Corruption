package com.Mods.corruption.event;

import net.minecraft.server.world.ServerWorld;

public interface CorruptionEvent {

    void execute(ServerWorld world);

}