package com.Mods.corruption.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record StalkerFootstepPayload() implements CustomPayload {
    public static final CustomPayload.Id<StalkerFootstepPayload> ID = new CustomPayload.Id<>(Identifier.of("corruption", "stalker_footstep"));

    public static final PacketCodec<RegistryByteBuf, StalkerFootstepPayload> CODEC = PacketCodec.unit(new StalkerFootstepPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}