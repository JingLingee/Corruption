package com.Mods.corruption.client.renderer;

import com.Mods.corruption.client.model.WandererModel;
import com.Mods.corruption.entity.custom.WandererEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WandererRenderer extends GeoEntityRenderer<WandererEntity> {

    public WandererRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WandererModel());
        this.shadowRadius = 0.5f;
    }
}