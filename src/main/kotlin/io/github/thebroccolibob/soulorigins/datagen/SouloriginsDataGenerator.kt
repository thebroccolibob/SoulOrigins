package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import io.github.thebroccolibob.soulorigins.datagen.power.PowerGenerator
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.advancement.Advancement
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

object SouloriginsDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		with (fabricDataGenerator.createPack()) {
			addProvider(::ModelGenerator)
			addProvider(::LangGenerator)
			addProvider(::PowerGenerator)
			addProvider(::AdvancementGenerator)
			addProvider(::BlockLootTableGenerator)
			addProvider(::BlockTagGenerator)
		}
	}

	class AdvancementGenerator(output: FabricDataOutput) : FabricAdvancementProvider(output) {
		override fun generateAdvancement(consumer: Consumer<Advancement>) {
			consumer.generateAdvancements()
		}
	}

	class BlockTagGenerator(
		output: FabricDataOutput,
		registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
	) : FabricTagProvider.BlockTagProvider(output, registriesFuture) {
		override fun configure(arg: RegistryWrapper.WrapperLookup?) {
			getOrCreateTagBuilder(BlockTags.HOE_MINEABLE).add(
				SoulOriginsBlocks.DECAYING_ROTTEN_FLESH,
				SoulOriginsBlocks.GARDEN_SCULK
			)
			getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE).add(
				SoulOriginsBlocks.DECAYING_SAND
			)
			getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(
				SoulOriginsBlocks.ARTIFICER_SURFACE,
				SoulOriginsBlocks.ARTIFICER_COLUMN,
				SoulOriginsBlocks.ARTIFICER_PLATFORM_BUILDER,
				SoulOriginsBlocks.ARTIFICER_NS_WALL_BUILDER,
				SoulOriginsBlocks.ARTIFICER_EW_WALL_BUILDER,
				SoulOriginsBlocks.ARTIFICER_COLUMN_BUILDER,
			)
		}
	}
}
