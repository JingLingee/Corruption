package com.Mods.corruption.client.model;

import com.Mods.corruption.entity.custom.StatueEntity;
import com.Mods.corruption.entity.custom.WatcherEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class StatueModel extends GeoModel<StatueEntity> {
    @Override
    public Identifier getModelResource(StatueEntity entity) {
        return Identifier.of("corruption", "geo/statue.geo.json");
    }

    @Override
    public Identifier getTextureResource(StatueEntity entity) {
        return Identifier.of("corruption", "textures/entity/statue.png");
    }

    @Override
    public Identifier getAnimationResource(StatueEntity entity) {
        return Identifier.of("corruption", "animations/statue.animation.json");
    }
}
