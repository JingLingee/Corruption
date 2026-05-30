package com.Mods.corruption.client.renderer;

import com.Mods.corruption.client.model.WatcherModel;
import com.Mods.corruption.entity.custom.WatcherEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WatcherRenderer extends GeoEntityRenderer<WatcherEntity> {

    public WatcherRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WatcherModel());
        this.shadowRadius = 0.5f;
    }
}