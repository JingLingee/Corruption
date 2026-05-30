package com.Mods.corruption.client;

import com.Mods.corruption.client.model.WatcherModel;
import com.Mods.corruption.client.network.ClientNetworking;
import com.Mods.corruption.client.network.ClientPossessionState;
import com.Mods.corruption.client.overlay.DistortionRenderer;
import com.Mods.corruption.client.overlay.NoiseOverlayRenderer;
import com.Mods.corruption.client.overlay.ScreenOverlayRenderer;
import com.Mods.corruption.client.renderer.StatueRenderer;
import com.Mods.corruption.client.renderer.TheOtherRenderer;
import com.Mods.corruption.client.renderer.WandererRenderer;
import com.Mods.corruption.client.renderer.WatcherRenderer;
import com.Mods.corruption.client.shader.DistortionShaderManager;
import com.Mods.corruption.client.system.StalkerSoundManager;
import com.Mods.corruption.client.system.WindowShakeManager;
import com.Mods.corruption.entity.CorruptionEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.option.Perspective;
import net.minecraft.util.math.Vec3d;

public class CorruptionClient implements ClientModInitializer {

    private Perspective savedPerspective = Perspective.FIRST_PERSON;
    private boolean wasPossessed = false;
    private Vec3d lastPos = Vec3d.ZERO;

    @Override
    public void onInitializeClient() {
        ClientNetworking.register();
        NoiseOverlayRenderer.init();
        DistortionRenderer.register();
        ScreenOverlayRenderer.register();
        StalkerSoundManager.register();
        WindowShakeManager.register();

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            boolean isPossessed = ClientPossessionState.possessed;

            // 빙의 시작 감지 (false → true)
            if (isPossessed && !wasPossessed) {
                savedPerspective = client.options.getPerspective();
                client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            }

            // 빙의 해제 감지 (true → false)
            if (!isPossessed && wasPossessed) {
                client.options.setPerspective(savedPerspective);
            }

            wasPossessed = isPossessed;

            if (!isPossessed) {
                lastPos = client.player.getPos();
                return;
            }

            // 키 입력 차단
            client.options.attackKey.setPressed(false);
            client.options.useKey.setPressed(false);
            client.options.forwardKey.setPressed(false);
            client.options.backKey.setPressed(false);
            client.options.leftKey.setPressed(false);
            client.options.rightKey.setPressed(false);
            client.options.jumpKey.setPressed(false);
            client.options.sprintKey.setPressed(false);
            client.options.sneakKey.setPressed(false);

            // 이동 차단
            client.player.input.movementForward = 0;
            client.player.input.movementSideways = 0;
            client.player.input.jumping = false;
            client.player.input.sneaking = false;
            client.player.setSneaking(false);

            Vec3d currentPos = client.player.getPos();
            double moved = currentPos.subtract(lastPos).horizontalLength();
            client.player.limbAnimator.updateLimbs((float) Math.min(moved * 4.0, 1.0), 0.4f);
            lastPos = currentPos;
        });


        EntityRendererRegistry.register(
                CorruptionEntities.PUPPET,
                ctx -> new net.minecraft.client.render.entity.EmptyEntityRenderer<>(ctx)
        );
        EntityRendererRegistry.register(
                CorruptionEntities.WATCHER,
                WatcherRenderer::new
        );

        EntityRendererRegistry.register(CorruptionEntities.WANDERER, WandererRenderer::new);
        EntityRendererRegistry.register(CorruptionEntities.THE_OTHER, TheOtherRenderer::new);
        EntityRendererRegistry.register(CorruptionEntities.STATUE, StatueRenderer ::new);
    }
}