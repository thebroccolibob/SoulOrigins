package io.github.thebroccolibob.soulorigins.recipe

import io.github.thebroccolibob.soulorigins.SoulOrigins
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SpecialRecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object SoulOriginsRecipeSerializers {
    private fun <S : RecipeSerializer<T>?, T : Recipe<*>?> register(id: Identifier, serializer: S): S {
        return Registry.register(Registries.RECIPE_SERIALIZER, id, serializer)
    }

    private fun <S : RecipeSerializer<T>?, T : Recipe<*>?> register(path: String, serializer: S): S {
        return Registry.register(Registries.RECIPE_SERIALIZER, SoulOrigins.id(path), serializer)
    }

    val BEE_BOMB = register("bee_bomb", SpecialRecipeSerializer(::BeeBombCraftingRecipe))

    fun register() {}
}
