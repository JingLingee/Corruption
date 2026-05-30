package com.Mods.corruption.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent WHISPER =
            Registry.register(
                    Registries.SOUND_EVENT,
                    Identifier.of("corruption", "whisper"),
                    SoundEvent.of(Identifier.of("corruption", "whisper"))
            );

    public static final SoundEvent WATCHER_HEARTBEAT =
            Registry.register(
                    Registries.SOUND_EVENT,
                    Identifier.of("corruption", "watcher_heartbeat"),
                    SoundEvent.of(Identifier.of("corruption", "watcher_heartbeat"))
            );

    public static final SoundEvent WANDERER_STARING =
            Registry.register(
                    Registries.SOUND_EVENT,
                    Identifier.of("corruption", "wanderer_staring"),
                    SoundEvent.of(Identifier.of("corruption", "wanderer_staring"))
            );

    private static SoundEvent register(String name) {
        Identifier id = Identifier.of("corruption", name);
        return Registry.register(
                Registries.SOUND_EVENT,
                id,
                SoundEvent.of(id)
        );
    }

    public static void register() {}
}