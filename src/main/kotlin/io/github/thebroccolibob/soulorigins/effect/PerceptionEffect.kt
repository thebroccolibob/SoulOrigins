package io.github.thebroccolibob.soulorigins.effect

import net.minecraft.entity.Entity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.entity.player.PlayerEntity
import kotlin.math.pow

class PerceptionEffect(category: StatusEffectCategory, color: Int) : StatusEffect(category, color) {
    companion object {
        @JvmStatic
        fun shouldGlow(playerEntity: PlayerEntity, entity: Entity): Boolean {
            playerEntity.getStatusEffect(SoulOriginsEffects.PERCEPTION)?.let {
                return entity.isLiving && playerEntity.squaredDistanceTo(entity) < ((it.amplifier + 1) * 12.0).pow(2.0)
            }
            return false
        }
    }
}
