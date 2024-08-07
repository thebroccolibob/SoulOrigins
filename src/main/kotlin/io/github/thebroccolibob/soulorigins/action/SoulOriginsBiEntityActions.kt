package io.github.thebroccolibob.soulorigins.action

import io.github.apace100.apoli.Apoli.SCHEDULER
import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.CooldownPower
import io.github.apace100.apoli.power.PowerTypeReference
import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.*
import io.github.thebroccolibob.soulorigins.cca.OwnerComponent.Companion.owner
import io.github.thebroccolibob.soulorigins.item.MobOrbItem
import io.github.thebroccolibob.soulorigins.item.SoulOriginsItems
import io.github.thebroccolibob.soulorigins.power.EntityStorePower
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.util.math.Vec3d
import java.util.function.BiConsumer
import net.minecraft.util.Pair as McPair

private fun register(actionFactory: ActionFactory<McPair<Entity, Entity>>) {
    Registry.register(ApoliRegistries.BIENTITY_ACTION, actionFactory.serializerId, actionFactory)
}

private fun register(id: String, data: SerializableData = SerializableData(), effect: BiConsumer<SerializableData.Instance, McPair<Entity, Entity>>) {
    register(ActionFactory(SoulOrigins.id(id), data, effect))
}

private inline fun register(id: String, data: SerializableData = SerializableData(), crossinline effect: (SerializableData.Instance, actor: Entity, target: Entity) -> Unit) {
    register(id, data) { dataInstance, (actor, target) -> effect(dataInstance, actor, target) }
}

private inline fun register(id: String, crossinline effect: (actor: Entity, target: Entity) -> Unit) {
    register(id) { _, actor, target -> effect(actor, target) }
}

fun registerSoulOriginsBiEntityActions() {
    register("absorb_mob_orb") { actor, target ->
        // Generate orb and inject mob nbt data
        val stack = ItemStack(SoulOriginsItems.MOB_ORB)

        MobOrbItem.setEntity(stack, target)

        target.customName?.let(stack::setCustomName)
        target.discard()

        // Give player orb with data
        (actor as? PlayerEntity)?.run {
            if (!giveItemStack(stack)) dropStack(stack)
        }

        return@register
    }

    register("health_delay", SerializableData {
        add("action", ApoliDataTypes.BIENTITY_ACTION) // For the delayed action
        add("multiplier", SerializableDataTypes.FLOAT) // for customizability
        add("cooldown", ApoliDataTypes.POWER_TYPE, null)
        add("bonus_cooldown", SerializableDataTypes.INT, 20)
    }) {
        data, entityPair ->
        val (actor, target) = entityPair

        val action = data.get<ActionFactory<McPair<Entity, Entity>>.Instance>("action")

        val delay = (data.getFloat("multiplier") * (target as LivingEntity).maxHealth).toInt()
        SCHEDULER.queue({ _ ->
            action.accept(entityPair)
        }, delay)

        if (actor is LivingEntity) {
            data.get<PowerTypeReference<*>?>("cooldown")?.let { type ->
                (actor.getPower(type) as? CooldownPower)?.let {
                    it.setCooldown(it.cooldownDuration - (delay + data.getInt("bonus_cooldown")))
                    actor.syncPower(type)
                }
            }
        }
    }

    register("raycast_between", RaycastBetweenCentersAction.serializableData, RaycastBetweenCentersAction::execute)

    register(EntityStorePower.setAction)

    register("proportional_velocity", SerializableData {
        add("multiplier", SerializableDataTypes.VECTOR, Vec3d(1.0, 1.0, 1.0))
        add("client", SerializableDataTypes.BOOLEAN, true)
        add("server", SerializableDataTypes.BOOLEAN, true)
        add("set", SerializableDataTypes.BOOLEAN, false)
    }) { data, (actor, target) ->

        if (target is PlayerEntity && (if (target.getWorld().isClient) !data.getBoolean("client") else !data.getBoolean("server"))) return@register

        val diff = actor.pos - target.pos

        val change = diff / diff.length() * data.get<Vec3d>("multiplier")

        if (data.getBoolean("set")) {
            target.velocity = change
        } else {
            target.addVelocity(change)
        }

        target.velocityModified = true
    }

    register("set_mob_owned") { actor, target ->
        if (target !is MobEntity || actor !is LivingEntity) return@register
        target.owner = actor
        target.setPersistent()
        (target as OwnedGoalAdder).`soulOrigins$addOwnedGoals`()
    }

    register("pathfind_towards", SerializableData {
        add("speed", SerializableDataTypes.DOUBLE, 1.0)
    }) { data, actor, target ->
        (actor as? PathAwareEntity)?.navigation?.startMovingTo(target, data.getDouble("speed"))
    }

    register("leash") { actor, target ->
        (target as? MobEntity)?.attachLeash(actor, true)
    }
}
