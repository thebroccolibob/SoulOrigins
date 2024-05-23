package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import net.minecraft.registry.Registry

private fun register(powerFactory: PowerFactory<*>) {
    Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.serializerId, powerFactory)
}

fun registerSoulOriginsPowers() {
    register(BowSpeedPower.factory)
    register(EmissiveOverlayPower.factory)
    register(DisengagePower.factory)
}
