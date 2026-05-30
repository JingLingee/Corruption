package com.Mods.corruption.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;

public class ReturnBlock extends Block {
    public ReturnBlock() {
        super(AbstractBlock.Settings.create()
                .mapColor(MapColor.BLACK)
                .strength(-1.0f)
                .noCollision()
                .requiresTool());
    }

    @Override
    public void onEntityCollision(net.minecraft.block.BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);

        if (!world.isClient && entity instanceof PlayerEntity player) {
            ServerWorld overworld = world.getServer().getOverworld();
            if (overworld == null) return;

            double x = overworld.getSpawnPos().getX() + 0.5;
            double y = overworld.getSpawnPos().getY() + 1.0;
            double z = overworld.getSpawnPos().getZ() + 0.5;

            player.teleport(
                    overworld,
                    x, y, z,
                    java.util.Set.of(),
                    player.getYaw(),
                    player.getPitch()
            );
        }
    }
}