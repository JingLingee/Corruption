package com.Mods.corruption.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;

public class CorruptedBlock extends Block {
    public CorruptedBlock() {
        super(AbstractBlock.Settings.create()
                .mapColor(MapColor.PURPLE)
                .strength(0.5f)
                .requiresTool());
    }
}
