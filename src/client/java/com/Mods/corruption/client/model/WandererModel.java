package com.Mods.corruption.client.model;

import com.Mods.corruption.entity.custom.WandererEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class WandererModel extends GeoModel<WandererEntity> {

    @Override
    public Identifier getModelResource(WandererEntity entity) {
        return Identifier.of("corruption", "geo/wanderer.geo.json");
    }

    @Override
    public Identifier getTextureResource(WandererEntity entity) {
        return Identifier.of("corruption", "textures/entity/wanderer.png");
    }

    @Override
    public Identifier getAnimationResource(WandererEntity entity) {
        return Identifier.of("corruption", "animations/wanderer.animation.json");
    }
}