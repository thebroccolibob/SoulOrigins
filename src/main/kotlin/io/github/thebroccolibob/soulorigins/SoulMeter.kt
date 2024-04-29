package io.github.thebroccolibob.soulorigins

import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.PowerTypeRegistry
import io.github.apace100.apoli.power.VariableIntPower
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier

var PlayerEntity.soulMeter: Int
    get() {
        val component = PowerHolderComponent.KEY[this]
        val powerType: PowerType<*> = PowerTypeRegistry.get(Identifier("soul-origins", "soul_meter"))
        val p: Power = component.getPower(powerType)
        if (p is VariableIntPower) {
            return p.value
        }
        return 0
    }
    set(value) {
        val component = PowerHolderComponent.KEY[this]
        val powerType: PowerType<*> = PowerTypeRegistry.get(Identifier("soul-origins", "soul_meter"))
        val p: Power = component.getPower(powerType)
        if (p is VariableIntPower) {
            val newValue: Int = p.value + value
            p.setValue(newValue)
            PowerHolderComponent.syncPower(this, powerType)
        }
    }
