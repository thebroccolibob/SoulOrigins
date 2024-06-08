package io.github.thebroccolibob.soulorigins.item

import io.github.thebroccolibob.soulorigins.entity.SurfaceBuilderProjectileEntity
import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

open class SurfaceBuilderProjectileItem(val block: Block, val offsetY: Int, settings: Settings) : Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand?): TypedActionResult<ItemStack> {
        val itemStack = user.getStackInHand(hand)

        world.playSound(
            null,
            user.x,
            user.y,
            user.z,
            SoundEvents.ENTITY_SNOWBALL_THROW,
            SoundCategory.NEUTRAL,
            0.5f,
            0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
        )
        if (!world.isClient) {
            val projectile = SurfaceBuilderProjectileEntity(world, user)
            projectile.setItem(itemStack)
            projectile.blockToPlace = getBlock(user).defaultState
            projectile.offsetY = offsetY
            projectile.setVelocity(user, user.pitch, user.yaw, 0.0f, 1.5f, 1.0f)
            world.spawnEntity(projectile)
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this))
        if (!user.abilities.creativeMode) {
            itemStack.decrement(1)
        }

        return TypedActionResult.success(itemStack, world.isClient())
    }

    protected open fun getBlock(user: PlayerEntity) = block

}
