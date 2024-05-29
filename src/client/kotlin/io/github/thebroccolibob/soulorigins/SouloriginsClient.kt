package io.github.thebroccolibob.soulorigins

import io.github.thebroccolibob.soulorigins.item.MarigoldCardItem.Companion.hasEntity
import io.github.thebroccolibob.soulorigins.item.MobOrbItem
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import net.fabricmc.api.ClientModInitializer
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
	}
}
