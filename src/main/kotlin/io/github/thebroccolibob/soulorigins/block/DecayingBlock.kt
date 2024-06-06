package io.github.thebroccolibob.soulorigins.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class DecayingBlock(override val minDelay: Int, override val maxDelay: Int, settings: Settings) : Block(settings), Decaying {
    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        super<Decaying>.onBlockAdded(state, world, pos, oldState, notify)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        super<Decaying>.scheduledTick(state, world, pos, random)
    }
}
