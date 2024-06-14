package io.github.thebroccolibob.soulorigins.client.render.entity.feature

import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.cca.OwnerComponent.Companion.isOwned
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.SkeletonEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.mob.AbstractSkeletonEntity

class SkeletonPaintFeatureRenderer(context: FeatureRendererContext<AbstractSkeletonEntity, SkeletonEntityModel<AbstractSkeletonEntity>>) :
    FeatureRenderer<AbstractSkeletonEntity, SkeletonEntityModel<AbstractSkeletonEntity>>(context) {

    companion object {
        val SKIN = SoulOrigins.id("textures/entity/skeleton/skeleton_paint.png")
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
        if (skeletonEntity.isOwned && !skeletonEntity.isInvisible) {
            renderModel(this.contextModel, SKIN, matrices, vertexConsumers, light, skeletonEntity, 1.0f, 1.0f, 1.0f)
        }
    }
}
