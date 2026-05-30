package com.Mods.corruption.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record WandererJumpscarePayload() implements CustomPayload {
    public static final CustomPayload.Id<WandererJumpscarePayload> ID = new CustomPayload.Id<>(Identifier.of("corruption", "wanderer_jumpscare"));

    public static final PacketCodec<RegistryByteBuf, WandererJumpscarePayload> CODEC = PacketCodec.unit(new WandererJumpscarePayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}