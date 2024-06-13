package io.github.thebroccolibob.soulorigins.action

import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.util.Space
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.component1
import io.github.thebroccolibob.soulorigins.component2
import io.github.thebroccolibob.soulorigins.plus
import net.merchantpug.apugli.action.factory.bientity.RaycastBetweenAction
import net.merchantpug.apugli.util.RaycastUtil
import net.minecraft.entity.Entity
import net.minecraft.particle.ParticleEffect
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Pair
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

object RaycastBetweenCentersAction : RaycastBetweenAction() {
    override fun execute(data: SerializableData.Instance, pair: Pair<Entity, Entity>) {
        val (actor, target) = pair

        val distance = actor.distanceTo(target).toDouble()
        val direction = this.createDirectionVector(actor.pos, target.pos)
        val blockHitResult = RaycastUtil.raycastBlock(actor, distance, direction, Space.WORLD)
        val blockHitResultType = blockHitResult.type

        if (data.isPresent("block_action") && blockHitResultType == HitResult.Type.BLOCK) {
            this.createParticlesAtHitPos(data, actor, blockHitResult)
            this.onHitBlock(data, actor, blockHitResult)
        } else {
            this.createParticlesAtHitPos(data, actor, EntityHitResult(target, Vec3d(target.x, target.y + target.height / 2, target.z)))
        }
    }

    override fun createParticlesAtHitPos(data: SerializableData.Instance, entity: Entity, hitResult: HitResult) {
        if (data.isPresent("particle") && !entity.world.isClient()) {
            val particleEffect = data.get<Any>("particle") as ParticleEffect
            val distanceTo = hitResult.squaredDistanceTo(entity)

            val startPos = (if (data.getBoolean("player_center")) entity.pos.add(0.0, entity.height.toDouble(), 0.0) else entity.eyePos).let {
                if (!data.isPresent("offset")) return@let it


                it + data.get<Vec3d>("offset").toVector3f().apply {
                    data.get<Space>("space").toGlobal(this, entity)
                }
            }

            var d = data.getDouble("spacing")
            while (d < distanceTo) {
                val lerpValue = MathHelper.clamp(d / distanceTo, 0.0, 1.0)
                (entity.world as ServerWorld).spawnParticles(
                    particleEffect,
                    MathHelper.lerp(lerpValue, startPos.x, hitResult.pos.getX()),
                    MathHelper.lerp(lerpValue, startPos.y, hitResult.pos.getY()),
                    MathHelper.lerp(lerpValue, startPos.z, hitResult.pos.getZ()),
                    1,
                    0.0,
                    0.0,
                    0.0,
                    0.0
                )
                d += data.getDouble("spacing")
            }
        }
    }

    override fun getSerializableData(): SerializableData {
        return super.getSerializableData().apply {
            add("offset", SerializableDataTypes.VECTOR, null)
            add("space", ApoliDataTypes.SPACE, Space.LOCAL)
            add("player_center", SerializableDataTypes.BOOLEAN, false)
        }
    }

}
