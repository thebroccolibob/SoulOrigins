package io.github.thebroccolibob.soulorigins

import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import io.github.thebroccolibob.soulorigins.client.particle.GustEmitterParticle
import io.github.thebroccolibob.soulorigins.client.particle.GustParticle
import io.github.thebroccolibob.soulorigins.entity.SoulOriginsEntities
import io.github.thebroccolibob.soulorigins.item.MarigoldCardItem.Companion.hasEntity
import io.github.thebroccolibob.soulorigins.item.MobOrbItem
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.util.Identifier

object SouloriginsClient : ClientModInitializer {
	override fun onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ModelPredicateProviderRegistry.register(SouloriginsItems.MARIGOLD_CARD, Identifier(Soulorigins.MOD_ID, "has_entity")) {
			itemStack, _, _, _ ->
			if (itemStack.hasEntity) 1f else 0f
		}
		ModelPredicateProviderRegistry.register(SouloriginsItems.MOB_ORB, Identifier(Soulorigins.MOD_ID, "mob_type")) {
				itemStack, _, _, _ ->
			MobOrbItem.getMobType(itemStack)
		}

		with (ParticleFactoryRegistry.getInstance()) {
			register(SoulOriginsParticles.GUST, GustParticle::Factory)
			register(SoulOriginsParticles.SMALL_GUST, GustParticle::SmallGustFactory)
			register(SoulOriginsParticles.GUST_EMITTER_LARGE, GustEmitterParticle.Factory(3.0, 12, 0))
			register(SoulOriginsParticles.GUST_EMITTER_SMALL, GustEmitterParticle.Factory(1.0, 3, 2))
		}

		BlockRenderLayerMap.INSTANCE.putBlock(SoulOriginsBlocks.DECAYING_SLIME, RenderLayer.getTranslucent())

		EntityRendererRegistry.register(SoulOriginsEntities.SURFACE_BUILDER_PROJECTILE, ::FlyingItemEntityRenderer)
		EntityRendererRegistry.register(SoulOriginsEntities.LOYALTY_ITEM, ::FlyingItemEntityRenderer)
	}
}
