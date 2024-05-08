package io.github.thebroccolibob.soulorigins.condition

import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.factory.condition.ConditionFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.apace100.apoli.util.Comparison
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

private fun register(conditionFactory: ConditionFactory<ItemStack>) {
    Registry.register(ApoliRegistries.ITEM_CONDITION, conditionFactory.serializerId, conditionFactory)
}

fun registerSoulOriginsItemConditions() {
    register(ConditionFactory(
        Identifier(Soulorigins.MOD_ID, "backtank_air"),
        SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.INT)
    ) { data, stack ->
        stack.nbt?.getInt("Air")?.let {
            data.get<Comparison>("comparison").compare(
                it.toDouble(),
                data.getInt("compare_to").toDouble()
            )
        } ?: false
    })
}
