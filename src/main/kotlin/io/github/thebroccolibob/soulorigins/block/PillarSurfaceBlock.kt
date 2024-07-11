@file:Suppress("OVERRIDE_DEPRECATION")

package io.github.thebroccolibob.soulorigins.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.PillarBlock.changeRotation
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.Direction

class PillarSurfaceBlock(settings: Settings) : SurfaceBlock(settings) {
    init {
        this.defaultState = defaultState.with(AXIS, Direction.Axis.Y)
    }

    val rotationY: BlockState = defaultState.with(AXIS, Direction.Axis.Y)
    val rotationX: BlockState = defaultState.with(AXIS, Direction.Axis.X)
    val rotationZ: BlockState = defaultState.with(AXIS, Direction.Axis.Z)

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return changeRotation(state, rotation)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(AXIS)
    }

    companion object {
        val AXIS: EnumProperty<Direction.Axis> = Properties.AXIS
    }
}
