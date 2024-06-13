package io.github.thebroccolibob.soulorigins

import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.PowerTypeRegistry
import io.github.apace100.apoli.power.VariableIntPower
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier

@Suppress("UNCHECKED_CAST")
val soulMeterType
    get() = PowerTypeRegistry.get(Identifier("soul-origins", "soul_meter")) as PowerType<VariableIntPower>

val PlayerEntity.soulMeter: VariableIntPower?
    get() {
        return getPower(soulMeterType)
    }

operator fun VariableIntPower?.plusAssign(change: Int) {
    this?.setValue(value + change)
}
operator fun VariableIntPower?.minusAssign(change: Int) {
    this?.setValue(value - change)
}

fun PlayerEntity.syncSoulMeter() {
    syncPower(soulMeterType)
}
