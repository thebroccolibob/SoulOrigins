package io.github.thebroccolibob.soulorigins.block

import io.github.thebroccolibob.soulorigins.FabricBlockSettings
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

object SoulOriginsBlocks {
    private fun register(id: Identifier, block: Block): Block {
        return Registry.register(Registries.BLOCK, id, block)
    }

    private fun register(path: String, block: Block) = register(Identifier(Soulorigins.MOD_ID, path), block)

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

    val DECAYING_SAND = register("decaying_sand", DecayingBlock(100, 200, FabricBlockSettings {
        hardness(0.5f)
        strength(0.5f)
        sounds(BlockSoundGroup.SAND)
    }))

    val FALLING_DECAYING_SAND = register("falling_decaying_sand", TransformingFallingBlock(DECAYING_SAND.defaultState, FabricBlockSettings {
        hardness(0.5f)
        strength(0.5f)
        sounds(BlockSoundGroup.SAND)
        dropsLike(DECAYING_SAND)
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

    fun register() {}
}
