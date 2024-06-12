package io.github.thebroccolibob.soulorigins.effect

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory

class NecrosisEffect(category: StatusEffectCategory, color: Int) : StatusEffect(category, color) {
    override fun onApplied(entity: LivingEntity, attributes: AttributeContainer, amplifier: Int) {
        super.onApplied(entity, attributes, amplifier)
        if (entity.health > entity.maxHealth) {
            entity.health = entity.maxHealth
        }
    }
}
