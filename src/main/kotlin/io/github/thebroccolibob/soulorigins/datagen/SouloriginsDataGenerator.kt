package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.datagen.power.PowerGenerator
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.advancement.Advancement
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models
import java.util.function.Consumer

object SouloriginsDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		with (fabricDataGenerator.createPack()) {
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
}
