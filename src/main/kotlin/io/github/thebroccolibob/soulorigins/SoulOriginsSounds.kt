package io.github.thebroccolibob.soulorigins

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier

object SoulOriginsSounds {
    fun register(identifier: Identifier): SoundEvent {
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier))
    }

    fun register(path: String) = register(SoulOrigins.id(path))

    val WIND_BURST = register("power.wind.burst")
    val WIND_BURST_LARGE = register("power.wind.burst_large")
    val WIND_LEVELUP = register("items.wind_shard.level")
    val WIND_BURST_AMBIENCE = register("power.wind.afterburst")
    val WIND_FLOAT = register("power.wind.float")
    val WIND_LAND = register("power.wind.land")

    val SORCERER_TP = register("power.sorcerer.teleport")
    val SORCERER_SCULK_GARDEN = register("power.sorcerer.sculk")

    val ARTIFICER_SURFACE_PLACE = register("block.artificer_surface.place")
    val ARTIFICER_SURFACE_DESTROY = register("block.artificer_surface.destroy")

    val ARTIFICER_SURFACE_GROUP = BlockSoundGroup(0.2f, 1.5f,
        ARTIFICER_SURFACE_DESTROY,
        SoundEvents.BLOCK_METAL_STEP,
        ARTIFICER_SURFACE_PLACE,
        SoundEvents.BLOCK_METAL_HIT,
        SoundEvents.BLOCK_METAL_FALL
    )

    fun register() {}
}
