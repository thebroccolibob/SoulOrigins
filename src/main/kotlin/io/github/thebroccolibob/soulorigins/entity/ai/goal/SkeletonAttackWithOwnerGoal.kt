package io.github.thebroccolibob.soulorigins.entity.ai.goal

import io.github.thebroccolibob.soulorigins.entity.OwnableSkeleton
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.TargetPredicate
import net.minecraft.entity.ai.goal.TrackTargetGoal
import net.minecraft.entity.mob.AbstractSkeletonEntity
import java.util.*

class SkeletonAttackWithOwnerGoal(private val skeleton: AbstractSkeletonEntity) : TrackTargetGoal(skeleton, false) {
    private var attacking: LivingEntity? = null
    private var lastAttackTime: Int = 0

    private inline val asOwnable get() = skeleton as OwnableSkeleton

    init {
        this.controls = EnumSet.of(Control.TARGET)
    }

    override fun canStart(): Boolean {
        if (asOwnable.isTamed) {
            val owner = asOwnable.owner
            if (owner == null) {
                return false
            } else {
                val target = owner.attacking
                this.attacking = target
                val i = owner.lastAttackTime
                return i != this.lastAttackTime && this.canTrack(
                    target,
                    TargetPredicate.DEFAULT
                ) && (target !is OwnableSkeleton || target.owner != owner)
            }
        } else {
            return false
        }
    }

    override fun start() {
        mob.target = this.attacking
        val livingEntity = attacking
        if (livingEntity != null) {
            this.lastAttackTime = livingEntity.lastAttackTime
        }

        super.start()
    }
}
