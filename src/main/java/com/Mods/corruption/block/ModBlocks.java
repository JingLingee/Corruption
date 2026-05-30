package com.Mods.corruption.block;

import com.Mods.corruption.block.custom.CorruptedBlock;
import com.Mods.corruption.block.custom.ReturnBlock;
import com.Mods.corruption.block.custom.StatueBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.block.Block;

public class ModBlocks {

    public static final Block STATUE_BLOCK = register("statue_block", new StatueBlock());
    public static final Block RETURN_BLOCK = register("return_block", new ReturnBlock());
    public static final Block CORRUPTED_BLOCK = register("corrupted_block", new CorruptedBlock());

    private static Block register(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of("corruption", name), block);
    }

    public static void registerModBlocks() {}
}