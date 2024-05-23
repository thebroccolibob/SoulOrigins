package io.github.thebroccolibob.soulorigins.item

import io.github.thebroccolibob.soulorigins.hasPower
import io.github.thebroccolibob.soulorigins.plus
import io.github.thebroccolibob.soulorigins.power.UseWindShardsPower
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.stat.Stats
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class WindShardItem(tooltipColor: Formatting, settings: Settings) : Item(settings) {

    private val flavorTooltip by lazy {
        Text.translatable(this + "flavor").formatted(tooltipColor, Formatting.ITALIC)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)

        if (!user.hasPower<UseWindShardsPower>()) return TypedActionResult.fail(stack)

        if (world is ServerWorld)
            user.incrementStat(Stats.USED.getOrCreateStat(stack.item))
        if (user is ServerPlayerEntity)
            Criteria.CONSUME_ITEM.trigger(user, stack)

        stack.decrement(1)

        // TODO Add some fancy effects here

        return TypedActionResult.success(stack)
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        tooltip.add(flavorTooltip)
        tooltip.add(Text.translatable("item.soul-origins.wind_shard.origin_exclusive").formatted(Formatting.GRAY, Formatting.ITALIC))
    }
}
