@file:Suppress("OVERRIDE_DEPRECATION")

package io.github.thebroccolibob.soulorigins.block

import io.github.thebroccolibob.soulorigins.copy
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

open class SurfaceBuilderBlock(private val rangeX: Int, private val rangeY: Int, private val rangeZ: Int, private val surfaceBlock: Block, settings: Settings) : Block(settings) {
    private val surfaceSounds = surfaceBlock.getSoundGroup(surfaceBlock.defaultState)

    override fun onBlockAdded(
        state: BlockState,
        world: World,
        pos: BlockPos,
        oldState: BlockState,
        notify: Boolean
    ) {
        if (oldState.isOf(this)) return
       world.scheduleBlockTick(pos, this, 5)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        val positions = mutableListOf<BlockPos>()
        for (areaPos in BlockPos.iterateOutwards(pos, rangeX, rangeY, rangeZ)) {
            val areaState = world.getBlockState(areaPos)
            if (!areaState.isReplaceable || areaState.isOf(surfaceBlock) || DIRECTIONS.none { world.getBlockState(areaPos.offset(it)).isOf(surfaceBlock) || areaPos.offset(it) == pos }) continue
            positions.add(areaPos.copy())
        }
        if (positions.isEmpty()) {
            onCompleteBuild(state, world, pos)
        } else {
            for (areaPos in positions) {
                world.setBlockState(areaPos, (surfaceBlock as? SurfaceBlock)?.getStateForLocation(world, areaPos) ?: surfaceBlock.defaultState)
                world.playSound(null, areaPos, surfaceSounds.placeSound, SoundCategory.BLOCKS, surfaceSounds.volume, surfaceSounds.pitch)
            }
            world.scheduleBlockTick(pos, this, 5)
        }
    }

    override fun onLandedUpon(world: World, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        entity.handleFallDamage(fallDistance, 0.2f, world.damageSources.fall())
    }

    protected open fun onCompleteBuild(state: BlockState, world: ServerWorld, pos: BlockPos) {}
}
