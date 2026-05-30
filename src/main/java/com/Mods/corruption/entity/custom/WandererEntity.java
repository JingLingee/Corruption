package com.Mods.corruption.entity.custom;

import com.Mods.corruption.network.CorruptionNetworking;
import com.Mods.corruption.network.packet.WandererJumpscarePayload;
import com.Mods.corruption.sound.ModSounds;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.EnumSet;

public class WandererEntity extends FlyingEntity implements GeoEntity {

    private static final Logger LOGGER = LogManager.getLogger();

    // =======================
    // 상태 머신
    // =======================
    public enum State {
        IDLE,
        OPENING,
        CHASING,
        STARE,      // 5블럭 도달 후 정지
        RUSH        // 돌진
    }

    private static final TrackedData<Integer> STATE =
            DataTracker.registerData(WandererEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private int stareTicks = 0;
    private static final int MAX_STARE_TICKS = 60; // 3초 (20tick * 3)
    private int openTicks = 0;
    private static final int MAX_OPEN_TICKS = 40;

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public WandererEntity(EntityType<? extends FlyingEntity> type, World world) {
        super(type, world);
        this.moveControl = new FlightMoveControl(this, 10, true);
        this.setNoGravity(true);
    }

    private void controlPlayer(PlayerEntity player) {

        if (!(player instanceof net.minecraft.server.network.ServerPlayerEntity sp))
            return;

        // 이동 완전 차단
        sp.setVelocity(0, 0, 0);
        sp.velocityModified = true;
        sp.setSprinting(false);
        sp.forwardSpeed = 0;
        sp.sidewaysSpeed = 0;

        // 실명
        sp.addStatusEffect(new StatusEffectInstance(
                StatusEffects.DARKNESS, 40, 1, false, false));

        // 나약함 100
        sp.addStatusEffect(new StatusEffectInstance(
                StatusEffects.WEAKNESS, 40, 100, false, false));

        // 슬로우 255 (완전 고정)
        sp.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SLOWNESS, 40, 255, false, false));

        // 시야 계산 (머리 기준)
        double dx = this.getX() - sp.getX();
        double dy = this.getEyeY() - sp.getEyeY();
        double dz = this.getZ() - sp.getZ();

        double dist = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float)(Math.atan2(dz, dx) * (180 / Math.PI)) - 90F;
        float pitch = (float)(-(Math.atan2(dy, dist) * (180 / Math.PI)));

        sp.teleport(
                sp.getServerWorld(),
                sp.getX(),
                sp.getY(),
                sp.getZ(),
                yaw,
                pitch
        );

        ServerPlayNetworking.send(sp, new WandererJumpscarePayload());
    }

    // =======================
    // DataTracker
    // =======================
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(STATE, State.IDLE.ordinal());
    }

    private void setState(State state) {
        this.dataTracker.set(STATE, state.ordinal());
    }

    private State getState() {
        return State.values()[this.dataTracker.get(STATE)];
    }

    // =======================
    // GeckoLib
    // =======================
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, state -> {

            switch (getState()) {

                case OPENING:
                    return state.setAndContinue(
                            RawAnimation.begin().thenPlay("open"));

                case CHASING:
                case STARE:
                case RUSH:
                    return state.setAndContinue(
                            RawAnimation.begin().thenLoop("opened_loop"));

                case IDLE:
                default:
                    return state.setAndContinue(
                            RawAnimation.begin().thenLoop("idle_closed"));
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }



    // =======================
    // 능력치
    // =======================
    public static DefaultAttributeContainer.Builder createAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 3)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 6)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new BirdNavigation(this, world);
    }

    // =======================
    // Wander Goal
    // =======================
    private class WanderGoal extends Goal {

        public WanderGoal() {
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return getState() == State.IDLE;
        }

        @Override
        public void tick() {
            if (getRandom().nextInt(80) == 0) {
                double x = getX() + (random.nextDouble() - 0.5) * 8;
                double y = getY() + (random.nextDouble() - 0.5) * 4;
                double z = getZ() + (random.nextDouble() - 0.5) * 8;

                getMoveControl().moveTo(x, y, z, 0.3);
            }
        }
    }



    // =======================
    // Chase Goal
    // =======================
    private class ChaseGoal extends Goal {

        public ChaseGoal() {
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            return getState() == State.CHASING && getTarget() != null;
        }

        @Override
        public void tick() {
            PlayerEntity target = (PlayerEntity) getTarget();
            if (target == null) return;

            getLookControl().lookAt(target, 360f, 360f);
            getMoveControl().moveTo(
                    target.getX(),
                    target.getEyeY(),
                    target.getZ(),
                    0.4
            );
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new WanderGoal());
        this.goalSelector.add(2, new ChaseGoal());
        this.targetSelector.add(1,
                new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    // =======================
    // 메인 로직
    // =======================
    @Override
    public void tick() {
        super.tick();

        if (getWorld().isClient) return;

        PlayerEntity player = (PlayerEntity) this.getTarget();

        switch (getState()) {

            case IDLE:
                PlayerEntity nearby = getWorld().getClosestPlayer(this, 20);
                if (nearby != null) {
                    this.setTarget(nearby);
                    setState(State.OPENING);
                    openTicks = 0;
                }
                break;

            case OPENING:
                openTicks++;

                if (player != null) {
                    this.getLookControl().lookAt(player, 360f, 360f);
                }

                if (openTicks >= MAX_OPEN_TICKS) {
                    setState(State.CHASING);
                }
                break;

            case CHASING:

                if (player == null || player.isDead()) {
                    setState(State.IDLE);
                    this.setTarget(null);
                    break;
                }

                controlPlayer(player);

                double maxDistance = 20.0;
                double distance = this.distanceTo(player);

                float t = 1.0f - (float)(distance / maxDistance);
                t = Math.max(0f, Math.min(1f, t));

                float intensity = t * t; // 제곱

                CorruptionNetworking.sendOverlay((ServerPlayerEntity) player, intensity);



                if (distance <= 5 && getState() != State.STARE) {
                    stareTicks = 0;
                    setState(State.STARE);
                    if (player instanceof ServerPlayerEntity sp) {
                        sp.getServerWorld().playSound(
                                null, // 🔥 모든 플레이어
                                sp.getBlockPos(),
                                ModSounds.WANDERER_STARING,
                                net.minecraft.sound.SoundCategory.MASTER,
                                3.0F,
                                1.0F
                        );
                    }
                }

                break;
            case STARE:

                if (player == null) break;

                controlPlayer(player);

                this.getMoveControl().moveTo(getX(), getY(), getZ(), 0); // 완전 정지
                stareTicks++;

                if (stareTicks >= MAX_STARE_TICKS) {
                    setState(State.RUSH);
                }

                break;

            case RUSH:

                if (player == null) break;

                controlPlayer(player);

                // 방향 벡터 계산
                double dx = player.getX() - this.getX();
                double dy = player.getEyeY() - this.getEyeY();
                double dz = player.getZ() - this.getZ();

                double length = Math.sqrt(dx * dx + dy * dy + dz * dz);

                if (length > 0) {
                    dx /= length;
                    dy /= length;
                    dz /= length;
                }

                double rushSpeed = 2.0;

                this.setVelocity(dx * rushSpeed, dy * rushSpeed, dz * rushSpeed);
                this.velocityModified = true;

                if (this.distanceTo(player) <= 1.5) {
                    player.damage(this.getDamageSources().cramming(), 1000f);
                    setState(State.IDLE);
                    this.discard();
                    CorruptionNetworking.sendOverlay((ServerPlayerEntity) player, 0);
                }

                break;
        }


    }



    public float getOpenProgress() {
        return (float) openTicks / MAX_OPEN_TICKS;
    }
}