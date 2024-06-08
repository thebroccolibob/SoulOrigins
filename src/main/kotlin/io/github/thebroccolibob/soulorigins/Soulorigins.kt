package io.github.thebroccolibob.soulorigins

import io.github.thebroccolibob.soulorigins.action.registerSoulOriginsBiEntityActions
import io.github.thebroccolibob.soulorigins.action.registerSoulOriginsEntityActions
import io.github.thebroccolibob.soulorigins.action.registerSoulOriginsItemActions
import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import io.github.thebroccolibob.soulorigins.block.entity.SoulOriginsBlockEntities
import io.github.thebroccolibob.soulorigins.condition.registerSoulOriginsBiEntityConditions
import io.github.thebroccolibob.soulorigins.condition.registerSoulOriginsEntityConditions
import io.github.thebroccolibob.soulorigins.condition.registerSoulOriginsItemConditions
import io.github.thebroccolibob.soulorigins.entity.SoulOriginsEntities
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import io.github.thebroccolibob.soulorigins.power.registerSoulOriginsPowers
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Soulorigins : ModInitializer {
	const val MOD_ID = "soul-origins"

	@JvmField
	@Suppress("unused")
    val logger: Logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		SoulOriginsBlocks.register()
		SouloriginsItems.register()
		SoulOriginsBlockEntities.register()
		SoulOriginsEntities.register()
		registerSoulOriginsBiEntityActions()
		registerSoulOriginsEntityActions()
		registerSoulOriginsItemActions()
		registerSoulOriginsItemConditions()
		registerSoulOriginsEntityConditions()
		registerSoulOriginsBiEntityConditions()
		registerSoulOriginsPowers()
		SoulOriginsParticles.register()
		SoulOriginsSounds.register()
	}
}
