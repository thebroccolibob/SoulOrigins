package io.github.thebroccolibob.soulorigins.item

import io.github.thebroccolibob.soulorigins.SoulOriginsParticles
import io.github.thebroccolibob.soulorigins.SoulOriginsSounds
import io.github.thebroccolibob.soulorigins.hasPower
import io.github.thebroccolibob.soulorigins.plus
import io.github.thebroccolibob.soulorigins.power.UseWindShardsPower
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
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

        world.addParticle(SoulOriginsParticles.GUST_EMITTER_SMALL, user.x, user.y + 0.5, user.z, 0.0, 0.0, 0.0)
        (world as? ServerWorld)?.spawnParticles(ParticleTypes.HAPPY_VILLAGER, user.x, user.y + 0.5, user.z, 12, 0.75, 0.75, 0.75, 0.0)
        world.playSound(null, user.blockPos, SoulOriginsSounds.WIND_BURST, SoundCategory.PLAYERS, 2.0f, 0.8f)
        world.playSound(null, user.blockPos, SoulOriginsSounds.WIND_BURST_LARGE, SoundCategory.PLAYERS, 1.0f, 1.0f)
        world.playSound(null, user.blockPos, SoulOriginsSounds.WIND_LEVELUP, SoundCategory.PLAYERS)

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
