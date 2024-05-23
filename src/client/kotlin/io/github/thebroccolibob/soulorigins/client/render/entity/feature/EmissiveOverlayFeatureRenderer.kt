package io.github.thebroccolibob.soulorigins.client.render.entity.feature

import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.thebroccolibob.soulorigins.power.EmissiveOverlayPower
import net.merchantpug.apugli.client.util.TextureUtilClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.render.entity.model.EntityModelLoader
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity

class EmissiveOverlayFeatureRenderer<T : LivingEntity, M : EntityModel<T>>(
    context: FeatureRendererContext<T, M>,
    slim: Boolean,
    loader: EntityModelLoader
) :
    FeatureRenderer<T, M>(context) {
    private var extraPlayerModel: PlayerEntityModel<T>? = null

    init {
        if (context.model is PlayerEntityModel<*>) {
            this.extraPlayerModel = PlayerEntityModel<T>(
                loader.getModelPart(if (slim) EntityModelLayers.PLAYER_SLIM else EntityModelLayers.PLAYER),
                slim
            )
        }
    }

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: T,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        PowerHolderComponent.getPowers(entity, EmissiveOverlayPower::class.java)
            .forEach { power ->
                if (power.textureLocation != null || power.textureUrl != null) {
                    val renderLayer = RenderLayer.getEntityTranslucentEmissive(if (TextureUtilClient.getUrls().containsKey(power.urlTextureIdentifier)) power.urlTextureIdentifier else power.textureLocation)

                    if (renderLayer != null) {
                        matrices.push()

                        val entityModel = extraPlayerModel ?: this.contextModel
                        val model: EntityModel<T> = this.contextModel
                        if (model is PlayerEntityModel<*>) {
                            val extraModel = entityModel as PlayerEntityModel<T>
                            (model as PlayerEntityModel<T>).copyBipedStateTo(extraModel)
                            extraModel.jacket.copyTransform(model.jacket)
                            extraModel.leftSleeve.copyTransform(model.leftSleeve)
                            extraModel.rightSleeve.copyTransform(model.rightSleeve)
                            extraModel.leftPants.copyTransform(model.leftPants)
                            extraModel.rightPants.copyTransform(model.rightPants)
                        }

                        entityModel.render(
                            matrices,
                            vertexConsumers.getBuffer(renderLayer),
                            light,
                            OverlayTexture.DEFAULT_UV,
                            1.0f,
                            1.0f,
                            1.0f,
                            1.0f
                        )
                        matrices.pop()
                    }
                }
            }
    }
}
