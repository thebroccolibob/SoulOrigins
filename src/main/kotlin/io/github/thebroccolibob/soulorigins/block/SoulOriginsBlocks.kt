package io.github.thebroccolibob.soulorigins.block

import io.github.thebroccolibob.soulorigins.FabricBlockSettings
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.minecraft.block.Block
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
        sounds(BlockSoundGroup.SLIME)
        nonOpaque()
    }))

    val DECAYING_SLIME = register("decaying_slime", DecayingSlimeBlock(200, 240, FabricBlockSettings {
        breakInstantly()
        strength(0f)
        sounds(BlockSoundGroup.SLIME)
    }))

    fun register() {}
}
