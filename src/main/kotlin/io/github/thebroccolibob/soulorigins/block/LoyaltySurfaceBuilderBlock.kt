@file:Suppress("OVERRIDE_DEPRECATION")

package io.github.thebroccolibob.soulorigins.block

import io.github.thebroccolibob.soulorigins.block.entity.LoyaltySurfaceBuilderBlockEntity
import io.github.thebroccolibob.soulorigins.entity.LoyaltyItemEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class LoyaltySurfaceBuilderBlock(rangeX: Int, rangeY: Int, rangeZ: Int, surfaceBlock: BlockState, private val breakDelay: Int, settings: Settings) :
    SurfaceBuilderBlock(rangeX, rangeY, rangeZ, surfaceBlock, settings), BlockEntityProvider {

    constructor(rangeX: Int, rangeY: Int, rangeZ: Int, surfaceBlock: Block, breakDelay: Int, settings: Settings) : this(rangeX, rangeY, rangeZ, surfaceBlock.defaultState, breakDelay, settings)

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        if (!state[COMPLETE]) {
            super.scheduledTick(state, world, pos, random)
            return
        }

        if (world.isClient) return

        breakSpawnItem(world, pos, state)
    }

    override fun onCompleteBuild(state: BlockState, world: ServerWorld, pos: BlockPos) {
        world.scheduleBlockTick(pos, this, breakDelay)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if (!player.getStackInHand(hand).isEmpty) return ActionResult.PASS

        if (world.isClient) return ActionResult.SUCCESS

        if ((world.getBlockEntity(pos) as LoyaltySurfaceBuilderBlockEntity).owner == player) {
            breakSpawnItem(world as ServerWorld, pos, state)
        }

        return ActionResult.SUCCESS
    }

    private fun breakSpawnItem(world: ServerWorld, pos: BlockPos, state: BlockState) {
        val blockEntity = world.getBlockEntity(pos) as LoyaltySurfaceBuilderBlockEntity
        val owner = blockEntity.owner

        if (owner?.isAlive == true) {
            LoyaltyItemEntity(world, blockEntity.item, owner).apply {
                setPosition(pos.toCenterPos())
                world.spawnEntity(this)
            }

            world.breakBlock(pos, false)
        } else {
            world.breakBlock(pos, true)
        }
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return LoyaltySurfaceBuilderBlockEntity(pos, state)
    }
}
