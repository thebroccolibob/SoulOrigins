package io.github.thebroccolibob.soulorigins.block

import io.github.thebroccolibob.soulorigins.FabricBlockSettings
import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.SoulOriginsSounds
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.Identifier

object SoulOriginsBlocks {
    private fun <T: Block> register(id: Identifier, block: T): T {
        return Registry.register(Registries.BLOCK, id, block)
    }

    private fun <T: Block> register(path: String, block: T) = register(SoulOrigins.id(path), block)

    private inline operator fun <T: Block> ((settings: AbstractBlock.Settings) -> T).invoke(init: FabricBlockSettings.() -> Unit): T {
        return this(FabricBlockSettings(init))
    }

    private inline fun <T: Block> register(path: String, noinline constructor: (AbstractBlock.Settings) -> T, settings: FabricBlockSettings.() -> Unit): T {
        return register(path, constructor(FabricBlockSettings(settings)))
    }

    val COMPLETE: BooleanProperty = BooleanProperty.of("complete")

    val DECAYING_ROTTEN_FLESH = register("decaying_rotten_flesh", DecayingBlock(100, 200, FabricBlockSettings {
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

    val DECAYING_SAND = register("decaying_sand", DecayingBlock(100, 200, FabricBlockSettings {
        strength(0.5f)
        sounds(BlockSoundGroup.SAND)
    }))

    val FALLING_DECAYING_SAND = register("falling_decaying_sand", TransformingFallingBlock(DECAYING_SAND.defaultState, FabricBlockSettings {
        strength(0.5f)
        sounds(BlockSoundGroup.SAND)
        dropsLike(DECAYING_SAND)
    }))

    val DECAYING_COBWEB_BLOCK = register("decaying_cobweb_block", DecayingCobwebBlock(80, 100, FabricBlockSettings {
        noCollision()
        strength(0.5f)
        sounds(BlockSoundGroup.STONE)
        nonOpaque()
    }))

    val DECAYING_WATER = register("decaying_water", DecayingWaterBlock(30, 40, FabricBlockSettings {
        noCollision()
        replaceable()
        dropsNothing()
        noBlockBreakParticles()
        pistonBehavior(PistonBehavior.DESTROY)
    }))

    @JvmField
    val GARDEN_SCULK = register("garden_sculk", ::GardenSculkBlock) {
        strength(0.2F)
        sounds(BlockSoundGroup.SCULK)
        allowsSpawning(Blocks::always)
        solidBlock(Blocks::always)
        blockVision(Blocks::always)
        suffocates(Blocks::always)
        dropsLike(Blocks.SCULK)
    }

    val ARTIFICER_SURFACE = register("artificer_surface", ::PillarSurfaceBlock) {
        hardness(5f)
        resistance(6f)
        sounds(SoulOriginsSounds.ARTIFICER_SURFACE_GROUP)
    }

    val ARTIFICER_COLUMN = register("artificer_column", ::SurfaceBlock) {
        hardness(5f)
        resistance(6f)
        sounds(SoulOriginsSounds.ARTIFICER_SURFACE_GROUP)
    }

    val ARTIFICER_EW_WALL_BUILDER = register("artificer_ew_wall_builder", LoyaltySurfaceBuilderBlock(5, 1, 0, ARTIFICER_SURFACE.rotationZ, 400, FabricBlockSettings {
        hardness(20f)
        resistance(12f)
        sounds(BlockSoundGroup.METAL)
        luminance(9)
        pistonBehavior(PistonBehavior.BLOCK)
    }))

    val ARTIFICER_NS_WALL_BUILDER = register("artificer_ns_wall_builder", LoyaltySurfaceBuilderBlock(0, 1, 5, ARTIFICER_SURFACE.rotationX, 400, FabricBlockSettings {
        hardness(20f)
        resistance(12f)
        sounds(BlockSoundGroup.METAL)
        luminance(9)
        pistonBehavior(PistonBehavior.BLOCK)
    }))

    val ARTIFICER_PLATFORM_BUILDER = register("artificer_platform_builder", LoyaltySurfaceBuilderBlock(2, 0, 2, ARTIFICER_SURFACE.rotationY, 300, FabricBlockSettings {
        hardness(20f)
        resistance(12f)
        sounds(BlockSoundGroup.METAL)
        luminance(9)
        pistonBehavior(PistonBehavior.BLOCK)
    }))

    val ARTIFICER_COLUMN_BUILDER = register("artificer_column_builder", LoyaltySurfaceBuilderBlock(0, 4, 0, ARTIFICER_COLUMN, 300, FabricBlockSettings {
        hardness(20f)
        resistance(12f)
        sounds(BlockSoundGroup.METAL)
        luminance(9)
        pistonBehavior(PistonBehavior.BLOCK)
    }))

    fun register() {}
}
