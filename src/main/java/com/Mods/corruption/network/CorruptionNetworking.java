package com.Mods.corruption.network;

import com.Mods.corruption.network.packet.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;

public class CorruptionNetworking {

    public static final Identifier NOISE_ID =
            Identifier.of("corruption", "noise_overlay");

    public static final Identifier TOGGLE_FULLSCREEN =
            Identifier.of("corruption", "toggle_fullscreen");

    public static final Identifier SEARCH_STRING =
            Identifier.of("corruption", "search_string");

    public static final Identifier CAMERA_LOCK =
            Identifier.of("corruption", "camera_lock");

    public static final Identifier OVERLAY_INTENSITY =
            Identifier.of("corruption", "overlay_intensity");

    public static final Identifier POSSESSION_ID =
            Identifier.of("corruption", "possession");

    public static final Identifier DISTORTION_ID =
            Identifier.of("corruption", "screen_distortion");


    // ===== 패킷 정의 =====
    public record NoisePayload(int duration) implements CustomPayload {

        public static final Id<NoisePayload> ID =
                new Id<>(NOISE_ID);

        public static final PacketCodec<RegistryByteBuf, NoisePayload> CODEC =
                PacketCodec.of(
                        (value, buf) -> buf.writeInt(value.duration),
                        buf -> new NoisePayload(buf.readInt())
                );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record FullScreenPayload() implements CustomPayload {
        public static final Id<FullScreenPayload> ID =
                new Id<>(TOGGLE_FULLSCREEN);

        public static final PacketCodec<RegistryByteBuf, FullScreenPayload> CODEC =
                PacketCodec.of(
                        (value, buf) -> {},
                        buf -> new FullScreenPayload()
                );


        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record SearchInBrowserPayload(String text) implements CustomPayload {
        public static final Id<SearchInBrowserPayload> ID =
                new Id<>(SEARCH_STRING);

        public static final PacketCodec<RegistryByteBuf, SearchInBrowserPayload> CODEC =
                PacketCodec.of(
                        (value, buf) -> buf.writeString(value.text),
                        buf -> new SearchInBrowserPayload(buf.readString())
                );


        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record OverlayPayload(float intensity) implements CustomPayload {

        public static final Id<OverlayPayload> ID =
                new Id<>(OVERLAY_INTENSITY);

        public static final PacketCodec<RegistryByteBuf, OverlayPayload> CODEC =
                PacketCodec.of(
                        (value, buf) -> buf.writeFloat(value.intensity),
                        buf -> new OverlayPayload(buf.readFloat())
                );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record PossessionPayload(boolean isPossessed) implements CustomPayload {
        public static final Id<PossessionPayload> ID = new Id<>(POSSESSION_ID);

        public static final PacketCodec<RegistryByteBuf, PossessionPayload> CODEC =
                PacketCodec.of(
                        (value, buf) -> buf.writeBoolean(value.isPossessed),
                        buf -> new PossessionPayload(buf.readBoolean())
                );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record DistortionPayload(float intensity) implements CustomPayload {

        public static final Id<DistortionPayload> ID =
                new Id<>(DISTORTION_ID);

        public static final PacketCodec<RegistryByteBuf, DistortionPayload> CODEC =
                PacketCodec.of(
                        (value, buf) -> buf.writeFloat(value.intensity),
                        buf -> new DistortionPayload(buf.readFloat())
                );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }



    // ===== 등록 =====
    public static void register() {
        PayloadTypeRegistry.playS2C().register(NoisePayload.ID, NoisePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(FullScreenPayload.ID, FullScreenPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SearchInBrowserPayload.ID, SearchInBrowserPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(OverlayPayload.ID, OverlayPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PossessionPayload.ID, PossessionPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(DistortionPayload.ID, DistortionPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CorruptionSyncPayload.ID, CorruptionSyncPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(FakeCrashPayload.ID, FakeCrashPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(StalkerFootstepPayload.ID, StalkerFootstepPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(WandererJumpscarePayload.ID, WandererJumpscarePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ChangeWallpaperPayload.ID, ChangeWallpaperPayload.CODEC);
    }

    // ===== 서버 → 클라이언트 전송 =====
    public static void sendNoise(ServerPlayerEntity player, int duration) {
        ServerPlayNetworking.send(player, new NoisePayload(duration));
    }

    public static void toggleFullscreen(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new FullScreenPayload());
        LogManager.getLogger().info(Text.literal("풀스크린 패킷 전송됨"));
    }

    public static void searchstringinbrowser(ServerPlayerEntity player, String string) {
        ServerPlayNetworking.send(player, new SearchInBrowserPayload(string));
    }

    public static void sendOverlay(ServerPlayerEntity player, float intensity) {
        ServerPlayNetworking.send(player, new OverlayPayload(intensity));
    }

    public static void sendPossession(ServerPlayerEntity player, boolean isPossessed) {
        ServerPlayNetworking.send(player, new PossessionPayload(isPossessed));
    }

    public static void sendDistortion(ServerPlayerEntity player, float intensity) {
        ServerPlayNetworking.send(player, new DistortionPayload(intensity));
    }

}