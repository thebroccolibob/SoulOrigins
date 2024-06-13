package io.github.thebroccolibob.soulorigins.action

import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.*
import net.minecraft.item.BoneMealItem
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.WorldEvents
import org.apache.commons.lang3.tuple.Triple
import java.util.function.BiConsumer

private fun register(actionFactory: ActionFactory<Triple<World, BlockPos, Direction>>) {
    Registry.register(ApoliRegistries.BLOCK_ACTION, actionFactory.serializerId, actionFactory)
}

private fun register(id: String, data: SerializableData, effect: BiConsumer<SerializableData.Instance, Triple<World, BlockPos, Direction>>) {
    register(ActionFactory(SoulOrigins.id(id), data, effect))
}

fun registerSoulOriginsBlockActions() {
    register("bonemeal_around", SerializableData {
        add("rangeX", SerializableDataTypes.INT)
        add("rangeY", SerializableDataTypes.INT)
        add("rangeZ", SerializableDataTypes.INT)
        add("count", SerializableDataTypes.INT, 1)
        add("attempts", SerializableDataTypes.INT, 16)
    }) { data, (world, pos, _) ->
        val rangeX = data.getInt("rangeX")
        val rangeY = data.getInt("rangeY")
        val rangeZ = data.getInt("rangeZ")

        for (i in 0..(data.getInt("attempts") + 3)) {
            val checkPos = if (i < 3)
                pos.add(0, i - 1, 0)
            else pos.add(
                world.random.nextBetween(-rangeX, rangeX),
                world.random.nextBetween(-rangeY, rangeY),
                world.random.nextBetween(-rangeZ, rangeZ),
            )

            if (BoneMealItem.useOnFertilizable(ItemStack.EMPTY, world, checkPos) || BoneMealItem.useOnGround(ItemStack.EMPTY, world, checkPos, Direction.UP)) {
                if (!world.isClient) {
                    world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, checkPos, 0)
                }
                break
            }
        }
    }
}
