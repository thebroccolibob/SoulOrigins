package io.github.thebroccolibob.soulorigins.entity.ai.goal

import io.github.thebroccolibob.soulorigins.entity.owner
import net.minecraft.block.LeavesBlock
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.pathing.*
import net.minecraft.entity.mob.AbstractSkeletonEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

/**
 * Copied from [net.minecraft.entity.ai.goal.FollowOwnerGoal]
 */
class SkeletonFollowOwnerGoal(
    private val skeleton: AbstractSkeletonEntity,
    private val speed: Double,
    private val minDistance: Float,
    private val maxDistance: Float,
    private val leavesAllowed: Boolean
) :
    Goal() {
    private var owner: LivingEntity? = null
    private val world: WorldView = skeleton.world
    private val navigation: EntityNavigation = skeleton.navigation
    private var updateCountdownTicks = 0
    private var oldWaterPathfindingPenalty = 0f

    init {
        this.controls = EnumSet.of(Control.MOVE, Control.LOOK)
        require(!(skeleton.navigation !is MobNavigation && skeleton.navigation !is BirdNavigation)) { "Unsupported mob type for FollowOwnerGoal" }
    }

    override fun canStart(): Boolean {
        val livingEntity = skeleton.owner
        if (livingEntity == null) {
            return false
        } else if (livingEntity.isSpectator) {
            return false
        } else if (this.cannotFollow()) {
            return false
        } else if (skeleton.squaredDistanceTo(livingEntity) < (this.minDistance * this.minDistance).toDouble()) {
            return false
        } else {
            this.owner = livingEntity
            return true
        }
    }

    override fun shouldContinue(): Boolean {
        return if (navigation.isIdle) {
            false
        } else if (this.cannotFollow()) {
            false
        } else {
            !(skeleton.squaredDistanceTo(this.owner) <= (this.maxDistance * this.maxDistance).toDouble())
        }
    }

    private fun cannotFollow(): Boolean {
        return skeleton.hasVehicle() || skeleton.isLeashed
    }

    override fun start() {
        this.updateCountdownTicks = 0
        this.oldWaterPathfindingPenalty = skeleton.getPathfindingPenalty(PathNodeType.WATER)
        skeleton.setPathfindingPenalty(PathNodeType.WATER, 0.0f)
    }

    override fun stop() {
        this.owner = null
        navigation.stop()
        skeleton.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty)
    }

    override fun tick() {
        skeleton.lookControl.lookAt(this.owner, 10.0f, skeleton.maxLookPitchChange.toFloat())
        if (--this.updateCountdownTicks <= 0) {
            this.updateCountdownTicks = this.getTickCount(10)
            if (skeleton.squaredDistanceTo(this.owner) >= TELEPORT_DISTANCE.toDouble().pow(2)) {
                this.tryTeleport()
            } else {
                navigation.startMovingTo(this.owner, this.speed)
            }
        }
    }

    private fun tryTeleport() {
        val blockPos = owner!!.blockPos

        for (i in 0..9) {
            val j = this.getRandomInt(-HORIZONTAL_VARIATION, HORIZONTAL_VARIATION)
            val k = this.getRandomInt(-VERTICAL_VARIATION, VERTICAL_VARIATION)
            val l = this.getRandomInt(-HORIZONTAL_VARIATION, HORIZONTAL_VARIATION)
            val bl = this.tryTeleportTo(blockPos.x + j, blockPos.y + k, blockPos.z + l)
            if (bl) {
                return
            }
        }
    }

    private fun tryTeleportTo(x: Int, y: Int, z: Int): Boolean {
        if (abs(x.toDouble() - owner!!.x) < HORIZONTAL_RANGE && abs(z.toDouble() - owner!!.z) < HORIZONTAL_RANGE) {
            return false
        } else if (!this.canTeleportTo(BlockPos(x, y, z))) {
            return false
        } else {
            skeleton.refreshPositionAndAngles(
                x.toDouble() + 0.5, y.toDouble(), z.toDouble() + 0.5,
                skeleton.yaw, skeleton.pitch
            )
            navigation.stop()
            return true
        }
    }

    private fun canTeleportTo(pos: BlockPos): Boolean {
        val pathNodeType = LandPathNodeMaker.getLandNodeType(this.world, pos.mutableCopy())
        if (pathNodeType != PathNodeType.WALKABLE) {
            return false
        } else {
            val blockState = world.getBlockState(pos.down())
            if (!this.leavesAllowed && blockState.block is LeavesBlock) {
                return false
            } else {
                val blockPos = pos.subtract(skeleton.blockPos)
                return world.isSpaceEmpty(this.skeleton, skeleton.boundingBox.offset(blockPos))
            }
        }
    }

    private fun getRandomInt(min: Int, max: Int): Int {
        return skeleton.random.nextInt(max - min + 1) + min
    }

    companion object {
        private const val TELEPORT_DISTANCE: Int = 64
        private const val HORIZONTAL_RANGE = 2
        private const val HORIZONTAL_VARIATION = 3
        private const val VERTICAL_VARIATION = 1
    }
}
