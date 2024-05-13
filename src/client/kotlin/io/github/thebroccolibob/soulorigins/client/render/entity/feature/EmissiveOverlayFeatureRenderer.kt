package io.github.thebroccolibob.soulorigins.client.render.entity.feature

import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.thebroccolibob.soulorigins.power.EmissiveOverlayPower
import net.merchantpug.apugli.client.util.TextureUtilClient
import net.minecraft.client.MinecraftClient
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
                    var renderLayer: RenderLayer? = null
                    if (TextureUtilClient.getUrls().containsKey(power.urlTextureIdentifier)) {
                        renderLayer = if (MinecraftClient.getInstance()
                                .hasOutline(entity) && entity.isInvisible
                        ) RenderLayer.getOutline(power.urlTextureIdentifier) else RenderLayer.getEntityTranslucent(
                            power.urlTextureIdentifier
                        )
                    } else if (power.textureLocation != null) {
                        renderLayer = if (MinecraftClient.getInstance()
                                .hasOutline(entity) && entity.isInvisible
                        ) RenderLayer.getOutline(power.textureLocation) else RenderLayer.getEntityTranslucent(
                            power.textureLocation
                        )
                    }

                    if (renderLayer != null) {
                        matrices.push()
                        val red = 1.0f
                        val green = 1.0f
                        val blue = 1.0f
                        val alpha = 1.0f

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

                        (entityModel as EntityModel<*>).render(
                            matrices,
                            vertexConsumers.getBuffer(renderLayer),
                            15728640,
                            OverlayTexture.DEFAULT_UV,
                            red,
                            green,
                            blue,
                            alpha
                        )
                        matrices.pop()
                    }
                }
            }
    }
}
