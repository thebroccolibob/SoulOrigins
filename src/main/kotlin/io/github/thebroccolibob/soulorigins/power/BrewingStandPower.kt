package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.Active
import io.github.apace100.apoli.power.Active.Key
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.calio.data.SerializableData
import io.github.thebroccolibob.soulorigins.PropertyDelegate
import io.github.thebroccolibob.soulorigins.Soulorigins
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
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import java.util.function.BiFunction

class BrewingStandPower(
    type: PowerType<BrewingStandPower>,
    entity: LivingEntity?,
    private var key: Key,
) : Power(type, entity), Active, Inventory {
    private val inventory: DefaultedList<ItemStack> = DefaultedList.ofSize(5, ItemStack.EMPTY)

    private var brewTime: Int = 0
    private var fuel: Int = 0

    private var itemBrewing: Item? = null

    private val propertyDelegate = PropertyDelegate(::brewTime, ::fuel)

    private val containerScreen = object : NamedScreenHandlerFactory {
        override fun createMenu(syncId: Int, playerInventory: PlayerInventory?, player: PlayerEntity?): ScreenHandler {
            return BrewingStandScreenHandler(syncId, playerInventory, this@BrewingStandPower, propertyDelegate)
        }

        override fun getDisplayName(): Text = Text.translatable("container.brewing")
    }

    private val containerSize = inventory.size

    override fun onLost() {
        dropItemsOnLost()
    }

    override fun onUse() {
        if (!isActive) {
            return
        }
        if (!entity.world.isClient && entity is PlayerEntity) {
            (entity as PlayerEntity).openHandledScreen(containerScreen)
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

    override fun size(): Int {
        return containerSize
    }

    override fun isEmpty(): Boolean {
        return inventory.isEmpty()
    }

    override fun getStack(slot: Int): ItemStack {
        return inventory[slot]
    }

    override fun removeStack(slot: Int, amount: Int): ItemStack {
        return inventory[slot].split(amount)
    }

    override fun removeStack(slot: Int): ItemStack {
        val stack = inventory[slot]
        setStack(slot, ItemStack.EMPTY)
        return stack
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        inventory[slot] = stack
    }

    override fun markDirty() {}

    override fun canPlayerUse(player: PlayerEntity): Boolean {
        return player === this.entity
    }

    override fun clear() {
        for (i in 0 until containerSize) {
            setStack(i, ItemStack.EMPTY)
        }
    }

    override fun shouldTick() = true

    override fun tick() {
        tick(this)
    }

    fun dropItemsOnDeath() {
        val playerEntity = entity as PlayerEntity
        for (i in 0 until containerSize) {
            val currentItemStack = getStack(i)

            if (!currentItemStack.isEmpty && EnchantmentHelper.hasVanishingCurse(currentItemStack)) removeStack(i)
            else {
                playerEntity.dropItem(currentItemStack, true, false)
                setStack(i, ItemStack.EMPTY)
            }
        }
    }

    fun dropItemsOnLost() {
        val playerEntity = entity as PlayerEntity
        for (i in 0 until containerSize) {
            val currentItemStack = getStack(i)
            playerEntity.inventory.offerOrDrop(currentItemStack)
        }
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
                Identifier(Soulorigins.MOD_ID, "brewing_stand"),
                SerializableData()
                    .add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, Key())
            ) { data ->
                BiFunction { powerType, livingEntity ->
                    BrewingStandPower(
                        powerType,
                        livingEntity,
                        data.get("key")
                    )
                }
            }.allowCondition()
        }

        fun tick(power: BrewingStandPower) {
            val fuelItem = power.inventory[4]
            if (power.fuel <= 0 && fuelItem.isOf(Items.BLAZE_POWDER)) {
                power.fuel = 20
                fuelItem.decrement(1)
            }

            val canCraft = canCraft(power.inventory)
            val brewing = power.brewTime > 0
            val ingredient = power.inventory[3]

            if (brewing) {
                --power.brewTime
                val finishedBrewing = power.brewTime == 0
                if (finishedBrewing && canCraft) {
                    craft(power.inventory)
                } else if (!canCraft || !ingredient.isOf(power.itemBrewing)) {
                    power.brewTime = 0
                }
            } else if (canCraft && power.fuel > 0) {
                --power.fuel
                power.brewTime = 400
                power.itemBrewing = ingredient.item
            }
        }


        private fun canCraft(slots: DefaultedList<ItemStack>): Boolean {
            val ingredient = slots[3]
            if (ingredient.isEmpty) {
                return false
            } else if (!BrewingRecipeRegistry.isValidIngredient(ingredient)) {
                return false
            } else {
                for (i in 0..2) {
                    val itemStack2 = slots[i]
                    if (!itemStack2.isEmpty && BrewingRecipeRegistry.hasRecipe(itemStack2, ingredient)) {
                        return true
                    }
                }

                return false
            }
        }

        private fun craft(slots: DefaultedList<ItemStack>) {
            var ingredient = slots[3]

            for (i in 0..2) {
                slots[i] = BrewingRecipeRegistry.craft(ingredient, slots[i])
            }

            ingredient.decrement(1)
            if (ingredient.item.hasRecipeRemainder()) {
                val remainder = ItemStack(ingredient.item.recipeRemainder)
                if (ingredient.isEmpty) {
                    ingredient = remainder
                } //else {
//                    ItemScatterer.spawn(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), itemStack2)
//                }
            }

            slots[3] = ingredient
        }

    }
}
