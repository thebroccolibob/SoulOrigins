package io.github.thebroccolibob.soulorigins.datagen

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
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models
import net.minecraft.item.Items
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

	class ModelGenerator(output: FabricDataOutput) : FabricModelProvider(output) {
		override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
			blockStateModelGenerator.registerStateWithModelReference(SoulOriginsBlocks.DECAYING_SLIME, Blocks.SLIME_BLOCK)
			blockStateModelGenerator.registerStateWithModelReference(SoulOriginsBlocks.DECAYING_SAND, Blocks.SAND)
			blockStateModelGenerator.registerStateWithModelReference(SoulOriginsBlocks.GARDEN_SCULK, Blocks.SCULK)
			blockStateModelGenerator.registerSimpleCubeAll(SoulOriginsBlocks.DECAYING_ROTTEN_FLESH)
		}

		override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
			itemModelGenerator.register(SouloriginsItems.UPDRAFT_SHARD, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.TAILWIND_SHARD, Models.GENERATED)
			itemModelGenerator.register(SouloriginsItems.BURST_SHARD, Models.GENERATED)
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

			addTable(SoulOriginsBlocks.DECAYING_SAND) {
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
		}
	}
}
