package com.Mods.corruption.item;

import com.Mods.corruption.Corruption;
import com.Mods.corruption.block.ModBlocks;
import com.Mods.corruption.item.custom.CorruptionMonitorItem;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item CORRUPTION_MONITOR = registerItem(
            "corruption_monitor",
            new CorruptionMonitorItem(new Item.Settings().maxCount(1))
    );

    public static final Item STATUE_BLOCK_ITEM = registerItem("statue_block",
            new BlockItem(ModBlocks.STATUE_BLOCK, new Item.Settings()));

    public static final Item RETURN_BLOCK_ITEM = registerItem("return_block",
            new BlockItem(ModBlocks.RETURN_BLOCK, new Item.Settings()));

    public static final Item CORRUPTED_BLOCK_ITEM = registerItem("corrupted_block",
            new BlockItem(ModBlocks.CORRUPTED_BLOCK, new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(
                Registries.ITEM,
                Identifier.of(Corruption.MOD_ID, name),
                item
        );
    }

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of("corruption", name), item);
    }

    public static void registerModItems() {
    }
}