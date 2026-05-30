package com.Mods.corruption.entity.custom;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class StatueEntity extends MobEntity implements GeoEntity {

    // 동물 타입 (0=닭, 1=돼지, 2=소, 3=양)
    public enum AnimalType {
        CHICKEN, PIG, COW, SHEEP
    }

    private static final TrackedData<Integer> ANIMAL_TYPE =
            DataTracker.registerData(StatueEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private boolean isMoving = false;
    private int stepCooldown = 6;
    private int teleportCooldown = 0;

    public StatueEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ANIMAL_TYPE, 0);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty,
                                 SpawnReason spawnReason, EntityData entityData) {
        this.dataTracker.set(ANIMAL_TYPE, this.getRandom().nextInt(4));
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public void tick() {
        super.tick();



        PlayerEntity player = this.getWorld().getClosestPlayer(this, 30);

        if (player == null) {
            isMoving = false;
            return;
        }

        boolean looking = player.canSee(this) && isPlayerLooking(player, this);

        if (looking) {
            this.getNavigation().stop();
            this.setVelocity(0, this.getVelocity().y, 0);
            isMoving = false;
        } else {
            this.getNavigation().startMovingTo(player, 1.4);
            isMoving = true;
            if (isMoving) {
                stepCooldown--;

                if (stepCooldown <= 0) {
                    this.getWorld().playSound(
                            null,
                            this.getBlockPos(),
                            SoundEvents.BLOCK_METAL_PLACE,
                            SoundCategory.HOSTILE,
                            1.0f,
                            0.8f + this.getRandom().nextFloat() * 0.4f
                    );

                    stepCooldown = 10;
                }
            } else {
                stepCooldown = 0;
            }
        }

        if (!this.getWorld().isClient) {

            if (teleportCooldown > 0) teleportCooldown--;
            if (player instanceof ServerPlayerEntity sp
                    && teleportCooldown <= 0
                    && this.distanceTo(player) < 1.2) {

                teleportCooldown = 40;

                MinecraftServer server = sp.getServer();

                RegistryKey<World> STATUE_DIMENSION =
                        RegistryKey.of(
                                RegistryKeys.WORLD,
                                Identifier.of("corruption", "statue_dimension")
                        );

                ServerWorld targetWorld =
                        server.getWorld(STATUE_DIMENSION);

                if (targetWorld != null) {

                    double x = this.getX();
                    double z = this.getZ();

                    int safeY = targetWorld.getTopY(
                            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                            (int)x,
                            (int)z
                    );

                    sp.teleport(
                            targetWorld,
                            x,
                            safeY + 5,
                            z,
                            sp.getYaw(),
                            sp.getPitch()
                    );
                }
            }
        }
    }



    public AnimalType getAnimalType() {
        return AnimalType.values()[this.dataTracker.get(ANIMAL_TYPE)];
    }

    public static boolean isPlayerLooking(PlayerEntity player, StatueEntity entity) {
        Vec3d playerLook = player.getRotationVec(1.0F).normalize();
        Vec3d toEntity = entity.getPos()
                .add(0, entity.getHeight() / 2, 0)
                .subtract(player.getCameraPosVec(1.0F))
                .normalize();
        double dot = playerLook.dotProduct(toEntity);
        return dot > 0.5;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6);
    }

    @Override
    public RegistryKey<LootTable> getLootTableId() {
        return RegistryKey.of(
                RegistryKeys.LOOT_TABLE,
                Identifier.of("corruption", "entities/statue")
        );
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, state -> {
            if (isMoving) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("run"));
            } else {
                return state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object entity) {
        return ((StatueEntity) entity).age;
    }
}