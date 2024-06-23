package io.github.thebroccolibob.soulorigins.effect

import io.github.thebroccolibob.soulorigins.SoulOrigins
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object SoulOriginsEffects {
    fun register(path: String, effect: StatusEffect): StatusEffect {
        return Registry.register(Registries.STATUS_EFFECT, SoulOrigins.id(path), effect)
    }

    private const val SUSPICIOUS_COLOR = 7864389

    val NECROSIS = register(
        "necrosis",
        NecrosisEffect(StatusEffectCategory.HARMFUL, SUSPICIOUS_COLOR)
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, "4CA00DAF-C2F1-4E1E-AE80-C0F96DF991A8", -4.0, EntityAttributeModifier.Operation.ADDITION)
    )

    val PERCEPTION = register(
        "perception",
        PerceptionEffect(StatusEffectCategory.BENEFICIAL, SUSPICIOUS_COLOR)
    )

    val THRONGLED = register(
        "throngled",
        object : StatusEffect(StatusEffectCategory.HARMFUL, SUSPICIOUS_COLOR) {}
    )

    val PRECISION = register(
        "precision",
        object : StatusEffect(StatusEffectCategory.BENEFICIAL, SUSPICIOUS_COLOR) {}
    )

    val INCOMPLETE_MEMENTO_MORI = register(
        "incomplete_memento_mori",
        object : StatusEffect(StatusEffectCategory.NEUTRAL, 2804175) {}
    )

    @JvmField
    val MEMENTO_MORI = register(
        "memento_mori",
        object : StatusEffect(StatusEffectCategory.HARMFUL, 5005931) {}
    )

    fun register() {}
}
