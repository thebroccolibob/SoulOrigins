package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.SerializableData
import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.getPowers
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import java.util.function.BiFunction
import kotlin.math.roundToInt

class BowSpeedPower(type: PowerType<BowSpeedPower>, entity: LivingEntity?, val multiplier: Double) : Power(type, entity) {
    companion object {
        val factory: PowerFactory<BowSpeedPower> = PowerFactory(
            SoulOrigins.id("bow_speed"),
            SerializableData {
                add("multiplier", SerializableDataTypes.DOUBLE, 1.0)
            }
        ) { data ->
            BiFunction {
                    type: PowerType<BowSpeedPower>, player: LivingEntity? -> BowSpeedPower(type, player, data.get("multiplier"))
            }
        }.allowCondition()

        @JvmStatic
        fun getMultiplier(entity: LivingEntity?): Double {
            if (entity !is PlayerEntity) return 1.0

            return entity.getPowers<BowSpeedPower>().map(BowSpeedPower::multiplier).fold(1.0, Double::times)
        }

        @JvmStatic
        fun applyMultiplier(entity: LivingEntity, ticksUsing: Int): Int {
            return (ticksUsing * getMultiplier(entity)).roundToInt()
        }
    }
}
