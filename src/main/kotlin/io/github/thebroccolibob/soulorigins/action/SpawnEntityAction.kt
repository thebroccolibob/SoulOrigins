package io.github.thebroccolibob.soulorigins.action

import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.apoli.util.MiscUtil
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.Soulorigins
import io.github.thebroccolibob.soulorigins.toNullable
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.Pair
import java.util.function.Consumer

object SpawnEntityAction {
    fun action(data: SerializableData.Instance, entity: Entity) {
        if (entity.world.isClient) return

        val serverWorld = entity.world as ServerWorld
        val entityType = data.get<EntityType<*>>("entity_type")
        val entityNbt = data.get<NbtCompound>("tag")

        val entityToSpawn = MiscUtil.getEntityWithPassengers(
            serverWorld,
            entityType,
            entityNbt,
            entity.pos,
            entity.yaw,
            entity.pitch
        ).toNullable()

        if (entityToSpawn == null) return

        serverWorld.spawnNewEntityAndPassengers(entityToSpawn)
        data.ifPresent(
            "entity_action"
        ) { entityAction: Consumer<Entity> ->
            entityAction.accept(entityToSpawn)
        }
        data.ifPresent(
            "bientity_action"
        ) { biEntityAction: Consumer<Pair<Entity, Entity>> ->
            biEntityAction.accept(Pair(entity, entityToSpawn))
        }
    }

    val factory: ActionFactory<Entity> = ActionFactory(
            Identifier(Soulorigins.MOD_ID, "spawn_entity"),
            SerializableData()
                .add("entity_type", SerializableDataTypes.ENTITY_TYPE)
                .add("tag", SerializableDataTypes.NBT, null)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
        ) { data: SerializableData.Instance, entity: Entity ->
            action(data, entity)
        }
}

