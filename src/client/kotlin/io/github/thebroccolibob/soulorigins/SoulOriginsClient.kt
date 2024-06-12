package io.github.thebroccolibob.soulorigins

import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import io.github.thebroccolibob.soulorigins.client.particle.GustEmitterParticle
import io.github.thebroccolibob.soulorigins.client.particle.GustParticle
import io.github.thebroccolibob.soulorigins.client.particle.VineParticle
import io.github.thebroccolibob.soulorigins.entity.SoulOriginsEntities
import io.github.thebroccolibob.soulorigins.item.MarigoldCardItem.Companion.hasEntity
import io.github.thebroccolibob.soulorigins.item.MobOrbItem
import io.github.thebroccolibob.soulorigins.item.SoulOriginsItems
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.FlyingItemEntityRenderer

object SoulOriginsClient : ClientModInitializer {
	override fun onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ModelPredicateProviderRegistry.register(SoulOriginsItems.MARIGOLD_CARD, SoulOrigins.id("has_entity")) {
			itemStack, _, _, _ ->
			if (itemStack.hasEntity) 1f else 0f
		}
		ModelPredicateProviderRegistry.register(SoulOriginsItems.MOB_ORB, SoulOrigins.id("mob_type")) {
				itemStack, _, _, _ ->
			MobOrbItem.getMobType(itemStack)
		}

		with (ParticleFactoryRegistry.getInstance()) {
			register(SoulOriginsParticles.GUST, GustParticle::Factory)
			register(SoulOriginsParticles.SMALL_GUST, GustParticle::SmallGustFactory)
			register(SoulOriginsParticles.GUST_EMITTER_LARGE, GustEmitterParticle.Factory(3.0, 12, 0))
			register(SoulOriginsParticles.GUST_EMITTER_SMALL, GustEmitterParticle.Factory(1.0, 3, 2))
			register(SoulOriginsParticles.VINE, VineParticle::Factory)
		}

		BlockRenderLayerMap.INSTANCE.putBlock(SoulOriginsBlocks.DECAYING_SLIME, RenderLayer.getTranslucent())
		BlockRenderLayerMap.INSTANCE.putBlock(SoulOriginsBlocks.DECAYING_COBWEB_BLOCK, RenderLayer.getCutout())

		EntityRendererRegistry.register(SoulOriginsEntities.SURFACE_BUILDER_PROJECTILE, ::FlyingItemEntityRenderer)
		EntityRendererRegistry.register(SoulOriginsEntities.LOYALTY_ITEM, ::FlyingItemEntityRenderer)
	}
}
