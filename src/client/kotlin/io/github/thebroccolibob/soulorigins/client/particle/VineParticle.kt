package io.github.thebroccolibob.soulorigins.client.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType

@Environment(EnvType.CLIENT)
class VineParticle(clientWorld: ClientWorld?, d: Double, e: Double, f: Double) :
    SpriteBillboardParticle(clientWorld, d, e, f) {

        init {
            gravityStrength = 0f
            maxAge = 1
            collidesWithWorld = false
        }

    override fun getSize(tickDelta: Float) = 0.25f

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_OPAQUE

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            parameters: DefaultParticleType,
            world: ClientWorld,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            return VineParticle(world, x, y, z).apply {
                setSprite(spriteProvider)
            }
        }

    }
}
