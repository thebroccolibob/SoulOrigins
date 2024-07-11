package io.github.thebroccolibob.soulorigins.action

import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.SerializableData
import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.component1
import io.github.thebroccolibob.soulorigins.component2
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.world.World
import java.util.function.BiConsumer
import net.minecraft.util.Pair as McPair

private fun register(actionFactory: ActionFactory<McPair<World, ItemStack>>) {
    Registry.register(ApoliRegistries.ITEM_ACTION, actionFactory.serializerId, actionFactory)
}

private fun register(id: String, data: SerializableData, effect: BiConsumer<SerializableData.Instance, McPair<World, ItemStack>>) {
    register(ActionFactory(SoulOrigins.id(id), data, effect))
}

fun registerSoulOriginsItemActions() {
    register("change_backtank_air", SerializableData {
        add("change", SerializableDataTypes.INT)
    }) { data, (_, itemStack) ->
        itemStack.nbt?.getInt("Air")?.let {
            itemStack.orCreateNbt.putInt(
                "Air",
                it + data.getInt("change")
            )
        }
    }
}
