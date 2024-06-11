@file:Suppress("OVERRIDE_DEPRECATION")

package io.github.thebroccolibob.soulorigins.block

import io.github.thebroccolibob.soulorigins.copy
import io.github.thebroccolibob.soulorigins.floorMod
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.MovementType
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

open class SurfaceBuilderBlock(private val rangeX: Int, private val rangeY: Int, private val rangeZ: Int, private val surfaceBlock: BlockState, settings: Settings) : Block(settings) {
    private val surfaceSounds = surfaceBlock.soundGroup

    constructor(rangeX: Int, rangeY: Int, rangeZ: Int, surfaceBlock: Block, settings: Settings) : this(rangeX, rangeY, rangeZ, surfaceBlock.defaultState, settings)

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
        val entities = mutableSetOf<Entity>()
        for (areaPos in BlockPos.iterateOutwards(pos, rangeX, rangeY, rangeZ)) {
            if (world.isOutOfHeightLimit(areaPos)) continue
            val areaState = world.getBlockState(areaPos)
            if (!areaState.isReplaceable || areaState.block is SurfaceBlock || DIRECTIONS.none { world.getBlockState(areaPos.offset(it)).block is SurfaceBlock || areaPos.offset(it) == pos }) continue
            positions.add(areaPos.copy())

            world.getOtherEntities(null, Box.from(BlockBox(areaPos))).forEach(entities::add)
        }
        if (positions.isEmpty()) {
            onCompleteBuild(state, world, pos)
        } else {
            for (areaPos in positions) {
                world.setBlockState(areaPos, (surfaceBlock.block as? SurfaceBlock)?.getStateForLocation(surfaceBlock, world, areaPos) ?: surfaceBlock)
                world.playSound(null, areaPos, surfaceSounds.placeSound, SoundCategory.BLOCKS, surfaceSounds.volume, surfaceSounds.pitch)
            }
            for (entity in entities) {
                entity.move(MovementType.SHULKER, Vec3d(0.0, 1 - (entity.y floorMod 1.0), 0.0))
            }
            world.scheduleBlockTick(pos, this, 5)
        }
    }

    override fun onLandedUpon(world: World, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        entity.handleFallDamage(fallDistance, 0.2f, world.damageSources.fall())
    }

    protected open fun onCompleteBuild(state: BlockState, world: ServerWorld, pos: BlockPos) {}
}
