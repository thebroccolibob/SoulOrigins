package io.github.thebroccolibob.soulorigins.action

import io.github.apace100.apoli.Apoli.SCHEDULER
import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.*
import io.github.thebroccolibob.soulorigins.entity.OwnableMonster
import io.github.thebroccolibob.soulorigins.entity.isTamed
import io.github.thebroccolibob.soulorigins.item.MobOrbItem
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import java.util.function.BiConsumer
import net.minecraft.util.Pair as McPair

private fun register(actionFactory: ActionFactory<McPair<Entity, Entity>>) {
    Registry.register(ApoliRegistries.BIENTITY_ACTION, actionFactory.serializerId, actionFactory)
}

private fun register(id: String, data: SerializableData, effect: BiConsumer<SerializableData.Instance, McPair<Entity, Entity>>) {
    register(ActionFactory(Identifier(Soulorigins.MOD_ID, id), data, effect))
}

fun registerSoulOriginsBiEntityActions() {
    register("absorb_mob_orb", SerializableData()) { _, (actor, target) ->
        // Generate orb and inject mob nbt data
        val stack = ItemStack(SouloriginsItems.MOB_ORB)

        MobOrbItem.setEntity(stack, target)

        target.customName?.let(stack::setCustomName)
        target.discard()

        // Mana refund
        val user = actor as? PlayerEntity
        if ((target as? OwnableMonster)?.isTamed == true) {
            if (user != null) {
                user.soulMeter += 2
            }
            user?.syncSoulMeter()
        }
        // Give player orb with data
        user?.giveItemStack(stack)
        return@register
    }

    register("health_delay", SerializableData {
        add("action", ApoliDataTypes.BIENTITY_ACTION) // For the delayed action
        add("multiplier", SerializableDataTypes.FLOAT) // for customizability
    }) {
        data, entityPair ->
        val (_, target) = entityPair

        val action = data.get<ActionFactory<McPair<Entity, Entity>>.Instance>("action")
        SCHEDULER.queue({ _ ->
            action.accept(entityPair)
        }, (data.getFloat("multiplier") * (target as LivingEntity).maxHealth).toInt())
        // for time = health / 5 seconds, use "multiplier":
        // all good? ready to push?
    }
}
