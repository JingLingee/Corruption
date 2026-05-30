package com.Mods.corruption.item.custom;

import com.Mods.corruption.server.CorruptionState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CorruptionMonitorItem extends Item {

    public CorruptionMonitorItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world,
                                            net.minecraft.entity.player.PlayerEntity user,
                                            Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {
            ServerWorld serverWorld = (ServerWorld) world;
            ServerPlayerEntity player = (ServerPlayerEntity) user;

            float corruption = CorruptionState
                    .get(serverWorld.getServer())
                    .getCorruptionPercent();

            if (corruption < 15f) {
                player.sendMessage(
                        Text.translatable("messages.corruption.quiet"),
                        false
                );
                return TypedActionResult.success(stack);
            }

            player.sendMessage(
                    Text.literal("§5Corruption: §c" +
                            String.format("%.1f", corruption) + "%"),
                    false
            );
        }

        return TypedActionResult.success(stack);
    }
}