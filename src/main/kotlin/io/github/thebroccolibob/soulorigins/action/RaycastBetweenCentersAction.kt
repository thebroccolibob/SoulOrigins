package io.github.thebroccolibob.soulorigins.action

import io.github.apace100.apoli.util.Space
import io.github.apace100.calio.data.SerializableData
import io.github.thebroccolibob.soulorigins.component1
import io.github.thebroccolibob.soulorigins.component2
import net.merchantpug.apugli.action.factory.bientity.RaycastBetweenAction
import net.merchantpug.apugli.util.RaycastUtil
import net.minecraft.entity.Entity
import net.minecraft.util.Pair
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
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
}
