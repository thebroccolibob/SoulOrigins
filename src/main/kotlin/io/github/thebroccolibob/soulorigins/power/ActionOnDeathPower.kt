package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.thebroccolibob.soulorigins.SerializableData
import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.getPowers
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import java.util.function.BiFunction

class ActionOnDeathPower(
    type: PowerType<*>,
    entity: LivingEntity?,
    private val entityAction: ActionFactory<Entity>.Instance,
) : Power(type, entity) {
    fun execute(entity: LivingEntity) {
        entityAction.accept(entity)
    }

    companion object {
        @JvmStatic
        fun executeAll(entity: LivingEntity) {
            for (power in entity.getPowers<ActionOnDeathPower>()) {
                power.execute(entity)
            }
        }

        val factory: PowerFactory<Power> = PowerFactory(
            SoulOrigins.id("action_on_death"),
            SerializableData {
                add("entity_action", ApoliDataTypes.ENTITY_ACTION)
            }
        ) { data ->
            BiFunction { type, entity -> ActionOnDeathPower(type, entity, data.get("entity_action")) }
        }.allowCondition()
    }
}
