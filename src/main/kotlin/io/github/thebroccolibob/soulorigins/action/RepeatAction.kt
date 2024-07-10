package io.github.thebroccolibob.soulorigins.action

import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.CooldownPower
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.VariableIntPower
import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.calio.data.SerializableDataType
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.SerializableData
import io.github.thebroccolibob.soulorigins.SoulOrigins
import net.minecraft.entity.Entity

object RepeatAction {
    fun <T> createFactory(factoryType: SerializableDataType<ActionFactory<T>.Instance>) = ActionFactory<T>(
        SoulOrigins.id("repeat"),
        SerializableData {
            add("action", factoryType)
            add("count", SerializableDataTypes.INT, 0)
        }
    ) { data, subject ->
        val count = data.getInt("count")
        val action: ActionFactory<T>.Instance = data["action"]

        for (i in 0..count) {
            action.accept(subject)
        }
    }

    val entityAction = ActionFactory(
        SoulOrigins.id("repeat"),
        SerializableData {
            add("action", ApoliDataTypes.ENTITY_ACTION)
            add("count", SerializableDataTypes.INT, 0)
            add("resource", ApoliDataTypes.POWER_TYPE, null)
        }
    ) { data, entity: Entity ->
        val count = data.get<PowerType<*>?>("resource")?.get(entity)?.let {
            when (it) {
                is VariableIntPower -> it.value
                is CooldownPower -> it.remainingTicks
                else -> null
            }
        } ?: data.getInt("count")
        val action: ActionFactory<Entity>.Instance = data["action"]

        for (i in 0..<count) {
            action.accept(entity)
        }
    }
}
