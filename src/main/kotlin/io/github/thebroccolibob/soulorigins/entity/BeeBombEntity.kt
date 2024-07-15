package io.github.thebroccolibob.soulorigins.entity

import io.github.thebroccolibob.soulorigins.block.entity.BeeBombBlockEntity.Companion.BEES_KEY
import io.github.thebroccolibob.soulorigins.getList
import io.github.thebroccolibob.soulorigins.hasPower
import io.github.thebroccolibob.soulorigins.mixin.TntEntityAccessor
import io.github.thebroccolibob.soulorigins.random
import io.github.thebroccolibob.soulorigins.times
import net.merchantpug.apugli.power.PreventBeeAngerPower
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MovementType
import net.minecraft.entity.TntEntity
import net.minecraft.entity.passive.BeeEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.Box
import net.minecraft.world.World
import net.minecraft.world.World.ExplosionSourceType
import kotlin.math.cos
import kotlin.math.sin

class BeeBombEntity(entityType: EntityType<out BeeBombEntity>, world: World, private var bees: List<NbtCompound> = listOf(NbtCompound(), NbtCompound(), NbtCompound())) : TntEntity(entityType, world) {
    constructor(world: World, bees: List<NbtCompound>, x: Double, y: Double, z: Double, igniter: LivingEntity?) : this(SoulOriginsEntities.BEE_BOMB, world, bees) {
        setPosition(x, y, z)
        val d = world.random.nextDouble() * (Math.PI * 2).toFloat()
        setVelocity(-sin(d) * 0.02, 0.2, -cos(d) * 0.02)
        fuse = 80
        prevX = x
        prevY = y
        prevZ = z
        @Suppress("CAST_NEVER_SUCCEEDS")
        (this as TntEntityAccessor).setCausingEntity(igniter)
    }

    override fun tick() {
        if (!hasNoGravity()) {
            velocity = velocity.add(0.0, -0.04, 0.0)
        }

        move(MovementType.SELF, velocity)
        velocity *= 0.98
        if (isOnGround) {
            velocity = velocity.multiply(0.7, -0.5, 0.7)
        }

        fuse -= 1

        if (fuse - 1 <= 0) {
            discard()
            if (!world.isClient) {
                explode()
            }
        } else {
            updateWaterState()
            if (world.isClient) {
                world.addParticle(ParticleTypes.SMOKE, this.x, this.y + 0.5, this.z, 0.0, 0.0, 0.0)
            }
        }
    }

    private fun explode() {
        world.createExplosion(this, this.x, getBodyY(0.0625), this.z, 1f, ExplosionSourceType.TNT)
        val entities = world.getOtherEntities(null, Box.of(pos, ANGER_RANGE, ANGER_RANGE, ANGER_RANGE)) { it.isLiving && it !is BeeEntity && !it.hasPower<PreventBeeAngerPower>() }
        for (bee in bees) {
            EntityType.BEE.create(world)?.apply {
                readNbt(bee)
                refreshPositionAndAngles(this@BeeBombEntity.blockPos, yaw, pitch)
                setVelocity(
                    this@BeeBombEntity.random.nextTriangular(0.0, MAX_VELOCITY),
                    this@BeeBombEntity.random.nextTriangular(0.0, MAX_VELOCITY),
                    this@BeeBombEntity.random.nextTriangular(0.0, MAX_VELOCITY),
                )
                this@BeeBombEntity.world.spawnEntity(this)
                universallyAnger()
                if (entities.isNotEmpty()) {
                    target = entities.random(world.random) as LivingEntity
                }
            }
        }
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.put(BEES_KEY, NbtList().apply { bees.forEach(::add) })
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        bees = nbt.getList(BEES_KEY, NbtCompound.COMPOUND_TYPE).map { it as NbtCompound }
    }

    companion object {
        const val ANGER_RANGE = 16.0
        const val MAX_VELOCITY = 0.5
    }
}
