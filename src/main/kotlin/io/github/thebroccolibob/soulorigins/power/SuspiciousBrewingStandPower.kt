package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.Active.Key
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.IngredientSlotProvider
import io.github.thebroccolibob.soulorigins.SerializableData
import io.github.thebroccolibob.soulorigins.Soulorigins
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import io.github.thebroccolibob.soulorigins.potion.SoulOriginsPotions
import io.github.thebroccolibob.soulorigins.power.SuspiciousBrewingStandPower.IngredientSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.potion.PotionUtil
import net.minecraft.recipe.BrewingRecipeRegistry
import net.minecraft.screen.BrewingStandScreenHandler
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.util.Identifier
import java.util.function.BiFunction

class SuspiciousBrewingStandPower(
    type: PowerType<BrewingStandPower>,
    entity: LivingEntity?,
    key: Key,
    fuelPowerType: PowerType<*>?,
    dropOnDeath: Boolean,
    recoverable: Boolean
) : BrewingStandPower(type, entity, key, fuelPowerType, dropOnDeath, recoverable) {

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory?, player: PlayerEntity?): ScreenHandler {
        return object : BrewingStandScreenHandler(syncId, playerInventory, this, propertyDelegate), IngredientSlotProvider {
            override fun replaceIngredientSlot(original: Slot): Slot =
                IngredientSlot(original.inventory, original.index, original.x, original.y)
        }
    }

    override fun isValidIngredient(stack: ItemStack): Boolean {
        return Companion.isValidIngredient(stack)
    }

    override fun hasRecipe(input: ItemStack, ingredient: ItemStack): Boolean {
        val potion = PotionUtil.getPotion(input)

        return SoulOriginsPotions.SUSPICIOUS_RECIPES.any { (recipeInput, recipeIngredient) ->
            recipeInput == potion && ingredient.isOf(recipeIngredient)
        } || super.hasRecipe(input, ingredient)
    }

    override fun craft(ingredient: ItemStack, input: ItemStack): ItemStack {
        val potion = PotionUtil.getPotion(input)

        return toSuspicious(SoulOriginsPotions.SUSPICIOUS_RECIPES.find { (recipeInput, recipeIngredient) ->
            recipeInput == potion && ingredient.isOf(recipeIngredient)
        }?.let {
            PotionUtil.setPotion(input, it.output)
        } ?: super.craft(ingredient, input))
    }

    private fun toSuspicious(stack: ItemStack): ItemStack {
        if (stack.isEmpty || PotionUtil.getPotion(stack).effects.isEmpty()) return stack

        val suspiciousItem = when(stack.item) {
            Items.POTION -> SouloriginsItems.SUSPICIOUS_BREW
            Items.SPLASH_POTION -> SouloriginsItems.SPLASH_SUSPICIOUS_BREW
            Items.LINGERING_POTION -> SouloriginsItems.LINGERING_SUSPICIOUS_BREW
            else -> return stack
        }

        return suspiciousItem.defaultStack.apply {
            PotionUtil.setPotion(this, PotionUtil.getPotion(stack))
        }
    }

    companion object {
        fun createFactory(): PowerFactory<BrewingStandPower> {
            return PowerFactory<BrewingStandPower>(
                Identifier(Soulorigins.MOD_ID, "suspicious_brewing_stand"),
                SerializableData {
                    add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, Key())
                    add("fuel_resource", ApoliDataTypes.POWER_TYPE, null)
                    add("drop_on_death", SerializableDataTypes.BOOLEAN, true)
                    add("recoverable", SerializableDataTypes.BOOLEAN, true)
                }
            ) { data ->
                BiFunction { powerType, livingEntity ->
                    SuspiciousBrewingStandPower(
                        powerType,
                        livingEntity,
                        data.get("key"),
                        data.get("fuel_resource"),
                        data.get("drop_on_death"),
                        data.get("recoverable")
                    )
                }
            }.allowCondition()
        }

        fun isValidIngredient(stack: ItemStack): Boolean {
            return SoulOriginsPotions.SUSPICIOUS_RECIPES.any { stack.isOf(it.ingredient) } || BrewingRecipeRegistry.isValidIngredient(stack)
        }

    }

    class IngredientSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
        override fun canInsert(stack: ItemStack): Boolean {
            return Companion.isValidIngredient(stack)
        }

        override fun getMaxItemCount(): Int = 64
    }

}

