package io.github.thebroccolibob.soulorigins.client.particle

import io.github.thebroccolibob.soulorigins.SoulOriginsParticles
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.NoRenderParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType

@Environment(EnvType.CLIENT)
class GustEmitterParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    private val deviation: Double,
    maxAge: Int,
    interval: Int
) :
    NoRenderParticle(world, x, y, z, 0.0, 0.0, 0.0) {
    private val interval: Int

    init {
        this.maxAge = maxAge
        this.interval = interval
    }

    override fun tick() {
        if (this.age % (this.interval + 1) == 0) {
            for (i in 0..2) {
                val d = this.x + (random.nextDouble() - random.nextDouble()) * this.deviation
                val e = this.y + (random.nextDouble() - random.nextDouble()) * this.deviation
                val f = this.z + (random.nextDouble() - random.nextDouble()) * this.deviation
                world.addParticle(SoulOriginsParticles.GUST, d, e, f, (age.toFloat() / maxAge.toFloat()).toDouble(), 0.0, 0.0)
            }
        }

        if (age++ == this.maxAge) {
            this.markDead()
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val deviation: Double, private val maxAge: Int, private val interval: Int) :
        ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            simpleParticleType: DefaultParticleType,
            clientWorld: ClientWorld,
            d: Double,
            e: Double,
            f: Double,
            g: Double,
            h: Double,
            i: Double
        ): Particle {
            return GustEmitterParticle(clientWorld, d, e, f, this.deviation, this.maxAge, this.interval)
        }
    }
}
