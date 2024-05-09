package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.SerializableData
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import java.util.function.BiFunction

class DisengagePower(type: PowerType<*>, entity: LivingEntity?, val distance: Double) : Power(type, entity) {
    companion object {
        val factory: PowerFactory<DisengagePower> = PowerFactory(
            Identifier(Soulorigins.MOD_ID, "disengage"),
            SerializableData {
                add("distance_multiplier", SerializableDataTypes.DOUBLE, 16.0)
            }
        ) { data ->
            BiFunction {
                type: PowerType<*>, player: LivingEntity? -> DisengagePower(type, player, data.get("distance_multiplier"))
            }
        }.allowCondition()
    }
}
