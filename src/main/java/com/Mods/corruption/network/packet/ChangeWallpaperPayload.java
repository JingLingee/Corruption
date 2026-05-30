package com.Mods.corruption.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ChangeWallpaperPayload() implements CustomPayload {
    public static final CustomPayload.Id<ChangeWallpaperPayload> ID = new CustomPayload.Id<>(Identifier.of("corruption", "change_wallpaper"));

    public static final PacketCodec<RegistryByteBuf, ChangeWallpaperPayload> CODEC = PacketCodec.unit(new ChangeWallpaperPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}