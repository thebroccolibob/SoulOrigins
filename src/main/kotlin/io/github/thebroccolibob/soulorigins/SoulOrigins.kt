package io.github.thebroccolibob.soulorigins

import io.github.thebroccolibob.soulorigins.action.registerSoulOriginsBiEntityActions
import io.github.thebroccolibob.soulorigins.action.registerSoulOriginsBlockActions
import io.github.thebroccolibob.soulorigins.action.registerSoulOriginsEntityActions
import io.github.thebroccolibob.soulorigins.action.registerSoulOriginsItemActions
import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import io.github.thebroccolibob.soulorigins.block.entity.SoulOriginsBlockEntities
import io.github.thebroccolibob.soulorigins.condition.registerSoulOriginsBiEntityConditions
import io.github.thebroccolibob.soulorigins.condition.registerSoulOriginsEntityConditions
import io.github.thebroccolibob.soulorigins.condition.registerSoulOriginsItemConditions
import io.github.thebroccolibob.soulorigins.effect.SoulOriginsEffects
import io.github.thebroccolibob.soulorigins.entity.SoulOriginsEntities
import io.github.thebroccolibob.soulorigins.item.SoulOriginsItems
import io.github.thebroccolibob.soulorigins.potion.SoulOriginsPotions
import io.github.thebroccolibob.soulorigins.power.registerSoulOriginsPowers
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.ResourcePackActivationType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object SoulOrigins : ModInitializer {
	const val MOD_ID = "soul-origins"

	@JvmStatic
	fun id(path: String) = Identifier(MOD_ID, path)

	@JvmField
	@Suppress("unused")
    val logger: Logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		SoulOriginsBlocks.register()
		SoulOriginsItems.register()
		SoulOriginsBlockEntities.register()
		SoulOriginsEntities.register()
		registerSoulOriginsBiEntityActions()
		registerSoulOriginsEntityActions()
		registerSoulOriginsItemActions()
		registerSoulOriginsBlockActions()
		registerSoulOriginsItemConditions()
		registerSoulOriginsEntityConditions()
		registerSoulOriginsBiEntityConditions()
		registerSoulOriginsPowers()
		SoulOriginsParticles.register()
		SoulOriginsSounds.register()
		SoulOriginsEffects.register()
		SoulOriginsPotions.register()
		SoulOriginsTags.register()
		registerSoulOriginsLootModifier()

		ResourceManagerHelper.registerBuiltinResourcePack(
            id("whitespace"),
            FabricLoader.getInstance().getModContainer(MOD_ID).get(),
            ResourcePackActivationType.NORMAL,
        )

		ResourceManagerHelper.registerBuiltinResourcePack(
			id("create_compat"),
			FabricLoader.getInstance().getModContainer(MOD_ID).get(),
			ResourcePackActivationType.NORMAL,
		)
	}
}
