package com.Mods.corruption;

import com.Mods.corruption.block.ModBlocks;
import com.Mods.corruption.command.CorruptionEventToggleCommand;
import com.Mods.corruption.entity.CorruptionEntities;
import com.Mods.corruption.event.CorruptionEventManager;
import com.Mods.corruption.item.ModItemGroups;
import com.Mods.corruption.command.CorruptionCommand;
import com.Mods.corruption.network.CorruptionNetworking;
import com.Mods.corruption.server.CorruptionState;
import com.Mods.corruption.server.CorruptionTicker;
import com.Mods.corruption.sound.ModSounds;
import com.Mods.corruption.system.PossessionManager;
import com.Mods.corruption.world.WandererSpawner;
import com.Mods.corruption.world.WatcherSpawner;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import com.Mods.corruption.item.ModItems;

public class Corruption implements ModInitializer {

    public static final String MOD_ID = "corruption";

    @Override
    public void onInitialize() {
        LogManager.getLogger().info(Text.literal("Corruption mod loaded"));
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {

            // CorruptionState 강제 생성
            CorruptionState state = CorruptionState.get(server);

            state.setCorruptionPercent(state.getCorruptionPercent());

            // 월드 강제 저장
            server.getOverworld().save(null, true, false);
        });

        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModItemGroups.registerItemGroups();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CorruptionCommand.register(dispatcher);
        });
        CorruptionEventToggleCommand.register();
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            PossessionManager.tick((ServerWorld) world);
        });

        CorruptionTicker.register();
        CorruptionEventManager.register();
        ModSounds.register();
        CorruptionNetworking.register();

        CorruptionEntities.register();
        WatcherSpawner.register();
        WandererSpawner.register();

    }
}
