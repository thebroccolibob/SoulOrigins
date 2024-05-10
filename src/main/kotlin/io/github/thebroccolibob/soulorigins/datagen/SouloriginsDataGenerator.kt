package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.datagen.power.LeveledCooldownEntry
import io.github.thebroccolibob.soulorigins.datagen.power.PowerGenerator
import io.github.thebroccolibob.soulorigins.datagen.power.wind.tailwindEntries
import io.github.thebroccolibob.soulorigins.datagen.power.wind.updraftEntries
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.advancement.Advancement
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models
import java.util.function.Consumer

object SouloriginsDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		fabricDataGenerator.createPack().apply {
			addProvider(::ModelGenerator)
			addProvider(::LangGenerator)
			addProvider(::PowerGenerator)
			addProvider(::AdvancementGenerator)
		}
	}

	class ModelGenerator(output: FabricDataOutput) : FabricModelProvider(output) {
		override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {

		}

		override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
			itemModelGenerator.register(SouloriginsItems.UPDRAFT_CRYSTAL, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.TAILWIND_CRYSTAL, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.BURST_CRYSTAL, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.NEUTRAL_CRYSTAL, Models.GENERATED)
		}
	}

	class LangGenerator(dataOutput: FabricDataOutput?) : FabricLanguageProvider(dataOutput) {
		private val romanNumerals = mapOf(
			1 to "I",
			2 to "II",
			3 to "III",
			4 to "IV",
			5 to "V"
		)

		private val updraftDescription = fun(level: Int) = when(level) {
			1 -> "Unlocks jumping"
			2 -> "Increases height"
			3 -> null
			4 -> "Increases height"
			5 -> "Increases height"
			else -> null
		}

		private val tailwindDescription = fun(level: Int) = when(level) {
			1 -> "Unlocks dashing"
			2 -> "Increases speed"
			3 -> null
			4 -> "Increases speed"
			5 -> "Increases speed"
			else -> null
		}

		private fun TranslationBuilder.cooldownDescriptions(id: String, name: String, entries: Iterable<LeveledCooldownEntry>, description: (Int) -> String?) {
			entries.forEach {
				val level = it.level
				add("advancements.soul-origins.wind.$id.lvl$level", "$name ${romanNumerals[level]}")
				add("advancements.soul-origins.wind.$id.lvl$level.description", (description(level)?.let {desc->"$desc\n"} ?: "") + "${it.charges} charge${if (it.charges == 1) "" else "s"}\n${it.recharge / 20}s")
			}
		}

		override fun generateTranslations(translationBuilder: TranslationBuilder) {
			translationBuilder.add(SouloriginsItems.MARIGOLD_CARD, "Marigold Card")
			translationBuilder.add("${SouloriginsItems.MARIGOLD_CARD.translationKey}.empty", "Empty")
			translationBuilder.add("${SouloriginsItems.MARIGOLD_CARD.translationKey}.multiple_items", "%s x%s")
			translationBuilder.add("container.soul-origins.inventory.deck", "Deck")

			translationBuilder.cooldownDescriptions("updraft", "Updraft", updraftEntries, updraftDescription)
			translationBuilder.cooldownDescriptions("tailwind", "Tailwind", tailwindEntries, tailwindDescription)
		}
	}

	class AdvancementGenerator(output: FabricDataOutput) : FabricAdvancementProvider(output) {
		override fun generateAdvancement(consumer: Consumer<Advancement>) {
			consumer.generateAdvancements()
		}

	}
}
