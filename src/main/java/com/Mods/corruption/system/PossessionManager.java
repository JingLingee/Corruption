package com.Mods.corruption.system;

import com.Mods.corruption.entity.CorruptionEntities;
import com.Mods.corruption.entity.custom.PuppetEntity;
import com.Mods.corruption.network.CorruptionNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class PossessionManager {

    private static final Map<UUID, Integer> POSSESSED_PLAYERS = new HashMap<>();
    private static final Map<UUID, UUID> PUPPET_MAP = new HashMap<>();

    // 공격 쿨타임 관리 (플레이어 UUID -> 남은 쿨타임 틱)
    private static final Map<UUID, Integer> ATTACK_COOLDOWN = new HashMap<>();

    // 공격 범위 (블록)
    private static final double ATTACK_RANGE = 3.0;

    // 공격 쿨타임 (틱) - 20틱 = 1초
    private static final int ATTACK_COOLDOWN_TICKS = 10;

    public static void possess(ServerPlayerEntity player) {
        POSSESSED_PLAYERS.put(player.getUuid(), 1500);
        ATTACK_COOLDOWN.put(player.getUuid(), 0);

        PuppetEntity puppet = new PuppetEntity(CorruptionEntities.PUPPET, player.getServerWorld());
        puppet.setPos(player.getX(), player.getY(), player.getZ());
        player.getServerWorld().spawnEntity(puppet);

        PUPPET_MAP.put(player.getUuid(), puppet.getUuid());
        CorruptionNetworking.sendPossession(player, true);
    }

    public static void release(ServerPlayerEntity player) {
        if (POSSESSED_PLAYERS.remove(player.getUuid()) != null) {
            CorruptionNetworking.sendDistortion(player, 0.0f);

            discardPuppet(player);
            completeRelease(player);

        }
    }

    private static void completeRelease(ServerPlayerEntity player) {
        ATTACK_COOLDOWN.remove(player.getUuid());

        CorruptionNetworking.sendDistortion(player, 0.0f);

        CorruptionNetworking.sendPossession(player, false);
        player.sendMessage(Text.translatable("messages.corruption.out_possession"), true);
    }

    private static void discardPuppet(ServerPlayerEntity player) {
        UUID puppetUUID = PUPPET_MAP.remove(player.getUuid());
        if (puppetUUID == null) return;
        Entity puppet = player.getServerWorld().getEntity(puppetUUID);
        if (puppet != null) puppet.discard();
    }

    public static boolean isPossessed(ServerPlayerEntity player) {
        return POSSESSED_PLAYERS.containsKey(player.getUuid());
    }

    public static void tick(ServerWorld world) {
        if (POSSESSED_PLAYERS.isEmpty()) return;

        Iterator<Map.Entry<UUID, Integer>> iterator = POSSESSED_PLAYERS.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();

            UUID uuid = entry.getKey();
            int timeLeft = entry.getValue();

            ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(uuid);

            if (player == null) {
                UUID puppetUUID = PUPPET_MAP.remove(uuid);
                if (puppetUUID != null) {
                    Entity puppet = world.getEntity(puppetUUID);
                    if (puppet != null) puppet.discard();
                }
                ATTACK_COOLDOWN.remove(uuid);
                iterator.remove();
                continue;
            }

            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.DARKNESS, 40, 0, false, false, false));
            applyPossessionEffects(player);
            syncFromPuppet(player, world);
            tryAttack(player, world);

            timeLeft--;
            if (timeLeft <= 0) {
                discardPuppet(player);
                completeRelease(player);
                iterator.remove();
            } else {
                entry.setValue(timeLeft);
            }
        }
    }

    private static void applyPossessionEffects(ServerPlayerEntity player) {

        player.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                net.minecraft.entity.effect.StatusEffects.DARKNESS, 40, 0, false, false));

        if (player.getRandom().nextFloat() < 0.01f) {
            MutableText message = Text.empty()
                    .setStyle(Style.EMPTY.withFormatting(Formatting.OBFUSCATED))
                    .append("abcde")
                    .append(Text.empty().setStyle(Style.EMPTY.withFormatting(Formatting.RESET)))
                    .append(" ")
                    .append(Text.translatable("subtitles.corruption.the_other_possess").formatted(Formatting.RED)); // 번역 + 빨강

            player.sendMessage(message, true);
        }

        // 3. 지속적인 낮은 글리치 강도 유지
        CorruptionNetworking.sendDistortion(player, 0.3f + (player.getRandom().nextFloat() * 0.2f));
    }

    private static void tryAttack(ServerPlayerEntity player, ServerWorld world) {
        // 쿨타임 감소
        int cooldown = ATTACK_COOLDOWN.getOrDefault(player.getUuid(), 0);
        if (cooldown > 0) {
            ATTACK_COOLDOWN.put(player.getUuid(), cooldown - 1);
            return;
        }

        // puppet의 타겟을 가져옴
        UUID puppetUUID = PUPPET_MAP.get(player.getUuid());
        if (puppetUUID == null) return;

        Entity puppetEntity = world.getEntity(puppetUUID);
        if (!(puppetEntity instanceof PuppetEntity puppet)) return;

        LivingEntity target = puppet.getTarget();
        if (target == null || !target.isAlive()) return;

        // 플레이어와 타겟 사이 거리 확인
        double distSq = player.squaredDistanceTo(target);
        if (distSq > ATTACK_RANGE * ATTACK_RANGE) return;

        // 공격
        target.damage(
                player.getDamageSources().playerAttack(player),
                (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)
        );
        player.swingHand(Hand.MAIN_HAND, true);

        ATTACK_COOLDOWN.put(player.getUuid(), ATTACK_COOLDOWN_TICKS);
    }

    private static float lerpYaw(float current, float target, float speed) {
        float diff = target - current;
        while (diff > 180f) diff -= 360f;
        while (diff < -180f) diff += 360f;
        if (Math.abs(diff) < speed) return target;
        return current + Math.signum(diff) * speed;
    }

    private static void syncFromPuppet(ServerPlayerEntity player, ServerWorld world) {
        UUID puppetUUID = PUPPET_MAP.get(player.getUuid());
        if (puppetUUID == null) return;

        Entity puppet = world.getEntity(puppetUUID);
        if (puppet == null) return;

        // 회전 보간
        float smoothedYaw = lerpYaw(player.getYaw(), puppet.getYaw(), 8f);
        float smoothedPitch = lerpYaw(player.getPitch(), puppet.getPitch(), 8f);

        player.setYaw(smoothedYaw);
        player.setHeadYaw(smoothedYaw);
        player.bodyYaw = smoothedYaw;

        // 위치 보간
        Vec3d playerPos = player.getPos();
        Vec3d puppetPos = puppet.getPos();
        double t = 0.3;
        double lerpX = playerPos.x + (puppetPos.x - playerPos.x) * t;
        double lerpY = playerPos.y + (puppetPos.y - playerPos.y) * t;
        double lerpZ = playerPos.z + (puppetPos.z - playerPos.z) * t;

        player.networkHandler.requestTeleport(lerpX, lerpY, lerpZ, smoothedYaw, smoothedPitch);
    }
}