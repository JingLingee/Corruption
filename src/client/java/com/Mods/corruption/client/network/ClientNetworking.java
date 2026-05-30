package com.Mods.corruption.client.network;

import com.Mods.corruption.client.overlay.DistortionRenderer;
import com.Mods.corruption.client.overlay.NoiseOverlayRenderer;
import com.Mods.corruption.client.overlay.ScreenOverlayRenderer;
import com.Mods.corruption.client.screen.FakeDisconnectScreen;
import com.Mods.corruption.client.shader.DistortionShaderManager;
import com.Mods.corruption.client.system.*;
import com.Mods.corruption.network.CorruptionNetworking;
import com.Mods.corruption.network.packet.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;

public class ClientNetworking {

    public static void register() {

        ClientPlayNetworking.registerGlobalReceiver(
                CorruptionNetworking.NoisePayload.ID,
                (payload, context) -> {

                    int duration = payload.duration();

                    context.client().execute(() -> {
                        NoiseOverlayRenderer.trigger(duration);
                    });
                }

        );

        ClientPlayNetworking.registerGlobalReceiver(
                CorruptionNetworking.FullScreenPayload.ID,
                (payload, context) -> {
                    context.client().execute(() -> {
                        // 전체 화면 끄기 실행
                        OffFullScreenMode.turnOffFullScreen();
                        LogManager.getLogger().info(Text.literal("풀스크린 패킷 수신됨"));
                    });
                }
        );

        ClientPlayNetworking.registerGlobalReceiver(
                CorruptionNetworking.SearchInBrowserPayload.ID,
                (payload, context) -> {

                    String text = payload.text();

                    context.client().execute(() -> {
                        SearchStringInBrowser.searchString(text);
                    });
                }

        );

        ClientPlayNetworking.registerGlobalReceiver(
                CorruptionNetworking.OverlayPayload.ID,
                (payload, context) -> {

                    float value = payload.intensity();

                    context.client().execute(() ->
                            ScreenOverlayRenderer.setIntensity(value));
                });

        ClientPlayNetworking.registerGlobalReceiver(
                CorruptionNetworking.PossessionPayload.ID,
                (payload, context) -> {
                    boolean isPossessed = payload.isPossessed();
                    context.client().execute(() -> {
                        // 클라이언트의 빙의 상태 업데이트
                        ClientPossessionState.possessed = isPossessed;
                    });
                }
        );

        ClientPlayNetworking.registerGlobalReceiver(
                CorruptionNetworking.DistortionPayload.ID,
                (payload, context) -> {
                    float value = payload.intensity();
                    context.client().execute(() -> {

                        System.out.println("[디버그] 서버에서 받은 쉐이더 강도: " + value);
                        if (value > 0.01f) {
                            DistortionShaderManager.enable(value); // 강도 전달
                        } else {
                            DistortionShaderManager.disable();
                        }
                    });
                }
        );

        ClientPlayNetworking.registerGlobalReceiver(
                CorruptionSyncPayload.ID, (payload, context) -> {
                    context.client().execute(() -> {
                        ClientCorruptionState.setCorruptionPercent(payload.percent());
                        context.client().updateWindowTitle();
                    });
                }
        );

        ClientPlayNetworking.registerGlobalReceiver(FakeCrashPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                context.client().setScreen(new FakeDisconnectScreen());
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(StalkerFootstepPayload.ID, (payload, context) -> {
            context.client().execute(StalkerSoundManager::start);
        });


        ClientPlayNetworking.registerGlobalReceiver(WandererJumpscarePayload.ID, (payload, context) -> {
            context.client().execute(WindowShakeManager::startShake);
        });

        ClientPlayNetworking.registerGlobalReceiver(ChangeWallpaperPayload.ID, (payload, context) -> {
            context.client().execute(WallpaperManager::changeWallpaper);
        });
    }
}