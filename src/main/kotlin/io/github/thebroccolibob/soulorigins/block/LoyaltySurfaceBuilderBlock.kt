@file:Suppress("OVERRIDE_DEPRECATION")

package io.github.thebroccolibob.soulorigins.block

import io.github.thebroccolibob.soulorigins.block.entity.LoyaltySurfaceBuilderBlockEntity
import io.github.thebroccolibob.soulorigins.entity.LoyaltyItemEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random

class LoyaltySurfaceBuilderBlock(rangeX: Int, rangeY: Int, rangeZ: Int, surfaceBlock: BlockState, private val breakDelay: Int, settings: Settings) :
    SurfaceBuilderBlock(rangeX, rangeY, rangeZ, surfaceBlock, settings), BlockEntityProvider {

    constructor(rangeX: Int, rangeY: Int, rangeZ: Int, surfaceBlock: Block, breakDelay: Int, settings: Settings) : this(rangeX, rangeY, rangeZ, surfaceBlock.defaultState, breakDelay, settings)

        init {
            defaultState = defaultState.with(COMPLETE, false)
        }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(COMPLETE)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        if (!state[COMPLETE]) {
            super.scheduledTick(state, world, pos, random)
            return
        }

        if (world.isClient) return

        val blockEntity = world.getBlockEntity(pos)
        val owner = (blockEntity as LoyaltySurfaceBuilderBlockEntity).owner

        if (owner?.isAlive == true) {
            getDroppedStacks(state, world, pos, blockEntity).forEach {
                LoyaltyItemEntity(
                    world,
                    it,
                    owner
                ).apply {
                    setPosition(pos.toCenterPos())
                    world.spawnEntity(this)
                }
            }

            world.breakBlock(pos, false)
        } else {
            world.breakBlock(pos, true)
        }
    }

    override fun onCompleteBuild(state: BlockState, world: ServerWorld, pos: BlockPos) {
        world.setBlockState(pos, state.with(COMPLETE, true))
        world.scheduleBlockTick(pos, this, breakDelay)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return LoyaltySurfaceBuilderBlockEntity(pos, state)
    }

    companion object {
        val COMPLETE: BooleanProperty = BooleanProperty.of("complete")
    }
}
