package io.github.thebroccolibob.soulorigins.action

import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.apace100.calio.data.SerializableData
import io.github.thebroccolibob.soulorigins.*
import io.github.thebroccolibob.soulorigins.entity.OwnableMonster
import io.github.thebroccolibob.soulorigins.entity.isTamed
import io.github.thebroccolibob.soulorigins.item.MobOrbItem.Companion.ENTITY_NBT
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import net.minecraft.entity.Entity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.SkeletonHorseEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.Pair as McPair

private fun register(actionFactory: ActionFactory<McPair<Entity, Entity>>) {
    Registry.register(ApoliRegistries.BIENTITY_ACTION, actionFactory.serializerId, actionFactory)
}

fun registerSoulOriginsBiEntityActions() {
    register(
        ActionFactory(
        Identifier(Soulorigins.MOD_ID, "absorb_mob_orb"), SerializableData()
    ) { _, (actor, target) ->
            // Generate orb and inject mob nbt data
            val stack = ItemStack(SouloriginsItems.MOB_ORB)

            val targetEntity = (target as? OwnableMonster) ?: (target.firstPassenger as? OwnableMonster) ?: return@ActionFactory
            targetEntity as MobEntity

            stack.orCreateNbt.put(ENTITY_NBT, NbtCompound().apply {
                // Get mob NBT data
                targetEntity.saveSelfNbt(this)
                remove("Pos")
                remove("UUID")
                remove("ActiveEffects")
            })
            (targetEntity.vehicle as? SkeletonHorseEntity)?.let {
                stack.orCreateNbt.put(ENTITY_NBT, NbtCompound().apply {
                    it.saveSelfNbt(this)
                    remove("Pos")
                    remove("UUID")
                    remove("ActiveEffects")
                })
                it.discard()
            }
            targetEntity.customName?.let(stack::setCustomName)
            targetEntity.discard()

            // Mana refund
            val user = actor as? PlayerEntity
            if ((targetEntity as OwnableMonster).isTamed) {
                if (user != null) {
                    user.soulMeter += 2
                }
                user?.syncSoulMeter()
            }
            // Give player orb with data
            user?.giveItemStack(stack)
            return@ActionFactory
        }
    )
}