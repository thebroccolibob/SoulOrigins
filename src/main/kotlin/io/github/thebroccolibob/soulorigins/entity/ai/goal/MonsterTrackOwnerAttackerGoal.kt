package io.github.thebroccolibob.soulorigins.entity.ai.goal

import io.github.thebroccolibob.soulorigins.entity.OwnableMonster
import io.github.thebroccolibob.soulorigins.entity.isTamed
import io.github.thebroccolibob.soulorigins.entity.owner
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
        if (monster.isTamed) {
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
                ) && (target !is OwnableMonster || target.owner != owner)
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
