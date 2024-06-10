package io.github.thebroccolibob.soulorigins.effect

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import kotlin.math.pow

class PrecisionEffect(category: StatusEffectCategory, color: Int) : StatusEffect(category, color) {
    companion object {
        @JvmStatic
        fun modify(entity: LivingEntity, base: Float): Float {
            entity.getStatusEffect(SoulOriginsEffects.PERCEPTION)?.let {
                return (base * 0.2.pow(it.amplifier + 1.0)).toFloat()
            }
            return base
        }
    }
}
