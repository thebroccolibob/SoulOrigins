package io.github.thebroccolibob.soulorigins.entity.ai.goal

import io.github.thebroccolibob.soulorigins.cca.OwnerComponent.Companion.isOwned
import io.github.thebroccolibob.soulorigins.cca.OwnerComponent.Companion.owner
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.TargetPredicate
import net.minecraft.entity.ai.goal.TrackTargetGoal
import net.minecraft.entity.mob.MobEntity
import java.util.*

class MonsterAttackWithOwnerGoal(private val monster: MobEntity) : TrackTargetGoal(monster, false) {
    private var attacking: LivingEntity? = null
    private var lastAttackTime: Int = 0

    init {
        this.controls = EnumSet.of(Control.TARGET)
    }

    override fun canStart(): Boolean {
        if (monster.isOwned) {
            val owner = monster.owner
            if (owner == null) {
                return false
            } else {
                val target = owner.attacking
                this.attacking = target
                val i = owner.lastAttackTime
                return i != this.lastAttackTime && this.canTrack(
                    target,
                    TargetPredicate.DEFAULT
                ) && (target as? MobEntity)?.owner != owner
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
