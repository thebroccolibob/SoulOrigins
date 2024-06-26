package io.github.thebroccolibob.soulorigins.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.client.item.TooltipData
import net.minecraft.item.ItemStack
import net.minecraft.item.LingeringPotionItem
import net.minecraft.item.PotionItem
import net.minecraft.item.SplashPotionItem
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World
import java.util.*

interface SuspiciousBrew {
    fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        tooltip.add(Companion.tooltip)
    }

    companion object {
        private val tooltip = Text.translatable("item.soul-origins.suspicious_brew.tooltip").formatted(Formatting.GRAY)
    }
}

class SuspiciousBrewItem(settings: Settings) : PotionItem(settings), SuspiciousBrew {
    override fun getTranslationKey(stack: ItemStack): String {
        return this.translationKey
    }

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        super<SuspiciousBrew>.appendTooltip(stack, world, tooltip, context)
    }

    override fun getTooltipData(stack: ItemStack?): Optional<TooltipData> = Optional.empty()
}

class SplashSuspiciousBrewItem(settings: Settings) : SplashPotionItem(settings), SuspiciousBrew {
    override fun getTranslationKey(stack: ItemStack): String {
        return this.translationKey
    }

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        super<SuspiciousBrew>.appendTooltip(stack, world, tooltip, context)
    }

    override fun getTooltipData(stack: ItemStack?): Optional<TooltipData> = Optional.empty()
}

class LingeringSuspiciousBrewItem(settings: Settings) : LingeringPotionItem(settings), SuspiciousBrew {
    override fun getTranslationKey(stack: ItemStack): String {
        return this.translationKey
    }

    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        super<SuspiciousBrew>.appendTooltip(stack, world, tooltip, context)
    }

    override fun getTooltipData(stack: ItemStack?): Optional<TooltipData> = Optional.empty()
}
