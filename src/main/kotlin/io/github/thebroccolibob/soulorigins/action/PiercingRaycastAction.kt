package io.github.thebroccolibob.soulorigins.action

import io.github.apace100.calio.data.SerializableData
import net.merchantpug.apugli.action.factory.entity.RaycastAction
import net.merchantpug.apugli.platform.Services
import net.minecraft.entity.Entity
import net.minecraft.util.hit.EntityHitResult

object FixedRaycastAction : RaycastAction() {
    override fun onHitEntity(
        data: SerializableData.Instance,
        actor: Entity?,
        result: EntityHitResult,
        calledThroughPierce: Boolean
    ) {
        if (Services.CONDITION.checkEntity(data, "target_condition", actor)) {
            val target = result.entity
            if (Services.CONDITION.checkBiEntity(data, "bientity_condition", actor, target)) {
                if (data.isPresent("target_action"))
                    Services.ACTION.executeEntity(data, "target_action", actor)

                if (data.isPresent("bientity_action"))
                    Services.ACTION.executeBiEntity(data, "bientity_action", actor, target)

                if (!calledThroughPierce)
                    this.executeSelfAction(data, actor)
            }
        }
    }
}
