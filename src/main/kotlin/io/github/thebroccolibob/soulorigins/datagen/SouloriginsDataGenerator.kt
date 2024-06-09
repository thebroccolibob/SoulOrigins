package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.Soulorigins
import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import io.github.thebroccolibob.soulorigins.datagen.lib.*
import io.github.thebroccolibob.soulorigins.datagen.power.PowerGenerator
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.advancement.Advancement
import net.minecraft.block.Blocks
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models
import net.minecraft.data.client.TexturedModel
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.Identifier
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

	class ModelGenerator(output: FabricDataOutput) : FabricModelProvider(output) {
		override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
			blockStateModelGenerator.registerStateWithModelReference(SoulOriginsBlocks.DECAYING_SLIME, Blocks.SLIME_BLOCK)
			blockStateModelGenerator.registerStateWithModelReference(SoulOriginsBlocks.HUSK_SAND, Blocks.SAND)
			blockStateModelGenerator.registerSimpleCubeAll(SoulOriginsBlocks.DECAYING_ROTTEN_FLESH)

			blockStateModelGenerator.registerSimpleCubeAll(SoulOriginsBlocks.ARTIFICER_SURFACE)
			val artificerBuilder = TexturedModel.getCubeAll(Identifier(Soulorigins.MOD_ID, "artificer_builder"))
			blockStateModelGenerator.registerSingleton(SoulOriginsBlocks.ARTIFICER_PLATFORM_BUILDER, artificerBuilder.textures, artificerBuilder.model)
			blockStateModelGenerator.registerSingleton(SoulOriginsBlocks.ARTIFICER_NS_WALL_BUILDER, artificerBuilder.textures, artificerBuilder.model)
			blockStateModelGenerator.registerSingleton(SoulOriginsBlocks.ARTIFICER_EW_WALL_BUILDER, artificerBuilder.textures, artificerBuilder.model)
		}

		override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
			itemModelGenerator.register(SouloriginsItems.UPDRAFT_SHARD, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.TAILWIND_SHARD, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.BURST_SHARD, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.ARTIFICER_PLATFORM_BUILDER, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.ARTIFICER_WALL_BUILDER, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.MOB_ORB, "_zombie", Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.MOB_ORB, "_husk", Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.MOB_ORB, "_drowned", Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.MOB_ORB, "_skeleton", Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.MOB_ORB, "_stray", Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.MOB_ORB, "_witherskeleton", Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.MOB_ORB, "_spider", Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.MOB_ORB, "_cavespider", Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.MOB_ORB, "_creeper", Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.MOB_ORB, "_enderman", Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.MOB_ORB, "_slime", Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.MOB_ORB, "_warden", Models.GENERATED)
		}
	}

	class AdvancementGenerator(output: FabricDataOutput) : FabricAdvancementProvider(output) {
		override fun generateAdvancement(consumer: Consumer<Advancement>) {
			consumer.generateAdvancements()
		}
	}

	class BlockLootTableGenerator(dataOutput: FabricDataOutput) : FabricBlockLootTableProvider(dataOutput) {
		override fun generate() {
			addTable(SoulOriginsBlocks.DECAYING_SLIME) {
				pool {
					item(Items.SLIME_BALL) {
						count(constant(1))
					}
					conditions {
						randomChance(0.5f)
						survivesExplosion()
					}
				}
			}

			addTable(SoulOriginsBlocks.DECAYING_ROTTEN_FLESH) {
				pool {
					item(Items.ROTTEN_FLESH) {
						count(constant(1))
					}
					conditions {
						randomChance(0.25f)
						survivesExplosion()
					}
				}
			}

			addTable(SoulOriginsBlocks.HUSK_SAND) {
				pool {
					item(Items.SAND) {
						count(constant(1))
					}
					conditions {
						randomChance(0.5f)
						survivesExplosion()
					}
				}
			}

			addDrop(SoulOriginsBlocks.ARTIFICER_PLATFORM_BUILDER, SouloriginsItems.ARTIFICER_PLATFORM_BUILDER)
			addDrop(SoulOriginsBlocks.ARTIFICER_EW_WALL_BUILDER, SouloriginsItems.ARTIFICER_WALL_BUILDER)
			addDrop(SoulOriginsBlocks.ARTIFICER_NS_WALL_BUILDER, SouloriginsItems.ARTIFICER_WALL_BUILDER)
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
				SoulOriginsBlocks.HUSK_SAND
			)
			getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(
				SoulOriginsBlocks.ARTIFICER_SURFACE,
				SoulOriginsBlocks.ARTIFICER_PLATFORM_BUILDER,
				SoulOriginsBlocks.ARTIFICER_NS_WALL_BUILDER,
				SoulOriginsBlocks.ARTIFICER_EW_WALL_BUILDER,
			)
		}
	}
}
