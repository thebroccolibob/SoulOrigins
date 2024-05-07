package io.github.thebroccolibob.soulorigins.item

import net.minecraft.advancement.criterion.Criteria
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class WindCrystalItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)

        if (world is ServerWorld)
            user.incrementStat(Stats.USED.getOrCreateStat(stack.item))
        if (user is ServerPlayerEntity)
            Criteria.CONSUME_ITEM.trigger(user, stack)

        stack.decrement(1)

        // Add some fancy effects here

        return TypedActionResult.consume(stack)
    }

    override fun hasGlint(stack: ItemStack) = true
}
