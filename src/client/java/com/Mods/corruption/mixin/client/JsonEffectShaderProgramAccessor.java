package com.Mods.corruption.mixin.client;

import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.GlUniform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.util.List;

@Mixin(JsonEffectShaderProgram.class)
public interface JsonEffectShaderProgramAccessor {
    @Accessor("uniformData")
    List<GlUniform> corruption$getUniforms();
}