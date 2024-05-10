package io.github.thebroccolibob.soulorigins.entity.ai.goal

import io.github.thebroccolibob.soulorigins.entity.owner
import net.minecraft.block.LeavesBlock
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.pathing.*
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

/**
 * Copied from [net.minecraft.entity.ai.goal.FollowOwnerGoal]
 */
class MonsterFollowOwnerGoal(
    private val monster: MobEntity,
    private val speed: Double,
    private val minDistance: Float,
    private val maxDistance: Float,
    private val leavesAllowed: Boolean
) :
    Goal() {
    private var owner: LivingEntity? = null
    private val world: WorldView = monster.world
    private val navigation: EntityNavigation = monster.navigation
    private var updateCountdownTicks = 0
    private var oldWaterPathfindingPenalty = 0f

    init {
        this.controls = EnumSet.of(Control.MOVE, Control.LOOK)
        require(!(monster.navigation !is MobNavigation && monster.navigation !is BirdNavigation)) { "Unsupported mob type for FollowOwnerGoal" }
    }

    override fun canStart(): Boolean {
        val livingEntity = monster.owner
        if (livingEntity == null) {
            return false
        } else if (livingEntity.isSpectator) {
            return false
        } else if (this.cannotFollow()) {
            return false
        } else if (monster.squaredDistanceTo(livingEntity) < (this.minDistance * this.minDistance).toDouble()) {
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
            !(monster.squaredDistanceTo(this.owner) <= (this.maxDistance * this.maxDistance).toDouble())
        }
    }

    private fun cannotFollow(): Boolean {
        return monster.hasVehicle() || monster.isLeashed
    }

    override fun start() {
        this.updateCountdownTicks = 0
        this.oldWaterPathfindingPenalty = monster.getPathfindingPenalty(PathNodeType.WATER)
        monster.setPathfindingPenalty(PathNodeType.WATER, 0.0f)
    }

    override fun stop() {
        this.owner = null
        navigation.stop()
        monster.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty)
    }

    override fun tick() {
        monster.lookControl.lookAt(this.owner, 10.0f, monster.maxLookPitchChange.toFloat())
        if (--this.updateCountdownTicks <= 0) {
            this.updateCountdownTicks = this.getTickCount(10)
            if (monster.squaredDistanceTo(this.owner) >= TELEPORT_DISTANCE.toDouble().pow(2)) {
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
            monster.refreshPositionAndAngles(
                x.toDouble() + 0.5, y.toDouble(), z.toDouble() + 0.5,
                monster.yaw, monster.pitch
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
                val blockPos = pos.subtract(monster.blockPos)
                return world.isSpaceEmpty(this.monster, monster.boundingBox.offset(blockPos))
            }
        }
    }

    private fun getRandomInt(min: Int, max: Int): Int {
        return monster.random.nextInt(max - min + 1) + min
    }

    companion object {
        private const val TELEPORT_DISTANCE: Int = 64
        private const val HORIZONTAL_RANGE = 2
        private const val HORIZONTAL_VARIATION = 3
        private const val VERTICAL_VARIATION = 1
    }
}
