package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.Soulorigins
import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import io.github.thebroccolibob.soulorigins.datagen.lib.upload
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.*
import net.minecraft.item.Item
import net.minecraft.util.Identifier

class ModelGenerator(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        with(blockStateModelGenerator) {
            registerStateWithModelReference(SoulOriginsBlocks.DECAYING_SLIME, Blocks.SLIME_BLOCK)
            registerStateWithModelReference(SoulOriginsBlocks.DECAYING_COBWEB_BLOCK, Blocks.COBWEB)
            registerRotatableWithReference(SoulOriginsBlocks.GARDEN_SCULK, Blocks.SCULK)
            registerRotatableWithReference(SoulOriginsBlocks.DECAYING_SAND, Blocks.SAND)
            registerRotatableWithReference(SoulOriginsBlocks.FALLING_DECAYING_SAND, Blocks.SAND)

            registerSimpleCubeAll(SoulOriginsBlocks.DECAYING_ROTTEN_FLESH)

            registerAxisRotated(SoulOriginsBlocks.ARTIFICER_SURFACE, TexturedModel.CUBE_COLUMN, TexturedModel.CUBE_COLUMN_HORIZONTAL)
            registerSingleton(SoulOriginsBlocks.ARTIFICER_COLUMN, TexturedModel.CUBE_COLUMN)

            val artificerBuilder = upload(Models.CUBE_ALL, Identifier(Soulorigins.MOD_ID, "block/artificer_builder"), TextureMap.all(Identifier(Soulorigins.MOD_ID, "block/artificer_builder")))

            blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(SoulOriginsBlocks.ARTIFICER_PLATFORM_BUILDER, artificerBuilder))
            blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(SoulOriginsBlocks.ARTIFICER_NS_WALL_BUILDER, artificerBuilder))
            blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(SoulOriginsBlocks.ARTIFICER_EW_WALL_BUILDER, artificerBuilder))
            blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(SoulOriginsBlocks.ARTIFICER_COLUMN_BUILDER, artificerBuilder))
        }
    }

    private fun ItemModelGenerator.register(item: Item) {
        register(item, Models.GENERATED)
    }

    private fun ItemModelGenerator.register(item: Item, suffix: String) {
        register(item, suffix, Models.GENERATED)
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
        with(itemModelGenerator) {
            register(SouloriginsItems.UPDRAFT_SHARD)
            register(SouloriginsItems.TAILWIND_SHARD)
            register(SouloriginsItems.BURST_SHARD)
            register(SouloriginsItems.ARTIFICER_PLATFORM_BUILDER)
            register(SouloriginsItems.ARTIFICER_WALL_BUILDER)
            register(SouloriginsItems.ARTIFICER_COLUMN_BUILDER)
            register(SouloriginsItems.MOB_ORB, "_zombie")
            register(SouloriginsItems.MOB_ORB, "_husk")
            register(SouloriginsItems.MOB_ORB, "_drowned")
            register(SouloriginsItems.MOB_ORB, "_skeleton")
            register(SouloriginsItems.MOB_ORB, "_stray")
            register(SouloriginsItems.MOB_ORB, "_witherskeleton")
            register(SouloriginsItems.MOB_ORB, "_spider")
            register(SouloriginsItems.MOB_ORB, "_cavespider")
            register(SouloriginsItems.MOB_ORB, "_creeper")
            register(SouloriginsItems.MOB_ORB, "_enderman")
            register(SouloriginsItems.MOB_ORB, "_slime")
            register(SouloriginsItems.MOB_ORB, "_warden")
            register(SouloriginsItems.SUSPICIOUS_BREW)
            register(SouloriginsItems.SPLASH_SUSPICIOUS_BREW)
            register(SouloriginsItems.LINGERING_SUSPICIOUS_BREW)
        }
    }

    private fun BlockStateModelGenerator.registerRotatable(block: Block, modelId: Identifier) {
        blockStateCollector.accept(VariantsBlockStateSupplier.create(block, *BlockStateModelGenerator.createModelVariantWithRandomHorizontalRotations(modelId)))
    }

    private fun BlockStateModelGenerator.registerRotatableWithReference(block: Block, modelReference: Block) {
        registerRotatable(block, ModelIds.getBlockModelId(modelReference))
    }
}
