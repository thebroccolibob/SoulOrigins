package io.github.thebroccolibob.soulorigins.action

import io.github.apace100.apoli.Apoli
import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.apoli.power.factory.condition.ConditionFactory
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.SerializableData
import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.plus
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.command.CommandOutput
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Pair
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import net.minecraft.world.RaycastContext.FluidHandling
import net.minecraft.world.RaycastContext.ShapeType

object SpawnEntityRaycastAction {
    fun action(data: SerializableData.Instance, entity: Entity) {
        val origin = Vec3d(entity.x, entity.eyeY, entity.z)
        val direction = entity.getRotationVec(1f)
        val target = origin.add(direction.multiply(data.get<Double>("distance")))

        data.get<ActionFactory<Entity>.Instance?>("before_action")?.accept(entity)

        var hitResult: HitResult? = null
        if (data.getBoolean("entities_intercept")) {
            hitResult = performEntityRaycast(entity, origin, target, data.get("intercept_entity_condition"), data.get("intercept_bientity_condition"))
        }
        val blockHit = performBlockRaycast(
            entity,
            origin,
            target,
            data.get("shape_type"),
            data.get("fluid_handling")
        )
        if (!data.getBoolean("missable") || blockHit.type != HitResult.Type.MISS) {
            if (hitResult == null || hitResult.type == HitResult.Type.MISS) {
                hitResult = blockHit
            } else {
                if (hitResult.squaredDistanceTo(entity) > blockHit.squaredDistanceTo(entity)) {
                    hitResult = blockHit
                }
            }
        }
        if (hitResult != null) {
            if (data.isPresent("command_along_ray")) {
                executeStepCommands(
                    entity,
                    origin,
                    hitResult.pos,
                    data.getString("command_along_ray"),
                    data.getDouble("command_step")
                )
            }
            if (hitResult is BlockHitResult && entity.world is ServerWorld) {
                data.get<EntityType<*>>("entity_type").spawn(
                    entity.world as ServerWorld,
                    if (data.getBoolean("in_block")) hitResult.blockPos else hitResult.blockPos.offset(hitResult.side),
                    SpawnReason.COMMAND
                )?.let {  spawnedEntity ->
                    data.get<NbtCompound?>("nbt")?.let {
                        spawnedEntity.readNbt(it)
                    }
                    data.get<ActionFactory<Entity>.Instance?>("entity_action")?.accept(spawnedEntity)
                    data.get<ActionFactory<Pair<Entity, Entity>>.Instance?>("bientity_action")?.accept(Pair(entity, spawnedEntity))
                    data.get<Vec3d?>("offset")?.let {
                        spawnedEntity.setPosition(spawnedEntity.pos + it)
                    }
                }
            }
            if (hitResult is EntityHitResult) {
                data.get<ActionFactory<Entity>.Instance?>("intercept_entity_action")?.accept(hitResult.entity)
                data.get<ActionFactory<Pair<Entity, Entity>>.Instance?>("intercept_bientity_action")?.accept(
                    Pair(entity, hitResult.entity)
                )
            }
            data.get<ActionFactory<Entity>.Instance?>("hit_action")?.accept(entity)
        } else {
            data.get<ActionFactory<Entity>.Instance?>("miss_action")?.accept(entity)
        }
    }

    private fun performBlockRaycast(
        source: Entity,
        origin: Vec3d,
        target: Vec3d,
        shapeType: ShapeType,
        fluidHandling: FluidHandling
    ): BlockHitResult {
        val context = RaycastContext(origin, target, shapeType, fluidHandling, source)
        return source.world.raycast(context)
    }

    private fun performEntityRaycast(
        source: Entity,
        origin: Vec3d,
        target: Vec3d,
        entityCondition: ConditionFactory<Entity>.Instance?,
        biEntityCondition: ConditionFactory<Pair<Entity, Entity>>.Instance?
    ): EntityHitResult? {
        val ray = target.subtract(origin)
        val box = source.boundingBox.stretch(ray).expand(1.0, 1.0, 1.0)
        val entityHitResult = ProjectileUtil.raycast(source, origin, target, box,
            { entityx: Entity ->
                !entityx.isSpectator && (biEntityCondition?.test(Pair(source, entityx))?: true) && (entityCondition?.test(entityx) ?: true)
            }, ray.lengthSquared()
        )
        return entityHitResult
    }

    private fun executeStepCommands(entity: Entity, origin: Vec3d, target: Vec3d, command: String, step: Double) {
        val server = entity.world.server
        if (server != null) {
            val direction = target.subtract(origin).normalize()
            val length = origin.distanceTo(target)
            var current = 0.0
            while (current < length) {
                val validOutput = entity !is ServerPlayerEntity || entity.networkHandler != null
                val source = ServerCommandSource(
                    if (Apoli.config.executeCommand.showOutput && validOutput) entity else CommandOutput.DUMMY,
                    origin.add(direction.multiply(current)),
                    entity.rotationClient,
                    if (entity.world is ServerWorld) entity.world as ServerWorld else null,
                    Apoli.config.executeCommand.permissionLevel,
                    entity.name.string,
                    entity.displayName,
                    entity.world.server,
                    entity
                )
                server.commandManager.executeWithPrefix(source, command)
                current += step
            }
        }
    }

    val factory = ActionFactory(SoulOrigins.id("spawn_entity_raycast"), SerializableData {
        add("distance", SerializableDataTypes.DOUBLE)
        add("shape_type", SerializableDataTypes.SHAPE_TYPE, ShapeType.OUTLINE)
        add("fluid_handling", SerializableDataTypes.FLUID_HANDLING, FluidHandling.ANY)

        add("command_along_ray", SerializableDataTypes.STRING, null)
        add("command_step", SerializableDataTypes.DOUBLE, 1.0)

        add("entity_type", SerializableDataTypes.ENTITY_TYPE)
        add("nbt", SerializableDataTypes.NBT, null)
        add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
        add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
        add("in_block", SerializableDataTypes.BOOLEAN, false)
        add("offset", SerializableDataTypes.VECTOR, null)
        add("missable", SerializableDataTypes.BOOLEAN, true)

        add("entities_intercept", SerializableDataTypes.BOOLEAN, false)
        add("intercept_entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
        add("intercept_bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
        add("intercept_entity_action", ApoliDataTypes.ENTITY_ACTION, null)
        add("intercept_bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)

        add("before_action", ApoliDataTypes.ENTITY_ACTION, null)
        add("hit_action", ApoliDataTypes.ENTITY_ACTION, null)
        add("miss_action", ApoliDataTypes.ENTITY_ACTION, null)
    }, ::action)
}
