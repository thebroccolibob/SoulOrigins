package io.github.thebroccolibob.soulorigins.potion

import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.effect.SoulOriginsEffects
import io.github.thebroccolibob.soulorigins.item.SoulOriginsItems
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.potion.Potion
import net.minecraft.potion.Potions
import net.minecraft.recipe.BrewingRecipeRegistry
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object SoulOriginsPotions {
    private fun register(path: String, potion: Potion): Potion {
        return Registry.register(Registries.POTION, SoulOrigins.id(path), potion)
    }

    private fun register(path: String, vararg effects: StatusEffectInstance): Potion {
        return register(path, Potion(*effects))
    }

    private fun register(path: String, baseName: String, vararg effects: StatusEffectInstance): Potion {
        return register(path, Potion(baseName, *effects))
    }

    private fun registerFullSet(name: String, effect: StatusEffect, baseDuration: Int, longDuration: Int, strongAmplify: Int = 1) = FullPotionSet(
        register(name, name, StatusEffectInstance(effect, baseDuration, 0)),
        register("long_$name", name, StatusEffectInstance(effect, longDuration, 0)),
        register("strong_$name", name, StatusEffectInstance(effect, baseDuration, strongAmplify)),
    ).also {
        BrewingRecipeRegistry.registerPotionRecipe(it.base, Items.REDSTONE, it.long)
        BrewingRecipeRegistry.registerPotionRecipe(it.base, Items.GLOWSTONE_DUST, it.strong)
    }

    val PRECISION = registerFullSet("precision", SoulOriginsEffects.PRECISION, 1800, 2400)
    val NECROSIS = registerFullSet("necrosis", SoulOriginsEffects.NECROSIS, 3600, 4800)
    val PERCEPTION = registerFullSet("perception", SoulOriginsEffects.PERCEPTION, 3600, 4800)
    val THRONGLED = registerFullSet("throngled", SoulOriginsEffects.THRONGLED, 4800, 9600)

    object MementoMori {
        val stage0 = register("memento_mori_stage0", "incomplete_memento_mori")
        val stage1 = register("memento_mori_stage1", "incomplete_memento_mori")
        val stage2 = register("memento_mori_stage2", "incomplete_memento_mori")

        val final = register("memento_mori", StatusEffectInstance(SoulOriginsEffects.MEMENTO_MORI, 12000))
        val final_long = register("long_memento_mori", "memento_mori", StatusEffectInstance(SoulOriginsEffects.MEMENTO_MORI, 24000))
    }

    fun register() {
        BrewingRecipeRegistry.registerPotionRecipe(MementoMori.final, Items.REDSTONE, MementoMori.final_long)
    }

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

        PotionRecipe(Potions.AWKWARD, SoulOriginsItems.TAILWIND_SHARD, MementoMori.stage0),
        PotionRecipe(MementoMori.stage0, SoulOriginsItems.UPDRAFT_SHARD, MementoMori.stage1),
        PotionRecipe(MementoMori.stage1, SoulOriginsItems.BURST_SHARD, MementoMori.stage2),
        PotionRecipe(MementoMori.stage2, SoulOriginsItems.MOB_ORB, MementoMori.final),
    )
}
