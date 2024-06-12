package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.Active
import io.github.apace100.apoli.power.Active.Key
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.ResourcePower
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.*
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.recipe.BrewingRecipeRegistry
import net.minecraft.screen.BrewingStandScreenHandler
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.WorldEvents
import java.util.function.BiFunction

open class BrewingStandPower(
    type: PowerType<BrewingStandPower>,
    entity: LivingEntity?,
    private var key: Key,
    private val fuelPowerType: PowerType<*>?,
    private val dropOnDeath: Boolean,
    private val recoverable: Boolean,
    private val inventory: DefaultedList<ItemStack> = DefaultedList.ofSize(5, ItemStack.EMPTY)
) : Power(type, entity), Active, Inventory by inventory.toInventory(), NamedScreenHandlerFactory {
    init {
        this.setTicking(false)
    }

    private var brewTime = 0
    private var fuel = 0

    private var itemBrewing: Item? = null

    protected val propertyDelegate = PropertyDelegate(::brewTime, ::fuel)

    private val containerSize = inventory.size

    private val fuelPower = if (entity == null || fuelPowerType == null) null else
        PowerHolderComponent.KEY.get(entity).getPower(fuelPowerType) as? ResourcePower

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory?, player: PlayerEntity?): ScreenHandler {
        return BrewingStandScreenHandler(syncId, playerInventory, this@BrewingStandPower, propertyDelegate)
    }

    override fun getDisplayName(): Text = Text.translatable("container.brewing")

    override fun onLost() {
        if (!recoverable) return

        (entity as? PlayerEntity)?.let {
            inventory.forEach(it.inventory::offerOrDrop)
        }
    }

    /**
     * Called by [PlayerEntityMixin][io.github.thebroccolibob.soulorigins.mixin.PlayerEntityMixin]
     */
    fun onDeath() {
        if (!dropOnDeath) return

        for (i in 0 until containerSize) {
            val currentItemStack = getStack(i)

            if (!currentItemStack.isEmpty && EnchantmentHelper.hasVanishingCurse(currentItemStack)) removeStack(i)
            else {
                (entity as PlayerEntity).dropItem(currentItemStack, true, false)
                setStack(i, ItemStack.EMPTY)
            }
        }

        brewTime = 0
        fuel = 0
    }

    override fun onUse() {
        if (!isActive) {
            return
        }
        if (!entity.world.isClient && entity is PlayerEntity) {
            (entity as PlayerEntity).openHandledScreen(this)
        }
    }

    override fun toTag(): NbtCompound {
        return NbtCompound().apply {
            put("Inventory", NbtCompound().apply {
                Inventories.writeNbt(this, inventory)
            })
            putInt("BrewTime", brewTime)
            putInt("Fuel", fuel)
        }
    }

    override fun fromTag(tag: NbtElement) {
        tag as NbtCompound
        Inventories.readNbt(tag.getCompound("Inventory"), inventory)
        brewTime = tag.getInt("BrewTime")
        fuel = tag.getInt("Fuel")
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean {
        return player === this.entity
    }

    override fun tick() {
        inventory[4].let {
            if (fuel <= 0 && it.isOf(Items.BLAZE_POWDER)) {
                fuel = 20
                it.decrement(1)
            }
        }

        val canCraft = canCraft()
        val brewing = brewTime > 0
        val ingredient = inventory[3]

        if (brewing) {
            --brewTime
            val finishedBrewing = brewTime == 0
            if (finishedBrewing && canCraft) {
                craft()
            } else if (!canCraft || !ingredient.isOf(itemBrewing)) {
                brewTime = 0
            }
        } else if (canCraft && hasFuel()) {
            consumeFuel()
            brewTime = 400
            itemBrewing = ingredient.item
        }
    }

    fun canCraft(): Boolean {
        val ingredient = inventory[3]

        if (ingredient.isEmpty || !isValidIngredient(ingredient)) {
            return false
        }

        return inventory[0..2].any {
            !it.isEmpty && hasRecipe(it, ingredient)
        }
    }

    protected open fun isValidIngredient(stack: ItemStack): Boolean {
        return BrewingRecipeRegistry.isValidIngredient(stack)
    }

    protected open fun hasRecipe(input: ItemStack, ingredient: ItemStack): Boolean {
        return BrewingRecipeRegistry.hasRecipe(input, ingredient)
    }

    private fun craft() {
        val ingredient = inventory[3]

        for (i in 0..2) {
            inventory[i] = craft(ingredient, inventory[i])
        }

        val ingredientItem = ingredient.item
        ingredient.decrement(1)
        if (ingredientItem.hasRecipeRemainder()) {
            val remainder = ItemStack(ingredientItem.recipeRemainder)
            if (ingredient.isEmpty) {
                inventory[3] = remainder
            } else {
                if ((entity as? PlayerEntity)?.giveItemStack(remainder) != true) {
                    entity.dropStack(remainder)
                }
            }
        }
        entity.world.syncWorldEvent(WorldEvents.BREWING_STAND_BREWS, entity.blockPos, 0)
    }

    protected open fun craft(ingredient: ItemStack, input: ItemStack): ItemStack {
        return BrewingRecipeRegistry.craft(ingredient, input)
    }

    private fun hasFuel() = fuel > 0 || (fuelPower?.value ?: 0) > 0

    private fun consumeFuel() {
        if (fuel > 0)
            fuel--
        else
            fuelPower?.decrement()
        PowerHolderComponent.syncPower(entity, fuelPowerType)
    }

    override fun getKey(): Key {
        return key
    }

    override fun setKey(key: Key) {
        this.key = key
    }

    companion object {
        fun createFactory(): PowerFactory<BrewingStandPower> {
            return PowerFactory<BrewingStandPower>(
                SoulOrigins.id("brewing_stand"),
                SerializableData {
                    add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, Key())
                    add("fuel_resource", ApoliDataTypes.POWER_TYPE, null)
                    add("drop_on_death", SerializableDataTypes.BOOLEAN, true)
                    add("recoverable", SerializableDataTypes.BOOLEAN, true)
                }
            ) { data ->
                BiFunction { powerType, livingEntity ->
                    BrewingStandPower(
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
    }
}
