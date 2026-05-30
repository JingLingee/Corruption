package com.Mods.corruption.event;

import net.minecraft.server.world.ServerWorld;

import java.util.Random;

public class ChangeDayNightEvent implements CorruptionEvent{

    private final Random random = new Random();

    @Override
    public void execute(ServerWorld world) {

        long time = world.getTimeOfDay(); // 현재 월드 시간 가져오기
        if (time < 12000) {
            // 낮이면 밤으로
            world.setTimeOfDay(18000); // 밤 시작 시간
        } else {
            // 밤이면 낮으로
            world.setTimeOfDay(1000); // 낮 시작 시간
        }
    }
}
