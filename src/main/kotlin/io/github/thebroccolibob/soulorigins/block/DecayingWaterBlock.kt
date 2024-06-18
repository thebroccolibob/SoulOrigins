@file:Suppress("OVERRIDE_DEPRECATION")

package io.github.thebroccolibob.soulorigins.block

import net.minecraft.block.*
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class DecayingWaterBlock(override val minDelay: Int, override val maxDelay: Int, settings: Settings) : Block(settings), Decaying {
    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        super<Decaying>.onBlockAdded(state, world, pos, oldState, notify)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        world.setBlockState(pos, Blocks.AIR.defaultState)
    }

    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = VoxelShapes.empty()

    override fun getAmbientOcclusionLightLevel(state: BlockState?, world: BlockView?, pos: BlockPos?): Float = 1.0f

    override fun getRenderType(state: BlockState?): BlockRenderType = BlockRenderType.INVISIBLE

    override fun getFluidState(state: BlockState?): FluidState {
        return Fluids.WATER.getStill(false)
    }
}
