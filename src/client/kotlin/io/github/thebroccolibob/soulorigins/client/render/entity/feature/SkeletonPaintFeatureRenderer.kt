package io.github.thebroccolibob.soulorigins.client.render.entity.feature

import io.github.thebroccolibob.soulorigins.Soulorigins
import io.github.thebroccolibob.soulorigins.entity.OwnableSkeleton
import io.github.thebroccolibob.soulorigins.entity.isTamed
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.SkeletonEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.mob.AbstractSkeletonEntity
import net.minecraft.util.Identifier

class SkeletonPaintFeatureRenderer(context: FeatureRendererContext<AbstractSkeletonEntity, SkeletonEntityModel<AbstractSkeletonEntity>>) :
    FeatureRenderer<AbstractSkeletonEntity, SkeletonEntityModel<AbstractSkeletonEntity>>(context) {

    companion object {
        val SKIN = Identifier(Soulorigins.MOD_ID, "textures/entity/skeleton/skeleton_paint.png")
    }

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        skeletonEntity: AbstractSkeletonEntity,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        if ((skeletonEntity as OwnableSkeleton).isTamed && !skeletonEntity.isInvisible) {
            renderModel(this.contextModel, SKIN, matrices, vertexConsumers, light, skeletonEntity, 1.0f, 1.0f, 1.0f)
        }
    }
}
