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


    @Inject(method = "renderWidget", at = @At("HEAD"))
    private void onRenderWidgetHead(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        ClickableWidget widget = (ClickableWidget) (Object) this;

        if (widget.isHovered()) {
            context.getMatrices().push();

            float shakeX = (float) (Math.random() * 2.0 - 1.0);
            float shakeY = (float) (Math.random() * 2.0 - 1.0);

            context.getMatrices().translate(shakeX, shakeY, 0.0f);
        }
    }

    @Inject(method = "renderWidget", at = @At("TAIL"))
    private void onRenderWidgetTail(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ClickableWidget widget = (ClickableWidget) (Object) this;

        if (widget.isHovered()) {
            context.getMatrices().pop();
        }
    }
}