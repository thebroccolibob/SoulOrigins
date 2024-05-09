package io.github.thebroccolibob.soulorigins.action

import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.CooldownPower
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

private fun register(actionFactory: ActionFactory<Entity>) {
    Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.serializerId, actionFactory)
}

fun registerSoulOriginsEntityActions() {
    register(ActionFactory(
        Identifier(Soulorigins.MOD_ID, "change_cooldown"), SerializableData()
            .add("cooldown", ApoliDataTypes.POWER_TYPE)
            .add("change", SerializableDataTypes.INT)
    ) { data, entity ->
        if (entity is LivingEntity) {
            val component = PowerHolderComponent.KEY[entity]
            val powerType = data.get<PowerType<*>>("cooldown")
            val p = component.getPower(powerType)
            val change = data.getInt("change")
            if (p is CooldownPower) {
                p.setCooldown((p.cooldownDuration - p.remainingTicks) - change)
                PowerHolderComponent.syncPower(entity, powerType)
            }
        }
    })

    register(ActionFactory(
        Identifier(Soulorigins.MOD_ID, "despawn"), SerializableData()
    ) { _, entity ->
        entity.discard()
    })
}
