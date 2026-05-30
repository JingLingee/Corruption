package com.Mods.corruption.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PressableWidget.class)
public abstract class PressableWidgetMixin {

    // 문제가 되던 @Shadow 선언은 완전히 삭제합니다.

    @Inject(method = "renderWidget", at = @At("HEAD"))
    private void onRenderWidgetHead(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        // 1. 현재 자신(this)을 부모 클래스인 ClickableWidget으로 강제 변환합니다.
        ClickableWidget widget = (ClickableWidget) (Object) this;

        // 2. 변환된 객체에서 부모의 isHovered() 메서드를 직접 호출합니다.
        if (widget.isHovered()) {
            context.getMatrices().push();

            float shakeX = (float) (Math.random() * 2.0 - 1.0);
            float shakeY = (float) (Math.random() * 2.0 - 1.0);

            context.getMatrices().translate(shakeX, shakeY, 0.0f);
        }
    }

    @Inject(method = "renderWidget", at = @At("TAIL"))
    private void onRenderWidgetTail(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        // 여기도 마찬가지로 변환 후 사용합니다.
        ClickableWidget widget = (ClickableWidget) (Object) this;

        if (widget.isHovered()) {
            context.getMatrices().pop();
        }
    }
}