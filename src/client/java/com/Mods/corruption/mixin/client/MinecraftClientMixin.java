package com.Mods.corruption.mixin.client;

import com.Mods.corruption.client.network.ClientCorruptionState;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "getWindowTitle", at = @At("RETURN"), cancellable = true)
    private void modifyCorruptedTitle(CallbackInfoReturnable<String> cir) {
        if (ClientCorruptionState.getCorruptionPercent() >= 30.0f) {
            cir.setReturnValue("Corrupted");
        }
    }
}