@file:Suppress("OVERRIDE_DEPRECATION")

package io.github.thebroccolibob.soulorigins.block

import net.minecraft.block.BlockState
import net.minecraft.block.FallingBlock
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random

class TransformingFallingBlock(private val transformState: BlockState, settings: Settings) : FallingBlock(settings) {
    override fun scheduledTick(state: BlockState?, world: ServerWorld?, pos: BlockPos?, random: Random?) {
        super.scheduledTick(transformState, world, pos, random)
    }
}
