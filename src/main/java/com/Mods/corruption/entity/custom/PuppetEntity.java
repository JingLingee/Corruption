package com.Mods.corruption.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class PuppetEntity extends PathAwareEntity {

    public PuppetEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setInvisible(true);
        this.setSilent(true);
        this.setNoGravity(false);
        this.setInvulnerable(true);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new MeleeAttackGoal(this, 3.0D, true));

        // 새로 범위 안에 들어온 타겟 탐색
        this.targetSelector.add(0, new ActiveTargetGoal<>(
                this,
                MobEntity.class,
                3,
                false,
                false,
                target -> !(target instanceof PuppetEntity)
        ));

        // 타겟을 잃었을 때 다시 찾는 용도
        this.targetSelector.add(1, new net.minecraft.entity.ai.goal.RevengeGoal(this));
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    // 실제 공격은 하지 않음 - 위치/방향 복사 전용
    @Override
    public boolean tryAttack(net.minecraft.entity.Entity target) {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 999.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.13D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 128.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.0D)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1D);
    }
}