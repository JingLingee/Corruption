package com.Mods.corruption.world;

import com.Mods.corruption.entity.CorruptionEntities;
import com.Mods.corruption.entity.custom.WandererEntity;

import com.Mods.corruption.server.CorruptionState;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;

import net.minecraft.entity.*;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Difficulty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;

public class WandererSpawner {

    public static void register() {

        BiomeModifications.addSpawn(
                BiomeSelectors.foundInOverworld(),
                SpawnGroup.MONSTER,
                CorruptionEntities.WANDERER,
                20, // weight
                1,  // min
                1   // max
        );

        SpawnRestriction.register(
                CorruptionEntities.WANDERER,
                SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                WandererSpawner::canSpawn
        );
    }

    public static boolean canSpawn(
            EntityType<WandererEntity> type,
            ServerWorldAccess world,
            SpawnReason reason,
            BlockPos pos,
            Random random) {


        var serverWorld = world.toServerWorld();

        //Corruption 수치 가져오기
        CorruptionState state = CorruptionState.get(serverWorld.getServer());


        boolean corruptionEnough = state.getCorruptionPercent() >= 25;




        return serverWorld.isNight()
                && corruptionEnough
                && serverWorld.getDifficulty() != Difficulty.PEACEFUL
                && world.getLightLevel(pos) < 7;

    }
}