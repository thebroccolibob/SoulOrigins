package io.github.thebroccolibob.soulorigins

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier

object SoulOriginsSounds {
    fun register(identifier: Identifier): SoundEvent {
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier))
    }

    fun register(path: String) = register(Identifier(Soulorigins.MOD_ID, path))

    val WIND_BURST = register("power.wind.burst")
    val WIND_BURST_LARGE = register("power.wind.burst_large")
    val WIND_LEVELUP = register("items.wind_shard.level")

    fun register() {}
}
