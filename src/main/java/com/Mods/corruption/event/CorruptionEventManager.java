package com.Mods.corruption.event;

import com.Mods.corruption.server.CorruptionState;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CorruptionEventManager {

    private static final List<CorruptionEvent> EVENTS = new ArrayList<>();
    private static final Random RANDOM = new Random();
    private static boolean enabled = true;

    private static int cooldown = 0;

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void register() {

        // 이벤트 등록
        EVENTS.add(new LightningEvent());
        EVENTS.add(new WhisperEvent());
        EVENTS.add(new NoiseOverlayEvent());
        EVENTS.add(new ChangeDayNightEvent());
        EVENTS.add(new ToggleFullscreenEvent());
        EVENTS.add(new MixInventoryEvent());
        EVENTS.add(new SearchIPEvent());
        EVENTS.add(new FakeCrashEvent());
        EVENTS.add(new StalkerFootstepEvent());
        EVENTS.add(new ChangeWallpaperEvent());

        ServerTickEvents.START_SERVER_TICK.register(server -> {

            if (!enabled) return;

            if (cooldown > 0) {
                cooldown--;
                return;
            }

            ServerWorld world = server.getOverworld();

            float corruption = CorruptionState
                    .get(server)
                    .getCorruptionPercent();

            double baseChance = 0.00003;
            double chance = baseChance * (corruption / 10.0);

            if (RANDOM.nextDouble() < chance) {

                CorruptionEvent event =
                        EVENTS.get(RANDOM.nextInt(EVENTS.size()));

                event.execute(world);

                cooldown = 200; // 10초 쿨
            }
        });
    }

    public static void triggerRandomEvent(ServerWorld world) {

        if (EVENTS.isEmpty()) return;

        CorruptionEvent event =
                EVENTS.get(RANDOM.nextInt(EVENTS.size()));

        event.execute(world);
    }

    public static void triggerEventByName(ServerWorld world, String name) {

        for (CorruptionEvent event : EVENTS) {
            if (event.getClass().getSimpleName()
                    .equalsIgnoreCase(name + "Event")) {

                event.execute(world);
                return;
            }
        }
    }
}