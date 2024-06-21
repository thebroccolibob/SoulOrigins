package io.github.thebroccolibob.soulorigins.action

import archives.tater.neutron.NeutronState
import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.CooldownPower
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataType
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.SerializableData
import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.getPower
import io.github.thebroccolibob.soulorigins.power.EntityStorePower
import io.github.thebroccolibob.soulorigins.syncPower
import net.minecraft.block.BlockState
import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MovementType
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.PotionItem
import net.minecraft.nbt.NbtElement
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.potion.PotionUtil
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.FluidTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import java.util.function.BiConsumer

private fun register(actionFactory: ActionFactory<Entity>) {
    Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.serializerId, actionFactory)
}

private fun register(id: String, data: SerializableData = SerializableData(), effect: BiConsumer<SerializableData.Instance, Entity>) {
    register(ActionFactory(SoulOrigins.id(id), data, effect))
}

private inline fun register(id: String, crossinline effect: (Entity) -> Unit) {
    register(id) { _, entity -> effect(entity) }
}

fun registerSoulOriginsEntityActions() {
    register("change_cooldown", SerializableData {
        add("cooldown", ApoliDataTypes.POWER_TYPE)
        add("change", SerializableDataTypes.INT)
    }) { data, entity ->
        if (entity is LivingEntity) {
            val powerType = data.get<PowerType<*>>("cooldown")
            (entity.getPower(powerType) as? CooldownPower)?.run {
                val change = data.getInt("change")
                setCooldown((cooldownDuration - remainingTicks) - change)
                entity.syncPower(powerType)
            }
        }
    }

    register("despawn") {
        it.discard()
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

        val state = serverWorld.getBlockState(entity.blockPos).let {
            @Suppress("DEPRECATION")
            if (it.isAir || it.isLiquid) serverWorld.getBlockState(entity.blockPos.down()) else it
        }

        serverWorld.spawnParticles(
            BlockStateParticleEffect(ParticleTypes.BLOCK, state),
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

    register("apugli_raycast", FixedRaycastAction.serializableData, FixedRaycastAction::execute)

    register(EntityStorePower.storeAction)
    register(EntityStorePower.clearAction)

    register("effect_cloud_from_item") { entity ->
        if (entity.world.isClient || entity !is LivingEntity) return@register

        val potionCheck = { stack: ItemStack ->
            if (stack.item is PotionItem) stack else null
        }

        val stack = entity.getStackInHand(Hand.MAIN_HAND).let(potionCheck) ?: entity.getStackInHand(Hand.OFF_HAND).let(potionCheck) ?: return@register

        AreaEffectCloudEntity(entity.world, entity.x, entity.y, entity.z).apply {
            owner = entity
            radius = 3.0f
            radiusOnUse = -0.5f
            waitTime = 10
            radiusGrowth = -radius / duration.toFloat()
            potion = PotionUtil.getPotion(stack)

            for (statusEffectInstance in PotionUtil.getCustomPotionEffects(stack)) {
                addEffect(StatusEffectInstance(statusEffectInstance))
            }

            stack.nbt?.run {
                if (contains("CustomPotionColor", NbtElement.NUMBER_TYPE.toInt())) {
                    color = getInt("CustomPotionColor")
                }
            }
        }.let(entity.world::spawnEntity)
    }

    register(SpawnEntityRaycastAction.factory)

    register("use_riptide", SerializableData {
        add("duration", SerializableDataTypes.INT, 20)
    }) { data, entity ->
        (entity as? PlayerEntity)?.useRiptide(data.getInt("duration"))
    }

    register("move", SerializableData {
        add("move", SerializableDataTypes.VECTOR)
        add("movement_type", SerializableDataType.enumValue(MovementType::class.java), MovementType.SELF)
    }) { data, entity ->
        entity.move(data.get("movement_type"), data.get("move"))
    }

    register("set_neutron", SerializableData {
        add("enabled", SerializableDataTypes.BOOLEAN, true)
    }) { data, entity ->
        entity.server?.let(NeutronState.Companion::get)?.run {
            if (data.getBoolean("enabled")) {
                enabledPlayers.add(entity.uuid)
            } else {
                enabledPlayers.remove(entity.uuid)
            }
        }
    }

    @Suppress("DEPRECATION")
    register("random_teleport", SerializableData {
        add("action_on_success", ApoliDataTypes.ENTITY_ACTION, null)
        add("attempts", SerializableDataTypes.INT, 16)
    }) { data, entity ->
        if (entity.world.isClient || entity !is LivingEntity) return@register

        val mutable = BlockPos.Mutable(0, 0, 0)

        for (i in 1..data.getInt("attempts")) {
            mutable.x = entity.blockX + (entity.world.random.nextInt(64) - 32)
            mutable.y = entity.blockY + (entity.world.random.nextInt(64) - 32)
            mutable.z = entity.blockZ + (entity.world.random.nextInt(64) - 32)

            while (mutable.y > entity.world.bottomY && !entity.world.getBlockState(mutable).blocksMovement()) {
                mutable.move(Direction.DOWN)
            }

            val blockState: BlockState = entity.world.getBlockState(mutable)
            val bl = blockState.blocksMovement()
            val bl2 = blockState.fluidState.isIn(FluidTags.WATER)

            if (bl && !bl2) {
                if (entity.teleport(mutable.x + 0.5, mutable.y + 1.0, mutable.z + 0.5, false)) {
                    data.get<ActionFactory<Entity>.Instance?>("action_on_success")?.accept(entity)
                }
                break
            }
        }
    }
}
