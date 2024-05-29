package io.github.thebroccolibob.soulorigins

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.DefaultParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier


object SoulOriginsParticles {
    private fun register(id: String, alwaysShow: Boolean = false): DefaultParticleType {
        return Registry.register(Registries.PARTICLE_TYPE, Identifier(Soulorigins.MOD_ID, id), FabricParticleTypes.simple(alwaysShow))
    }

    val GUST = register("gust")
    val SMALL_GUST = register("small_gust")
    val GUST_EMITTER_LARGE = register("gust_emitter_large")
    val GUST_EMITTER_SMALL = register("gust_emitter_small")

    fun register() {}
}
