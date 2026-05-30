package com.Mods.corruption.mixin.client;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererMixin {

    @Invoker("loadPostProcessor")
    void corruption$loadPostProcessor(Identifier id);

    @Accessor("postProcessor")
    PostEffectProcessor corruption$getPostProcessor();


}