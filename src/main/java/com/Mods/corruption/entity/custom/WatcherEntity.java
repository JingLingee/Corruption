package com.Mods.corruption.entity.custom;

import com.Mods.corruption.sound.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WatcherEntity extends MobEntity implements GeoAnimatable {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // 애니메이션 컨트롤러 등록
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public WatcherEntity(EntityType<? extends MobEntity> type, World world) {
        super(type, world);
    }

    @Override
    public double getTick(Object entity) {
        return ((WatcherEntity) entity).age;
    }

    @Override
    public void tick() {
        super.tick();


        PlayerEntity player = this.getWorld().getClosestPlayer(this, 128);

        if (player == null) return;

        // 항상 플레이어 바라보기
        this.lookAtEntity(player, 360f, 360f);

        // 플레이어가 엔티티를 바라보는지 체크
        if (isPlayerLookingAtMe(player)) {

            this.getWorld().playSound(
                    player.getX(), player.getY(), player.getZ(),
                    ModSounds.WATCHER_HEARTBEAT,
                    SoundCategory.AMBIENT,
                    1.0F,
                    0.8F,
                    false // 소리를 무작위로 재생하지 않음
            );

            this.discard(); // 즉시 제거
        }


    }

    private boolean isPlayerLookingAtMe(PlayerEntity player) {

        Vec3d playerLook = player.getRotationVec(1.0f).normalize();
        Vec3d toEntity = this.getPos()
                .add(0, this.getHeight() / 2, 0)
                .subtract(player.getCameraPosVec(1.0f))
                .normalize();

        double dot = playerLook.dotProduct(toEntity);

        // 0.95 이상이면 거의 정면 응시
        return dot > 0.95;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 128.0);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected void initGoals() {
        // 0순위: 플레이어를 20블록 안에서 쳐다봄
        this.goalSelector.add(0, new LookAtEntityGoal(this, PlayerEntity.class, 40.0F));

        // 1순위: 가끔 주위를 둘러봄 (자연스러움 추가)
        this.goalSelector.add(1, new LookAroundGoal(this));

        // 이전에 만든 도망가기 AI가 있다면 여기에 이어서 추가하세요.
    }
}