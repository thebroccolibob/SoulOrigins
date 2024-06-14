package io.github.thebroccolibob.soulorigins.entity.ai.goal

import io.github.thebroccolibob.soulorigins.cca.OwnerComponent.Companion.isOwned
import io.github.thebroccolibob.soulorigins.cca.OwnerComponent.Companion.owner
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.TargetPredicate
import net.minecraft.entity.ai.goal.TrackTargetGoal
import net.minecraft.entity.mob.MobEntity
import java.util.*

class MonsterTrackOwnerAttackerGoal(private val monster: MobEntity) : TrackTargetGoal(monster, false) {
    private var attacker: LivingEntity? = null
    private var lastAttackedTime: Int = 0

    init {
        this.controls = EnumSet.of(Control.TARGET)
    }


    override fun canStart(): Boolean {
        if (monster.isOwned) {
            val owner = monster.owner
            if (owner == null) {
                return false
            } else {
                val target = owner.attacker
                this.attacker = target
                val i = owner.lastAttackedTime
                return i != this.lastAttackedTime && this.canTrack(
                    target,
                    TargetPredicate.DEFAULT
                ) && (target as? MobEntity)?.owner != owner
            }
        } else {
            return false
        }
    }

    override fun start() {
        mob.target = this.attacker
        val livingEntity = monster.owner
        if (livingEntity != null) {
            this.lastAttackedTime = livingEntity.lastAttackedTime
        }

        super.start()
    }

}
