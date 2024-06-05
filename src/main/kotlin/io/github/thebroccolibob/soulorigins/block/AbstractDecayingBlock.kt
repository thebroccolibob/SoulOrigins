package io.github.thebroccolibob.soulorigins.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

interface AbstractDecayingBlock {
    fun onBlockAdded(
        state: BlockState,
        world: World,
        pos: BlockPos,
        oldState: BlockState,
        notify: Boolean
    ) {
        world.scheduleBlockTick(pos, this as Block, world.random.nextBetween(minDelay, maxDelay))
    }

    fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        world.breakBlock(pos, true)
    }

    val minDelay: Int
    val maxDelay: Int
}
