package io.github.thebroccolibob.soulorigins

import io.github.thebroccolibob.soulorigins.action.SoulOriginsEntityActions
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import io.github.thebroccolibob.soulorigins.power.registerSoulOriginsPowers
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Soulorigins : ModInitializer {
	const val MOD_ID = "soul-origins"

	@JvmField
    val logger: Logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		SouloriginsItems.register()
		SoulOriginsEntityActions.register()
		registerSoulOriginsPowers()
	}
}
