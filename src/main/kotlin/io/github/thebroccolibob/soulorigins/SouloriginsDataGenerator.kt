package io.github.thebroccolibob.soulorigins

import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models

object SouloriginsDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		fabricDataGenerator.createPack().apply {
			addProvider(::ModelGenerator)
			addProvider(::LangGenerator)
		}
	}

	class ModelGenerator(output: FabricDataOutput) : FabricModelProvider(output) {
		override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {

		}

		override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
			itemModelGenerator.register(SouloriginsItems.MARIGOLD_CARD, Models.GENERATED);
		}
	}

	class LangGenerator(dataOutput: FabricDataOutput?) : FabricLanguageProvider(dataOutput) {
		override fun generateTranslations(translationBuilder: TranslationBuilder) {
			translationBuilder.add(SouloriginsItems.MARIGOLD_CARD, "Marigold Card")
			translationBuilder.add("${SouloriginsItems.MARIGOLD_CARD.translationKey}.empty", "Empty")
		}
	}
}
