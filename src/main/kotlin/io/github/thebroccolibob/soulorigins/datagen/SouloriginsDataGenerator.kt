package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.datagen.power.PowerGenerator
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator

object SouloriginsDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		fabricDataGenerator.createPack().apply {
			addProvider(::ModelGenerator)
			addProvider(::LangGenerator)
			addProvider(::PowerGenerator)
		}
	}

	class ModelGenerator(output: FabricDataOutput) : FabricModelProvider(output) {
		override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {

		}

		override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

		}
	}

	class LangGenerator(dataOutput: FabricDataOutput?) : FabricLanguageProvider(dataOutput) {
		override fun generateTranslations(translationBuilder: TranslationBuilder) {
			translationBuilder.add(SouloriginsItems.MARIGOLD_CARD, "Marigold Card")
			translationBuilder.add("${SouloriginsItems.MARIGOLD_CARD.translationKey}.empty", "Empty")
			translationBuilder.add("${SouloriginsItems.MARIGOLD_CARD.translationKey}.multiple_items", "%s x%s")
			translationBuilder.add("container.soul-origins.inventory.deck", "Deck")
		}
	}
}
