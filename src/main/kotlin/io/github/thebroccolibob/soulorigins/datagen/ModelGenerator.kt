package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.block.BeeBombBlock
import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import io.github.thebroccolibob.soulorigins.datagen.lib.*
import io.github.thebroccolibob.soulorigins.item.SoulOriginsItems
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

            val artificerBuilder = upload(Models.CUBE_ALL, SoulOrigins.id("block/artificer_builder"), TextureMap.all(SoulOrigins.id("block/artificer_builder")))

            blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(SoulOriginsBlocks.ARTIFICER_PLATFORM_BUILDER, artificerBuilder))
            blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(SoulOriginsBlocks.ARTIFICER_NS_WALL_BUILDER, artificerBuilder))
            blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(SoulOriginsBlocks.ARTIFICER_EW_WALL_BUILDER, artificerBuilder))
            blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(SoulOriginsBlocks.ARTIFICER_COLUMN_BUILDER, artificerBuilder))

            registerStates(SoulOriginsBlocks.BEE_BOMB, BeeBombBlock.LIT) { lit ->
                BlockStateVariant(
                    if (lit)
                        upload(Models.CUBE_BOTTOM_TOP, SoulOriginsBlocks.BEE_BOMB.modelId("_lit")) {
                            TextureKey.TOP to SoulOriginsBlocks.BEE_BOMB.textureId("_top_lit")
                            TextureKey.SIDE to SoulOriginsBlocks.BEE_BOMB.textureId("_side_lit")
                            TextureKey.BOTTOM to SoulOriginsBlocks.BEE_BOMB.textureId("_bottom_lit")
                        }
                    else
                        upload(Models.CUBE_BOTTOM_TOP, SoulOriginsBlocks.BEE_BOMB) {
                            TextureKey.TOP to Blocks.BEE_NEST.textureId("_top")
                            TextureKey.SIDE to SoulOriginsBlocks.BEE_BOMB.textureId("_side")
                            TextureKey.BOTTOM to Blocks.BEE_NEST.textureId("_bottom")
                        }
                )
            }
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
            register(SoulOriginsItems.UPDRAFT_SHARD)
            register(SoulOriginsItems.TAILWIND_SHARD)
            register(SoulOriginsItems.BURST_SHARD)
            register(SoulOriginsItems.ARTIFICER_CORE)
            register(SoulOriginsItems.ARTIFICER_PLATFORM_BUILDER)
            register(SoulOriginsItems.ARTIFICER_WALL_BUILDER)
            register(SoulOriginsItems.ARTIFICER_COLUMN_BUILDER)
            register(SoulOriginsItems.MOB_ORB, "_zombie")
            register(SoulOriginsItems.MOB_ORB, "_husk")
            register(SoulOriginsItems.MOB_ORB, "_drowned")
            register(SoulOriginsItems.MOB_ORB, "_skeleton")
            register(SoulOriginsItems.MOB_ORB, "_stray")
            register(SoulOriginsItems.MOB_ORB, "_witherskeleton")
            register(SoulOriginsItems.MOB_ORB, "_spider")
            register(SoulOriginsItems.MOB_ORB, "_cavespider")
            register(SoulOriginsItems.MOB_ORB, "_creeper")
            register(SoulOriginsItems.MOB_ORB, "_enderman")
            register(SoulOriginsItems.MOB_ORB, "_slime")
            register(SoulOriginsItems.MOB_ORB, "_warden")
            register(SoulOriginsItems.SUSPICIOUS_BREW)
            register(SoulOriginsItems.SPLASH_SUSPICIOUS_BREW)
            register(SoulOriginsItems.LINGERING_SUSPICIOUS_BREW)
        }
    }

    private fun BlockStateModelGenerator.registerRotatable(block: Block, modelId: Identifier) {
        blockStateCollector.accept(VariantsBlockStateSupplier.create(block, *BlockStateModelGenerator.createModelVariantWithRandomHorizontalRotations(modelId)))
    }

    private fun BlockStateModelGenerator.registerRotatableWithReference(block: Block, modelReference: Block) {
        registerRotatable(block, ModelIds.getBlockModelId(modelReference))
    }
}
