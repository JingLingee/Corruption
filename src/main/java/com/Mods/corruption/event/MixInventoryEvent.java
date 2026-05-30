package com.Mods.corruption.event;

import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

import java.util.*;

public class MixInventoryEvent implements CorruptionEvent{

    private final Random random = new Random();

    @Override
    public void execute(ServerWorld world) {
        var player = world.getPlayers()
                .get(random.nextInt(world.getPlayers().size()));


        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < player.getInventory().size(); i++) {
            items.add(player.getInventory().getStack(i));
        }

        Collections.shuffle(items);

        for (int i = 0; i < player.getInventory().size(); i++) {
            player.getInventory().setStack(i, items.get(i));
        }

        player.playerScreenHandler.sendContentUpdates();
    }
}
