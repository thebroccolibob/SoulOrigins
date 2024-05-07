package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.datagen.power.PowerGenerator
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
			addProvider(::UpgradeFunctionGenerator)
			addProvider(::AdvancementGenerator)
		}
	}

	class ModelGenerator(output: FabricDataOutput) : FabricModelProvider(output) {
		override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {

		}

		override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
			itemModelGenerator.register(SouloriginsItems.JUMP_CRYSTAL, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.DASH_CRYSTAL, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.BURST_CRYSTAL, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.BARRIER_CRYSTAL, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.NEUTRAL_CRYSTAL, Models.GENERATED)
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

	class AdvancementGenerator(output: FabricDataOutput) : FabricAdvancementProvider(output) {
		override fun generateAdvancement(consumer: Consumer<Advancement>) {
			consumer.generateAdvancements()
		}

	}
}
