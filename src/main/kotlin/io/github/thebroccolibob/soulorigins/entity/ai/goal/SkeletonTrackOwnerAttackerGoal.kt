package io.github.thebroccolibob.soulorigins.entity.ai.goal

import io.github.thebroccolibob.soulorigins.entity.OwnableSkeleton
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.TargetPredicate
import net.minecraft.entity.ai.goal.TrackTargetGoal
import net.minecraft.entity.mob.AbstractSkeletonEntity
import java.util.*

class SkeletonTrackOwnerAttackerGoal(private val skeleton: AbstractSkeletonEntity) : TrackTargetGoal(skeleton, false) {
    private var attacker: LivingEntity? = null
    private var lastAttackedTime: Int = 0

    private inline val asOwnable get() = skeleton as OwnableSkeleton

    init {
        this.controls = EnumSet.of(Control.TARGET)
    }


    override fun canStart(): Boolean {
        if (asOwnable.isTamed) {
            val owner = asOwnable.getOwner()
            if (owner == null) {
                return false
            } else {
                val target = owner.attacker
                this.attacker = target
                val i = owner.lastAttackedTime
                return i != this.lastAttackedTime && this.canTrack(
                    target,
                    TargetPredicate.DEFAULT
                ) && (target !is OwnableSkeleton || target.owner != owner)
            }
        } else {
            return false
        }
    }

    override fun start() {
        mob.target = this.attacker
        val livingEntity = asOwnable.getOwner()
        if (livingEntity != null) {
            this.lastAttackedTime = livingEntity.lastAttackedTime
        }

        super.start()
    }

}
