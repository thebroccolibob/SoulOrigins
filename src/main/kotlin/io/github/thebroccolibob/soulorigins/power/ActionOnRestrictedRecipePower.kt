package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.SerializableData
import io.github.thebroccolibob.soulorigins.SoulOrigins
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import java.util.function.BiFunction

class ActionOnRestrictedRecipePower(type: PowerType<*>, entity: LivingEntity?, private val recipeIds: List<Identifier>, private val entityAction: ActionFactory<Entity>.Instance) : Power(type, entity) {
    fun tryExecute(recipeId: Identifier) {
        if (recipeId in recipeIds) {
            entityAction.accept(entity)
        }
    }

    companion object {
        val factory: PowerFactory<Power> = PowerFactory(
            SoulOrigins.id("action_on_restricted_recipe"),
            SerializableData {
                add("entity_action", ApoliDataTypes.ENTITY_ACTION)
                add("recipe", SerializableDataTypes.IDENTIFIER, null)
                add("recipes", SerializableDataTypes.IDENTIFIERS, null)
            }
        ) { data ->
            val recipeIds = mutableListOf<Identifier>()
            data.get<Identifier?>("recipe") ?.let(recipeIds::add)
            data.get<List<Identifier>?>("recipes")?.let(recipeIds::addAll)
            BiFunction { type, entity -> ActionOnRestrictedRecipePower(type, entity, recipeIds, data.get("entity_action")) }
        }.allowCondition()
    }
}
