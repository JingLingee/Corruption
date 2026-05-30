package com.Mods.corruption.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record FakeCrashPayload() implements CustomPayload {
    public static final CustomPayload.Id<FakeCrashPayload> ID = new CustomPayload.Id<>(Identifier.of("corruption", "fake_crash"));

    public static final PacketCodec<RegistryByteBuf, FakeCrashPayload> CODEC = PacketCodec.unit(new FakeCrashPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}