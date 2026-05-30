package com.Mods.corruption.entity.custom;

import com.Mods.corruption.network.CorruptionNetworking;
import com.Mods.corruption.system.PossessionManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.List;

public class TheOtherEntity extends PathAwareEntity {

    private boolean hasPossessed = false;

    public TheOtherEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {

        this.goalSelector.add(0, new MeleeAttackGoal(this, 1.8D, false));
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 16.0f));
        this.goalSelector.add(2, new LookAroundGoal(this));

        this.targetSelector.add(0,
                new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) return;

        List<PlayerEntity> players = this.getWorld().getEntitiesByClass(
                PlayerEntity.class,
                this.getBoundingBox().expand(32),
                p -> true
        );

        for (PlayerEntity player : players) {

            if (!(player instanceof ServerPlayerEntity serverPlayer)) continue;

            if (com.Mods.corruption.system.PossessionManager.isPossessed(serverPlayer)) continue;

            double dist = this.distanceTo(player);

            float intensity = 0f;

            if (dist < 20) {
                intensity = (float)(1.0 - dist / 20.0);
                intensity = Math.max(0f, Math.min(1f, intensity));
            }

            if (dist < 15.0 && this.random.nextFloat() < 0.005f) { // 확률은 적절히 조절
                // 소리 종류: MUSIC_GAME (기본 브금들 중 랜덤)
                // 피치: 0.5f (반값으로 낮춰서 아주 느리고 무겁게 들리게 함)
                serverPlayer.getWorld().playSound(
                        null,
                        serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
                        SoundEvents.AMBIENT_CAVE.value(),
                        net.minecraft.sound.SoundCategory.AMBIENT,
                        1.0f,
                        0.5f,
                        serverPlayer.getWorld().getRandom().nextLong()
                );
            }

            CorruptionNetworking.sendDistortion(serverPlayer, intensity);
        }

        // ===== 빙의 =====

        if (!hasPossessed) {
            List<PlayerEntity> closePlayers = this.getWorld().getEntitiesByClass(
                    PlayerEntity.class,
                    this.getBoundingBox().expand(0.3),
                    p -> true
            );

            if (!closePlayers.isEmpty()) {
                possess(closePlayers.get(0));
            }
        }
    }



    @Override
    public boolean isAttackable() {
        return false;
    }



    private void possess(PlayerEntity player) {
        if (hasPossessed) return;
        hasPossessed = true;

        ServerPlayerEntity sPlayer = (ServerPlayerEntity) player;

        CorruptionNetworking.sendDistortion(sPlayer, 1.0f);
        CorruptionNetworking.sendDistortion(sPlayer, 0f);

        CorruptionNetworking.sendDistortion(sPlayer, 0f);
        CorruptionNetworking.sendDistortion(sPlayer, 0f);


        CorruptionNetworking.sendDistortion(sPlayer, 0f);
        PossessionManager.possess(sPlayer);
        CorruptionNetworking.sendDistortion(sPlayer, 0f);


        CorruptionNetworking.sendDistortion(sPlayer, 0f);
        this.discard();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.45D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.0D);
    }
}