package io.github.thebroccolibob.soulorigins.condition

import io.github.apace100.apoli.power.factory.condition.ConditionFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.apace100.calio.data.SerializableData
import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.cca.OwnerComponent.Companion.owner
import io.github.thebroccolibob.soulorigins.component1
import io.github.thebroccolibob.soulorigins.component2
import io.github.thebroccolibob.soulorigins.power.EntityStorePower
import net.minecraft.entity.Entity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.registry.Registry
import java.util.function.BiFunction
import net.minecraft.util.Pair as McPair

private fun register(conditionFactory: ConditionFactory<McPair<Entity, Entity>>) {
    Registry.register(ApoliRegistries.BIENTITY_CONDITION, conditionFactory.serializerId, conditionFactory)
}

private fun register(id: String, data: SerializableData = SerializableData(), condition: BiFunction<SerializableData.Instance, McPair<Entity, Entity>, Boolean>) {
    register(ConditionFactory(SoulOrigins.id(id), data, condition))
}

private fun register(id: String, data: SerializableData = SerializableData(), condition: (SerializableData.Instance, actor: Entity, target: Entity) -> Boolean) {
    register(id, data) { dataInstance, (actor, target) -> condition(dataInstance, actor, target)}
}

private fun register(id: String, condition: (actor: Entity, target: Entity) -> Boolean) {
    register(id) { _, actor, target -> condition(actor, target)}
}

fun registerSoulOriginsBiEntityConditions() {
    register("monster_owner") { actor, target ->
        (target as? MobEntity)?.owner == actor
    }

    register(EntityStorePower.isStoredCondition)

    register("leashed") { actor, target ->
        (target as? MobEntity)?.holdingEntity == actor
    }

    register(FacingCondition.factory)
}
