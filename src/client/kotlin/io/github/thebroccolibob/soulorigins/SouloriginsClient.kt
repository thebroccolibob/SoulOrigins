package io.github.thebroccolibob.soulorigins

import io.github.thebroccolibob.soulorigins.client.particle.GustEmitterParticle
import io.github.thebroccolibob.soulorigins.client.particle.GustParticle
import io.github.thebroccolibob.soulorigins.item.MarigoldCardItem.Companion.hasEntity
import io.github.thebroccolibob.soulorigins.item.MobOrbItem
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.minecraft.client.item.ModelPredicateProviderRegistry
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
			MobOrbItem.getMobType(itemStack).toFloat()
		}

		with (ParticleFactoryRegistry.getInstance()) {
			register(SoulOriginsParticles.GUST, GustParticle::Factory)
			register(SoulOriginsParticles.SMALL_GUST, GustParticle::SmallGustFactory)
			register(SoulOriginsParticles.GUST_EMITTER_LARGE, GustEmitterParticle.Factory(3.0, 12, 0))
			register(SoulOriginsParticles.GUST_EMITTER_SMALL, GustEmitterParticle.Factory(1.0, 3, 2))
		}
	}
}
