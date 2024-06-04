package io.github.thebroccolibob.soulorigins.condition

import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.factory.condition.ConditionFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.SerializableData
import io.github.thebroccolibob.soulorigins.Soulorigins
import io.github.thebroccolibob.soulorigins.item.MobOrbItem
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.registry.Registry
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import java.util.function.BiFunction
import kotlin.math.max
import kotlin.math.min

private fun register(conditionFactory: ConditionFactory<Entity>) {
    Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.serializerId, conditionFactory)
}

private fun register(id: String, data: SerializableData, condition: BiFunction<SerializableData.Instance, Entity, Boolean>) {
    register(ConditionFactory(Identifier(Soulorigins.MOD_ID, id), data, condition))
}


fun registerSoulOriginsEntityConditions() {
    register("held_mob_orb", SerializableData {
        add("entity_type", SerializableDataTypes.ENTITY_TYPE, null)
        add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
    }) { data, entity ->
        (entity as? LivingEntity)?.getStackInHand(Hand.MAIN_HAND)?.let {
            val entityType = data.get<EntityType<*>?>("entity_type")
            val entityCondition = data.get<ConditionFactory<Entity>.Instance?>("entity_condition")
            it.item is MobOrbItem
                && (entityType === null || run {
                    entityType === MobOrbItem.getEntityType(it)
                })
                && (entityCondition === null || run {
                    val orbEntity = MobOrbItem.getEntity(it, entity.world)
                    val result = entityCondition.test(orbEntity)
                    orbEntity?.discard()
                    result
                })
        } ?: false
    }

    register("facing_east_west", SerializableData {})
    { _, entity ->
        entity.headYaw in -135f..-45f || entity.headYaw in 45f..135f
    }
}
