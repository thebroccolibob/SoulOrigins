package io.github.thebroccolibob.soulorigins.potion

import io.github.thebroccolibob.soulorigins.Soulorigins
import io.github.thebroccolibob.soulorigins.effect.SoulOriginsEffects
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.potion.Potion
import net.minecraft.potion.Potions
import net.minecraft.recipe.BrewingRecipeRegistry
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object SoulOriginsPotions {
    private fun register(path: String, potion: Potion): Potion {
        return Registry.register(Registries.POTION, Identifier(Soulorigins.MOD_ID, path), potion)
    }

    private fun register(path: String, vararg effects: StatusEffectInstance): Potion {
        return register(path, Potion(*effects))
    }

    private fun registerSet(name: String, effect: StatusEffect, baseDuration: Int, longDuration: Int) = PotionSet(
        register(name, StatusEffectInstance(effect, baseDuration, 0)),
        register("long_$name", StatusEffectInstance(effect, longDuration, 0)),
        register("strong_$name", StatusEffectInstance(effect, baseDuration, 1)),
    ).also {
        BrewingRecipeRegistry.registerPotionRecipe(it.base, Items.REDSTONE, it.long)
        BrewingRecipeRegistry.registerPotionRecipe(it.base, Items.GLOWSTONE_DUST, it.strong)
    }

    val PRECISION = registerSet("precision", SoulOriginsEffects.PRECISION, 1800, 2400)
    val NECROSIS = registerSet("necrosis", SoulOriginsEffects.NECROSIS, 3600, 4800)
    val PERCEPTION = registerSet("perception", SoulOriginsEffects.PERCEPTION, 3600, 4800)
    val THRONGLED = registerSet("throngled", SoulOriginsEffects.THRONGLED, 4800, 9600)

    fun register() {}

    data class PotionRecipe(val input: Potion, val ingredient: Item, val output: Potion)

    val SUSPICIOUS_RECIPES = listOf(
        PotionRecipe(Potions.STRENGTH, Items.RABBIT_FOOT, PRECISION.base),
        PotionRecipe(Potions.LONG_STRENGTH, Items.RABBIT_FOOT, PRECISION.long),
        PotionRecipe(Potions.STRONG_STRENGTH, Items.RABBIT_FOOT, PRECISION.strong),

        PotionRecipe(Potions.NIGHT_VISION, Items.SPIDER_EYE, PERCEPTION.base),
        PotionRecipe(Potions.LONG_NIGHT_VISION, Items.SPIDER_EYE, PERCEPTION.long),

        PotionRecipe(Potions.WEAKNESS, Items.BLAZE_POWDER, NECROSIS.base),
        PotionRecipe(Potions.LONG_WEAKNESS, Items.BLAZE_POWDER, NECROSIS.long),

        PotionRecipe(Potions.WATER, Items.GLASS_BOTTLE, THRONGLED.base),
    )

    data class PotionSet(val base: Potion, val long: Potion, val strong: Potion)
}
