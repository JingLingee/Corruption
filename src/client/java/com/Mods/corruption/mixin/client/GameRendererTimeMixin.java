package com.Mods.corruption.mixin.client;

import com.Mods.corruption.client.shader.DistortionShaderManager;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(GameRenderer.class)
public abstract class GameRendererTimeMixin {

    @Shadow
    private PostEffectProcessor postProcessor;

    @Inject(method = "render", at = @At("TAIL"))
    private void corruption$updateShaderTime(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if (postProcessor != null) {
            float time = (float)(System.currentTimeMillis() % 100000L) / 1000.0f;

            List<PostEffectPass> passes = ((PostEffectProcessorAccessor)postProcessor).getPasses();

            for (PostEffectPass pass : passes) {
                var program = ((PostEffectPassAccessor)pass).corruption$getProgram();

                List<GlUniform> uniforms = ((JsonEffectShaderProgramAccessor)program).corruption$getUniforms();

                if (uniforms != null) {
                    for (GlUniform uniform : uniforms) {
                        if ("GameTime".equals(uniform.getName())) {
                            uniform.set(time);
                        }
                        if ("Intensity".equals(uniform.getName())) {
                            uniform.set(DistortionShaderManager.currentIntensity);
                        }
                    }
                }
            }
        }
    }
}