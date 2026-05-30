package com.Mods.corruption.item;

import com.Mods.corruption.Corruption;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static ItemGroup CORRUPTION_GROUP;

    public static void registerItemGroups() {

        CORRUPTION_GROUP = Registry.register(
                Registries.ITEM_GROUP,
                Identifier.of(Corruption.MOD_ID, "corruption"),
                FabricItemGroup.builder()
                        .displayName(Text.translatable("itemGroup.corruption"))
                        .icon(() -> ModItems.CORRUPTION_MONITOR.getDefaultStack())
                        .entries((displayContext, entries) -> {

                            entries.add(ModItems.CORRUPTION_MONITOR);
                            entries.add(ModItems.STATUE_BLOCK_ITEM);
                            entries.add(ModItems.RETURN_BLOCK_ITEM);
                            entries.add(ModItems.CORRUPTED_BLOCK_ITEM);

                        })
                        .build()
        );
    }
}