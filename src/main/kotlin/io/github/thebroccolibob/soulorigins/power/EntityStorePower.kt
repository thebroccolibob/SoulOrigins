package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.apoli.power.factory.condition.ConditionFactory
import io.github.apace100.calio.data.SerializableData
import io.github.thebroccolibob.soulorigins.*
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Pair
import java.util.*
import java.util.function.BiFunction

/**
 * Only works on the server
 */
class EntityStorePower(type: PowerType<EntityStorePower>, entity: LivingEntity?) : Power(type, entity) {
    var entityUUID: UUID? = null
        private set

    var storedEntity: Entity? = null
        get() {
            if (entity.world !is ServerWorld) return null

            if (field == null || field!!.isRemoved) {
                field = (entity.world as ServerWorld).getEntity(entityUUID)?.let {
                    if (it.isRemoved) null else it
                }
            }
            return field
        }
        set(value) {
            field = value
            entityUUID = value?.uuid
        }

    val isEmpty get() = entityUUID != null

    val isNotEmpty get() = !isEmpty

    companion object {
        val factory: PowerFactory<EntityStorePower> = PowerFactory(SoulOrigins.id("entity_store"), SerializableData()) {
            BiFunction(::EntityStorePower)
        }

        val setAction: ActionFactory<Pair<Entity, Entity>> = ActionFactory(SoulOrigins.id("set_entity_store"), SerializableData {
            add("store", ApoliDataTypes.POWER_TYPE)
        }) { data, (actor, target) ->
            (actor.getPower(data.get<PowerType<*>>("store")) as? EntityStorePower)?.storedEntity = target
        }

        val clearAction: ActionFactory<Entity> = ActionFactory(SoulOrigins.id("clear_entity_store"), SerializableData {
            add("store", ApoliDataTypes.POWER_TYPE)
        }) { data, entity ->
            (entity.getPower(data.get<PowerType<*>>("store")) as? EntityStorePower)?.storedEntity = null
        }

        val storeAction: ActionFactory<Entity> = ActionFactory(SoulOrigins.id("stored_entity"), SerializableData {
            add("store", ApoliDataTypes.POWER_TYPE)
            add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
            add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
        }) { data, entity ->
            (entity.getPower(data.get<PowerType<*>>("store")) as? EntityStorePower)?.storedEntity?.let {
                data.get<ActionFactory<Entity>.Instance?>("entity_action")?.accept(it)
                data.get<ActionFactory<Pair<Entity, Entity>>.Instance?>("bientity_action")?.accept(Pair(entity, it))
            }
        }

        val condition: ConditionFactory<Entity> = ConditionFactory(SoulOrigins.id("entity_store"), SerializableData {
            add("store", ApoliDataTypes.POWER_TYPE)
            add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
            add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
        }) { data, entity ->
            val power = entity.getPower(data.get<PowerType<*>>("store"))
            if (power !is EntityStorePower) return@ConditionFactory false

            power.storedEntity != null
                    && (data.get<ConditionFactory<Entity>.Instance?>("entity_condition")?.test(power.storedEntity) ?: true)
                    && (data.get<ConditionFactory<Pair<Entity, Entity>>.Instance?>("bientity_condition")?.test(Pair(entity, power.storedEntity)) ?: true)
        }

        val isStoredCondition: ConditionFactory<Pair<Entity, Entity>> = ConditionFactory(SoulOrigins.id("stored_entity"), SerializableData {
            add("store", ApoliDataTypes.POWER_TYPE)
        }) { data, (actor, target) ->
            (actor.getPower(data.get<PowerType<*>>("store")) as? EntityStorePower)?.entityUUID == target.uuid
        }
    }
}
