package io.github.thebroccolibob.soulorigins.client.particle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.particle.*
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType

@Environment(EnvType.CLIENT)
class GustParticle (
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    private val spriteProvider: SpriteProvider
) : SpriteBillboardParticle(world, x, y, z) {
    init {
        this.setSpriteForAge(spriteProvider)
        this.maxAge = 12 + random.nextInt(4)
        this.scale = 1.0f
        this.setBoundingBoxSpacing(1.0f, 1.0f)
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_LIT

    public override fun getBrightness(tint: Float) = 15728880

    override fun tick() {
        if (age++ >= this.maxAge) {
            this.markDead()
        } else {
            this.setSpriteForAge(this.spriteProvider)
        }
    }

    @Environment(EnvType.CLIENT)
    class SmallGustFactory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
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
            val particle: Particle = GustParticle(clientWorld, d, e, f, this.spriteProvider)
            particle.scale(0.15f)
            return particle
        }
    }

    @Environment(EnvType.CLIENT)
    class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            simpleParticleType: DefaultParticleType?,
            clientWorld: ClientWorld,
            d: Double,
            e: Double,
            f: Double,
            g: Double,
            h: Double,
            i: Double
        ): Particle {
            return GustParticle(clientWorld, d, e, f, this.spriteProvider)
        }
    }
}
