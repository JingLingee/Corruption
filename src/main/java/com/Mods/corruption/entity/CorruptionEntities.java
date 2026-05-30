package com.Mods.corruption.entity;

import com.Mods.corruption.entity.custom.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CorruptionEntities {

    public static final EntityType<WatcherEntity> WATCHER =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    Identifier.of("corruption", "watcher"),
                    EntityType.Builder
                            .create(WatcherEntity::new, SpawnGroup.MONSTER)
                            .dimensions(0.6f, 1.8f)
                            .build("watcher")
            );

    public static final EntityType<WandererEntity> WANDERER =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    Identifier.of("corruption", "wanderer"),
                    EntityType.Builder
                            .create(WandererEntity::new, SpawnGroup.MONSTER)
                            .dimensions(1f, 5f)
                            .build("wanderer")
            );

    public static final EntityType<TheOtherEntity> THE_OTHER =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    Identifier.of("corruption", "the_other"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, TheOtherEntity::new)
                            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                            .build()
            );

    // 플레이어 빙의 중 AI 대리자 - 클라이언트에서 렌더링 안 됨 (invisible)
    public static final EntityType<PuppetEntity> PUPPET =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    Identifier.of("corruption", "puppet"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MISC, PuppetEntity::new)
                            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                            .build()
            );

    public static final EntityType<StatueEntity> STATUE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of("corruption", "statue"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, StatueEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f, 2.4f))
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(
                WATCHER,
                WatcherEntity.createAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                WANDERER,
                WandererEntity.createAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                THE_OTHER,
                TheOtherEntity.createAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                PUPPET,
                PuppetEntity.createAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                STATUE,
                StatueEntity.createAttributes()
        );
    }
}