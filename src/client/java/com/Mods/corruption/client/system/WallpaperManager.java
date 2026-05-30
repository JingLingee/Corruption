package com.Mods.corruption.client.system;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class WallpaperManager {

    public interface User32 extends Library {
        User32 INSTANCE = Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
        boolean SystemParametersInfo(int uiAction, int uiParam, String pvParam, int fWinIni);
    }

    public static void changeWallpaper() {
        System.out.println("[Corruption Debug] 바탕화면 변경 이벤트 수신 성공! 실행을 시작합니다.");

        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            System.out.println("[Corruption Debug] 실패: 윈도우 운영체제가 아닙니다.");
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        // 이미지 경로 설정
        Identifier imageId = Identifier.of("corruption", "textures/gui/noise.png");

        try {
            var resourceOpt = client.getResourceManager().getResource(imageId);
            if (resourceOpt.isEmpty()) {
                System.out.println("[Corruption Debug] 실패: 이미지를 찾을 수 없습니다! 경로를 확인하세요 -> " + imageId.toString());
                return;
            }

            System.out.println("[Corruption Debug] 이미지를 찾았습니다. 임시 폴더로 파일 복사 시작...");
            // 권한 문제가 없도록 마인크래프트 실행 폴더에 임시 파일 생성
            File tempFile = new File(client.runDirectory, "corrupted_bg.png");

            try (InputStream in = resourceOpt.get().getInputStream();
                 FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("[Corruption Debug] 파일 생성 완료: " + tempFile.getAbsolutePath());
            System.out.println("[Corruption Debug] Windows API 호출 중...");

            // 바탕화면 강제 변경 API 호출
            boolean success = User32.INSTANCE.SystemParametersInfo(0x0014, 0, tempFile.getAbsolutePath(), 1 | 2);

            if (success) {
                System.out.println("[Corruption Debug] 바탕화면 변경 완벽 성공!");
                if (client.player != null) {
                    client.player.playSound(SoundEvents.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
                }
            } else {
                System.out.println("[Corruption Debug] 실패: Windows API가 막혀있습니다. (정품 인증이 안 된 윈도우일 가능성이 있습니다)");
            }

        } catch (Throwable e) {
            System.out.println("[Corruption Debug] 치명적인 오류 발생:");
            e.printStackTrace();
        }
    }
}