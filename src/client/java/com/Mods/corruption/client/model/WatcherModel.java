package com.Mods.corruption.client.model;

import com.Mods.corruption.entity.custom.WatcherEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class WatcherModel extends GeoModel<WatcherEntity> {

    @Override
    public Identifier getModelResource(WatcherEntity entity) {
        return Identifier.of("corruption", "geo/watcher.geo.json");
    }

    @Override
    public Identifier getTextureResource(WatcherEntity entity) {
        return Identifier.of("corruption", "textures/entity/watcher.png");
    }

    @Override
    public Identifier getAnimationResource(WatcherEntity entity) {
        return Identifier.of("corruption", "animations/watcher.animation.json");
    }
}