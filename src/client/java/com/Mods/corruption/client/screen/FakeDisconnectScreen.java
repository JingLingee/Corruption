package com.Mods.corruption.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.sound.SoundEvents;

public class FakeDisconnectScreen extends Screen {
    private int ticks = 0;

    public FakeDisconnectScreen() {
        super(Text.translatable("menu.disconnect"));
    }

    @Override
    public boolean shouldCloseOnEsc() {

        return false;
    }

    @Override
    protected void init() {
        super.init();
        // 가짜 '타이틀 화면으로' 버튼 추가
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.toMenu"), button -> {
            // 버튼을 클릭해도 아무 반응 없이 기괴한 버튼 클릭 소리만 재생
            if (this.client != null && this.client.player != null) {
                this.client.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 0.5F);
            }
        }).dimensions(this.width / 2 - 100, this.height / 2 + 30, 200, 20).build());
    }

    @Override
    public void tick() {
        super.tick();
        ticks++;


        if (ticks > 170) {
            if (this.client != null) {
                this.client.setScreen(null); // 화면 닫기


            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        context.fill(0, 0, this.width, this.height, 0xFF111111);

        super.render(context, mouseX, mouseY, delta);

        if (ticks < 120) {

            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 40, 0xFFFFFF);
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Internal Exception: java.net.SocketException: Connection reset"), this.width / 2, this.height / 2 - 10, 0xAAAAAA);
        } else {

            context.getMatrices().push();

            int offsetX = (int)(Math.random() * 6 - 3);
            int offsetY = (int)(Math.random() * 6 - 3);
            context.getMatrices().translate(offsetX, offsetY, 0);

            // 텍스트 변환
            Text creepyTitle = Text.translatable("messages.corruption.creepy.title").formatted(Formatting.DARK_RED, Formatting.OBFUSCATED);
            Text creepyReason = Text.translatable("messages.corruption.creepy.text").formatted(Formatting.RED);

            context.drawCenteredTextWithShadow(this.textRenderer, creepyTitle, this.width / 2, this.height / 2 - 40, 0xAA0000);
            context.drawCenteredTextWithShadow(this.textRenderer, creepyReason, this.width / 2, this.height / 2 - 10, 0xFF0000);

            context.getMatrices().pop();
        }
    }
}