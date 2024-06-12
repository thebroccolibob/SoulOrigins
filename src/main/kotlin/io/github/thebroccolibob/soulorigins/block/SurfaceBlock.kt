@file:Suppress("OVERRIDE_DEPRECATION")

package io.github.thebroccolibob.soulorigins.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

open class SurfaceBlock(settings: Settings) : Block(settings) {
    init {
        defaultState = defaultState
            .with(DISTANCE, 1)
            .with(COMPLETE, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(DISTANCE, COMPLETE)
    }

    override fun onBlockAdded(
        state: BlockState,
        world: World,
        pos: BlockPos,
        oldState: BlockState,
        notify: Boolean
    ) {
        val distance = getDistanceFromBuilder(world, pos)
        if (distance == state[DISTANCE]) return
        world.setBlockState(pos, state.with(DISTANCE, distance))
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        if (neighborState.contains(COMPLETE) && neighborState[COMPLETE])
            world.setBlockState(pos, state.with(COMPLETE, true), NOTIFY_ALL)

        val distance = state[DISTANCE]
        val newDistance = getDistanceFromBuilder(world, pos)

        if (newDistance > distance) world.scheduleBlockTick(pos, this, 10)

        return state
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        world.setBlockState(pos, world.getFluidState(pos).blockState)
        val soundGroup = state.soundGroup
        world.playSound(null, pos, soundGroup.breakSound, SoundCategory.BLOCKS, soundGroup.volume, soundGroup.pitch)
    }

    override fun onLandedUpon(world: World, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        entity.handleFallDamage(fallDistance, 0.2f, world.damageSources.fall())
    }

    private fun getDistanceFromBuilder(world: WorldAccess, pos: BlockPos): Int {
        return Direction.entries.map { world.getBlockState(pos.offset(it)) }.minOf {
            when (it.block) {
                is SurfaceBuilderBlock -> 1
                is SurfaceBlock -> (it[DISTANCE] + 1).coerceAtMost(16)
                else -> 16
            }
        }
    }

    fun getStateForLocation(state: BlockState, world: World, pos: BlockPos): BlockState {
        return state.with(DISTANCE, getDistanceFromBuilder(world, pos))
    }

    companion object {
        val DISTANCE: IntProperty = IntProperty.of("distance", 1, 16)
        val COMPLETE = SoulOriginsBlocks.COMPLETE
    }
}
