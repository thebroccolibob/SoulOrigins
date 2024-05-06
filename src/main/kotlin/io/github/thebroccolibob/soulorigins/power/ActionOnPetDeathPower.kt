package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.calio.data.SerializableData
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import java.util.function.BiFunction
import java.util.function.Consumer

class ActionOnPetDeathPower(type: PowerType<*>, entity: LivingEntity, private val entityAction: Consumer<Entity>) : Power(type, entity) {
    fun executeAction() {
        entityAction.accept(entity)
    }

    fun createFactory(): PowerFactory<*> {
        return PowerFactory(
            Identifier(Soulorigins.MOD_ID, "action_on_pet_death"),
            SerializableData()
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
        ) { data ->
            BiFunction { type: PowerType<Power>, player: LivingEntity ->
                ActionOnPetDeathPower(type, player, data.get<ActionFactory<Entity>.Instance>("entity_action"))
            }
        }.allowCondition()
    }
}
