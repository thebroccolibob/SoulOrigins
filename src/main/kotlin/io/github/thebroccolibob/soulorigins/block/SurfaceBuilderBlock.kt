@file:Suppress("OVERRIDE_DEPRECATION")

package io.github.thebroccolibob.soulorigins.block

import io.github.thebroccolibob.soulorigins.block.entity.SurfaceBuilderBlockEntity
import io.github.thebroccolibob.soulorigins.copy
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class SurfaceBuilderBlock(private val rangeX: Int, private val rangeY: Int, private val rangeZ: Int, private val surfaceBlock: Block, settings: Settings) : Block(settings), BlockEntityProvider {
    private val range = Vec3i(rangeX, rangeY, rangeZ)
    private val placeSound = surfaceBlock.getSoundGroup(surfaceBlock.defaultState).placeSound

    override fun onBlockAdded(
        state: BlockState,
        world: World,
        pos: BlockPos,
        oldState: BlockState,
        notify: Boolean
    ) {
        world.scheduleBlockTick(pos, this, 5)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        val positions = mutableListOf<BlockPos>()
        for (areaPos in BlockPos.iterateOutwards(pos, rangeX, rangeY, rangeZ)) {
            val areaState = world.getBlockState(areaPos)
            if (!areaState.isReplaceable || areaState.isOf(surfaceBlock) || DIRECTIONS.none { world.getBlockState(areaPos.offset(it)).isOf(surfaceBlock) || areaPos.offset(it) == pos }) continue
            positions.add(areaPos.copy())
        }
        for (areaPos in positions) {
            world.setBlockState(areaPos, (surfaceBlock as? SurfaceBlock)?.getStateForLocation(world, areaPos) ?: surfaceBlock.defaultState)
        }
        if (positions.isNotEmpty()) {
            world.playSound(null, pos, placeSound, SoundCategory.BLOCKS, 2.0f, 1.0f)
            world.scheduleBlockTick(pos, this, 5)
        }
    }

    override fun onLandedUpon(world: World, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        entity.handleFallDamage(fallDistance, 0.2f, world.damageSources.fall())
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return SurfaceBuilderBlockEntity(pos, state)
    }
}
