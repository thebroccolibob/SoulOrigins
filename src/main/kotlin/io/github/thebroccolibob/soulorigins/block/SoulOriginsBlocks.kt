package io.github.thebroccolibob.soulorigins.block

import io.github.thebroccolibob.soulorigins.FabricBlockSettings
import io.github.thebroccolibob.soulorigins.SoulOriginsSounds
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.FallingBlock
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object SoulOriginsBlocks {
    private fun register(id: Identifier, block: Block): Block {
        return Registry.register(Registries.BLOCK, id, block)
    }

    private fun register(path: String, block: Block) = register(Identifier(Soulorigins.MOD_ID, path), block)

    private operator fun Block.provideDelegate(thisRef: Any, property: KProperty<*>): ReadOnlyProperty<Any, Block> {
        register(property.name.lowercase(), this)
        return ReadOnlyProperty { _, _ -> this@provideDelegate }
    }

    private inline operator fun ((settings: AbstractBlock.Settings) -> Block).invoke(init: FabricBlockSettings.() -> Unit): Block {
        return this(FabricBlockSettings(init))
    }

    val DECAYING_ROTTEN_FLESH = register("decaying_rotten_flesh", DecayingBlock(100, 200, FabricBlockSettings {
        hardness(0.5f)
        strength(0.5f)
        burnable()
        sounds(BlockSoundGroup.FROGLIGHT)
    }))

    val DECAYING_SLIME = register("decaying_slime", DecayingSlimeBlock(200, 240, FabricBlockSettings {
        breakInstantly()
        strength(0f)
        sounds(BlockSoundGroup.SLIME)
        nonOpaque()
    }))

    val HUSK_SAND = register("decaying_sand", FallingBlock(FabricBlockSettings {
        hardness(0.5f)
        strength(0.5f)
        sounds(BlockSoundGroup.SAND)
    }))

    @JvmField
    val GARDEN_SCULK = register("garden_sculk", GardenSculkBlock(FabricBlockSettings {
        strength(0.2F)
        sounds(BlockSoundGroup.SCULK)
        allowsSpawning(Blocks::always)
        solidBlock(Blocks::always)
        blockVision(Blocks::always)
        suffocates(Blocks::always)
        dropsLike(Blocks.SCULK)
    }))

    val ARTIFICER_SURFACE = register("artificer_surface", ::SurfaceBlock {
        hardness(5f)
        strength(6f)
        sounds(SoulOriginsSounds.ARTIFICER_SURFACE_GROUP)
    })

    val ARTIFICER_EW_WALL_BUILDER = register("artificer_ew_wall_builder", LoyaltySurfaceBuilderBlock(5, 1, 0, ARTIFICER_SURFACE, 400, FabricBlockSettings {
        hardness(10f)
        strength(12f)
        sounds(BlockSoundGroup.METAL)
    }))

    val ARTIFICER_NS_WALL_BUILDER = register("artificer_ns_wall_builder", LoyaltySurfaceBuilderBlock(0, 1, 5, ARTIFICER_SURFACE, 400, FabricBlockSettings {
        hardness(10f)
        strength(12f)
        sounds(BlockSoundGroup.METAL)
    }))

    val ARTIFICER_PLATFORM_BUILDER = register("artificer_platform_builder", LoyaltySurfaceBuilderBlock(2, 0, 2, ARTIFICER_SURFACE, 300, FabricBlockSettings {
        hardness(10f)
        strength(12f)
        sounds(BlockSoundGroup.METAL)
    }))

    fun register() {}
}
