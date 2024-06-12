package io.github.thebroccolibob.soulorigins.condition

import io.github.apace100.apoli.power.factory.condition.ConditionFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.apace100.calio.data.SerializableData
import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.component1
import io.github.thebroccolibob.soulorigins.component2
import io.github.thebroccolibob.soulorigins.entity.OwnableMonster
import io.github.thebroccolibob.soulorigins.entity.owner
import io.github.thebroccolibob.soulorigins.power.EntityStorePower
import net.minecraft.entity.Entity
import net.minecraft.registry.Registry
import net.minecraft.util.Pair as McPair

private fun register(conditionFactory: ConditionFactory<McPair<Entity, Entity>>) {
    Registry.register(ApoliRegistries.BIENTITY_CONDITION, conditionFactory.serializerId, conditionFactory)
}

fun registerSoulOriginsBiEntityConditions() {
    register(
        ConditionFactory(
            SoulOrigins.id("monster_owner"), SerializableData()
        ) {_, (actor, target) ->
            (target as? OwnableMonster)?.owner == actor
        }
    )

    register(EntityStorePower.isStoredCondition)
}
