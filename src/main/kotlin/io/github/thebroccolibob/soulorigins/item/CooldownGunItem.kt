package io.github.thebroccolibob.soulorigins.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.SnowballEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Colors
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class CooldownGunItem(private val maxUses: Int, private val duration: Int, settings: Settings) : Item(settings) {
    private val maxDuration = maxUses * duration

    private var currentWorld: World? = null

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)

        if (stack.getCooldown(world) > maxDuration)
            return TypedActionResult.fail(stack)

        if (!world.isClient) {
            val snowballEntity = SnowballEntity(world, user)
            snowballEntity.setItem(stack)
            snowballEntity.setVelocity(user, user.pitch, user.yaw, 0.0f, 1.5f, 1.0f)
            world.spawnEntity(snowballEntity)
        }

        if (currentWorld != world)
            currentWorld = world // yes this is an ugly hack
        val newCooldown = (stack.getCooldown(world) + duration)
        stack.setCooldown(world, newCooldown.coerceAtMost(maxDuration))
        if (newCooldown >= maxDuration) {
            user.itemCooldownManager.set(this, maxDuration)
        }

        return TypedActionResult.success(stack, false)
    }

    override fun getItemBarColor(stack: ItemStack): Int {
        return Colors.RED
    }

    override fun getItemBarStep(stack: ItemStack): Int {
        return currentWorld?.let { world ->
            stack.getCooldown(world).takeIf { it > 0 }?.let {
                12 * it / maxDuration + 1
            }
        } ?: 0
    }

    override fun isItemBarVisible(stack: ItemStack): Boolean {
        return currentWorld?.let { stack.getCooldown(it) > 0 } ?: false
    }

    companion object {
        private const val NBT_FINISH_TIME = "FinishTime"

        private var ItemStack.finishTime
            get() = this.nbt?.getLong(NBT_FINISH_TIME)
            set(value) {
                if (value == null) {
                    this.nbt?.remove(NBT_FINISH_TIME)
                } else {
                    this.orCreateNbt.putLong(NBT_FINISH_TIME, value)
                }
            }

        fun ItemStack.getCooldown(world: World): Int {
            return this.finishTime?.let { it - world.time }?.toInt()?.coerceAtLeast(0) ?: 0
        }

        fun ItemStack.setCooldown(world: World, cooldown: Int) {
            this.finishTime = world.time + cooldown
        }

    }
}
