package io.github.thebroccolibob.soulorigins.action

import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.CooldownPower
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.SerializableData
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Registry
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import java.util.function.BiConsumer

private fun register(actionFactory: ActionFactory<Entity>) {
    Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.serializerId, actionFactory)
}

private fun register(id: String, data: SerializableData, effect: BiConsumer<SerializableData.Instance, Entity>) {
    register(ActionFactory(Identifier(Soulorigins.MOD_ID, id), data, effect))
}

fun registerSoulOriginsEntityActions() {
    register("change_cooldown", SerializableData {
        add("cooldown", ApoliDataTypes.POWER_TYPE)
        add("change", SerializableDataTypes.INT)
    }) { data, entity ->
        if (entity is LivingEntity) {
            val component = PowerHolderComponent.KEY[entity]
            val powerType = data.get<PowerType<*>>("cooldown")
            val p = component.getPower(powerType)
            val change = data.getInt("change")
            if (p is CooldownPower) {
                p.setCooldown((p.cooldownDuration - p.remainingTicks) - change)
                PowerHolderComponent.syncPower(entity, powerType)
            }
        }
    }

    register("despawn", SerializableData()) { _, entity ->
        entity.discard()
    }

    register(SpawnEntityAction.factory)

    register("block_particle", SerializableData {
        add("count", SerializableDataTypes.INT)
        add("speed", SerializableDataTypes.FLOAT, 0.0F)
        add("force", SerializableDataTypes.BOOLEAN, false)
        add("spread", SerializableDataTypes.VECTOR, Vec3d(0.5, 0.25, 0.5))
        add("offset_y", SerializableDataTypes.FLOAT, 0.5F)
    }) {
        data, entity ->

        if (entity.world.isClient) {
            return@register
        }
        val serverWorld = entity.world as ServerWorld
        val count = data.get<Int>("count")
        if (count <= 0) return@register
        val speed = data.get<Float>("speed")
        val spread = data.get<Vec3d>("spread")
        val deltaX = (entity.width * spread.x).toFloat()
        val deltaY = (entity.height * spread.y).toFloat()
        val deltaZ = (entity.width * spread.z).toFloat()
        val offsetY = entity.height * data.getFloat("offset_y")
        serverWorld.spawnParticles(
            BlockStateParticleEffect(ParticleTypes.BLOCK, serverWorld.getBlockState(entity.blockPos.offset(Direction.DOWN))),
            entity.x,
            entity.y + offsetY,
            entity.z,
            count,
            deltaX.toDouble(),
            deltaY.toDouble(),
            deltaZ.toDouble(),
            speed.toDouble()
        )
    }
}
