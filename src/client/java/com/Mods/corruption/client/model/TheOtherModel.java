package com.Mods.corruption.client.model;

import com.Mods.corruption.entity.custom.TheOtherEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public class TheOtherModel extends PlayerEntityModel<TheOtherEntity> {

    public TheOtherModel(ModelPart root) {
        super(root, false); // false = 일반(슬림 아님)
    }
}