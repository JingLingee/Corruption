package com.Mods.corruption.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CorruptionSyncPayload(float percent) implements CustomPayload {
    public static final Id<CorruptionSyncPayload> ID = new Id<>(Identifier.of("corruption", "sync_corruption"));

    public static final PacketCodec<RegistryByteBuf, CorruptionSyncPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, CorruptionSyncPayload::percent,
            CorruptionSyncPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}