package com.Mods.corruption.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;

public class StatueBlock extends Block {

    public StatueBlock() {
        super(AbstractBlock.Settings.create()
                .mapColor(MapColor.STONE_GRAY)
                .strength(3.0f)
                .requiresTool());
    }
}