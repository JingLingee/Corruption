package com.Mods.corruption.client.renderer;

import com.Mods.corruption.client.model.TheOtherModel;
import com.Mods.corruption.entity.custom.TheOtherEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class TheOtherRenderer
        extends MobEntityRenderer<TheOtherEntity, TheOtherModel> {

    private static final Identifier TEXTURE =
            Identifier.of("corruption", "textures/entity/the_other.png");

    public TheOtherRenderer(EntityRendererFactory.Context context) {
        super(context,
                new TheOtherModel(context.getPart(EntityModelLayers.PLAYER)),
                0.0f); // 그림자 제거 (유령 느낌)
    }

    @Override
    public Identifier getTexture(TheOtherEntity entity) {
        return TEXTURE;
    }


}