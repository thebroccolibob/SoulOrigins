package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.SoulOriginsTags
import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import io.github.thebroccolibob.soulorigins.datagen.power.PowerGenerator
import io.github.thebroccolibob.soulorigins.potion.SoulOriginsPotions
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.advancement.Advancement
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.potion.Potion
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

object SoulOriginsDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		with (fabricDataGenerator.createPack()) {
			addProvider(::ModelGenerator)
			addProvider(::LangGenerator)
			addProvider(::PowerGenerator)
			addProvider(::AdvancementGenerator)
			addProvider(::BlockLootTableGenerator)
			addProvider(::BlockTagGenerator)
			addProvider(::PotionTagGenerator)
			addProvider(::RecipeGenerator)
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

	class PotionTagGenerator(
		output: FabricDataOutput,
		registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
	) : FabricTagProvider<Potion>(output, RegistryKeys.POTION, registriesFuture) {
		override fun configure(arg: RegistryWrapper.WrapperLookup?) {
			getOrCreateTagBuilder(SoulOriginsTags.SECRET_POTIONS).add(
				SoulOriginsPotions.MementoMori.stage0,
				SoulOriginsPotions.MementoMori.stage1,
				SoulOriginsPotions.MementoMori.stage2,
				SoulOriginsPotions.MementoMori.final,
				SoulOriginsPotions.MementoMori.final_long,
			)
		}
	}

	class RecipeGenerator(output: FabricDataOutput) : FabricRecipeProvider(output) {
		override fun generate(exporter: Consumer<RecipeJsonProvider>) {
			exporter.generateSoulOriginsRecipes()
		}

	}
}
