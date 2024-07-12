package io.github.thebroccolibob.soulorigins.block.entity

import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import io.github.thebroccolibob.soulorigins.id
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

typealias BlockEntityFactory<T> = FabricBlockEntityTypeBuilder.Factory<T>

object SoulOriginsBlockEntities {
    private fun <T: BlockEntity> register(id: Identifier, blockEntity: BlockEntityFactory<T>, vararg blocks: Block): BlockEntityType<T> {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.create(blockEntity, *blocks).build())
    }

    private fun <T: BlockEntity> register(path: String, blockEntity: BlockEntityFactory<T>, vararg blocks: Block): BlockEntityType<T> {
        return register(SoulOrigins.id(path), blockEntity, *blocks)
    }

    private fun <T: BlockEntity> register(block: Block, blockEntity: BlockEntityFactory<T>): BlockEntityType<T> {
        return register(block.id, blockEntity, block)
    }

    val SURFACE_BUILDER = register("surface_builder", ::LoyaltySurfaceBuilderBlockEntity, SoulOriginsBlocks.ARTIFICER_PLATFORM_BUILDER, SoulOriginsBlocks.ARTIFICER_NS_WALL_BUILDER, SoulOriginsBlocks.ARTIFICER_EW_WALL_BUILDER)
    val BEE_BOMB = register("bee_bomb", ::BeeBombBlockEntity, SoulOriginsBlocks.BEE_BOMB)

    fun register() {}
}
