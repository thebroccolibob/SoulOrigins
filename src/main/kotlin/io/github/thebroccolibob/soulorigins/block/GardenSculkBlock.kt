@file:Suppress("OVERRIDE_DEPRECATION")

package io.github.thebroccolibob.soulorigins.block

import io.github.thebroccolibob.soulorigins.hasPower
import io.github.thebroccolibob.soulorigins.power.GardenWalker
import net.minecraft.block.BlockState
import net.minecraft.block.EntityShapeContext
import net.minecraft.block.SculkBlock
import net.minecraft.block.ShapeContext
import net.minecraft.entity.Entity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class GardenSculkBlock(settings: Settings) : SculkBlock(settings) {
    private val particleEffect = BlockStateParticleEffect(ParticleTypes.BLOCK, this.defaultState)

    override fun getCollisionShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape {
        if (context is EntityShapeContext && context.entity?.hasPower<GardenWalker>() == true) {
            return FULL_COLLISION_SHAPE
        }
        return SINKING_COLLISION_SHAPE
    }

    override fun getSidesShape(state: BlockState, world: BlockView, pos: BlockPos): VoxelShape = VoxelShapes.fullCube()

    override fun getCameraCollisionShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape =
        VoxelShapes.fullCube()

    override fun getAmbientOcclusionLightLevel(state: BlockState, world: BlockView, pos: BlockPos) = 0.2f

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    override fun asItem(): Item = Items.SCULK

    override fun onSteppedOn(world: World, pos: BlockPos, state: BlockState, entity: Entity) {
        if (entity.hasPower<GardenWalker>()) return

        entity.slowMovement(state, Vec3d(0.7, 0.2, 0.7))

        if (entity.lastRenderX == entity.x && entity.lastRenderZ == entity.z) return

        if (world.isClient) {
            if (entity.isPlayer)
                for (i in 0..<5)
                    world.addParticle(particleEffect, entity.x, entity.y + 0.5, entity.z, 0.0, 0.0, 0.0)
        } else {
            if (entity.isLiving && !entity.isPlayer)
                (world as ServerWorld).spawnParticles(particleEffect, entity.x, entity.y + 0.5, entity.z, 5, 0.0, 0.0, 0.0, 0.0)
        }
    }

    companion object {
        val SINKING_COLLISION_SHAPE: VoxelShape = createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0)
        val FULL_COLLISION_SHAPE: VoxelShape = VoxelShapes.fullCube()
    }
}
