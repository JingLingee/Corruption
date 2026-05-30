package com.Mods.corruption.client.renderer;

import com.Mods.corruption.entity.custom.StatueEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class StatueRenderer extends MobEntityRenderer<StatueEntity, EntityModel<StatueEntity>> {

    private static final Identifier CHICKEN_TEXTURE =
            Identifier.of("minecraft", "textures/entity/chicken.png");
    private static final Identifier PIG_TEXTURE =
            Identifier.of("minecraft", "textures/entity/pig/pig.png");
    private static final Identifier COW_TEXTURE =
            Identifier.of("minecraft", "textures/entity/cow/cow.png");
    private static final Identifier SHEEP_TEXTURE =
            Identifier.of("minecraft", "textures/entity/sheep/sheep.png");

    private final EntityModel<StatueEntity> chickenModel;
    private final EntityModel<StatueEntity> pigModel;
    private final EntityModel<StatueEntity> cowModel;
    private final EntityModel<StatueEntity> sheepModel;

    // setAngles에 넘길 더미 엔티티들 (필드 접근 대신 실제 타입 인스턴스 사용)
    private static ChickenEntity dummyChicken = null;
    private static PigEntity dummyPig = null;
    private static CowEntity dummyCow = null;
    private static SheepEntity dummySheep = null;

    @SuppressWarnings("unchecked")
    public StatueRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, (EntityModel<StatueEntity>) (EntityModel<?>)
                new PigEntityModel<>(PigEntityModel.getTexturedModelData(Dilation.NONE).createModel()), 0.5f);

        ChickenEntityModel<ChickenEntity> chicken = new ChickenEntityModel<>(
                ChickenEntityModel.getTexturedModelData().createModel());
        chicken.child = false;
        this.chickenModel = new WrappedModel<>(chicken);

        PigEntityModel<PigEntity> pig = new PigEntityModel<>(
                PigEntityModel.getTexturedModelData(Dilation.NONE).createModel());
        pig.child = false;
        this.pigModel = new WrappedModel<>(pig);

        CowEntityModel<CowEntity> cow = new CowEntityModel<>(
                CowEntityModel.getTexturedModelData().createModel());
        cow.child = false;
        this.cowModel = new WrappedModel<>(cow);

        SheepEntityModel<SheepEntity> sheep = new SheepEntityModel<>(
                SheepEntityModel.getTexturedModelData().createModel());
        sheep.child = false;
        this.sheepModel = new WrappedModel<>(sheep);
    }

    // 더미 엔티티를 지연 초기화 (World가 필요하므로 렌더 시점에 생성)
    private static void initDummies(World world) {
        if (dummyChicken == null) {
            dummyChicken = new ChickenEntity(EntityType.CHICKEN, world);
            dummyChicken.setBaby(false);
        }
        if (dummyPig == null) {
            dummyPig = new PigEntity(EntityType.PIG, world);
            dummyPig.setBaby(false);
        }
        if (dummyCow == null) {
            dummyCow = new CowEntity(EntityType.COW, world);
            dummyCow.setBaby(false);
        }
        if (dummySheep == null) {
            dummySheep = new SheepEntity(EntityType.SHEEP, world);
            dummySheep.setBaby(false);
        }
    }

    @Override
    public void render(StatueEntity entity, float yaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        initDummies(entity.getWorld());
        this.model = switch (entity.getAnimalType()) {
            case CHICKEN -> chickenModel;
            case PIG     -> pigModel;
            case COW     -> cowModel;
            case SHEEP   -> sheepModel;
        };
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(StatueEntity entity) {
        return switch (entity.getAnimalType()) {
            case CHICKEN -> CHICKEN_TEXTURE;
            case PIG     -> PIG_TEXTURE;
            case COW     -> COW_TEXTURE;
            case SHEEP   -> SHEEP_TEXTURE;
        };
    }

    // 더미 엔티티를 setAngles에 넘기는 래퍼
    @SuppressWarnings("unchecked")
    private static class WrappedModel<A extends net.minecraft.entity.Entity>
            extends EntityModel<StatueEntity> {

        private final EntityModel<A> inner;

        WrappedModel(EntityModel<A> inner) {
            this.inner = inner;
        }

        @Override
        public void setAngles(StatueEntity entity, float limbAngle, float limbDistance,
                              float animationProgress, float headYaw, float headPitch) {
            // inner 타입에 맞는 더미 엔티티 선택
            A dummy = null;
            if (inner instanceof ChickenEntityModel) dummy = (A) dummyChicken;
            else if (inner instanceof PigEntityModel) dummy = (A) dummyPig;
            else if (inner instanceof CowEntityModel) dummy = (A) dummyCow;
            else if (inner instanceof SheepEntityModel) dummy = (A) dummySheep;

            inner.setAngles(dummy, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertices,
                           int light, int overlay, int color) {
            inner.render(matrices, vertices, light, overlay, color);
        }
    }
}