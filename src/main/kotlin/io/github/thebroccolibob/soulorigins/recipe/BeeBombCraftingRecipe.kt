package io.github.thebroccolibob.soulorigins.recipe

import io.github.thebroccolibob.soulorigins.getList
import io.github.thebroccolibob.soulorigins.item.SoulOriginsItems
import io.github.thebroccolibob.soulorigins.toNbtList
import net.minecraft.block.entity.BeehiveBlockEntity.BEES_KEY
import net.minecraft.inventory.RecipeInputInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.Identifier
import net.minecraft.world.World

class BeeBombCraftingRecipe(id: Identifier, category: CraftingRecipeCategory) : SpecialCraftingRecipe(id, category) {
    override fun matches(inventory: RecipeInputInventory, world: World): Boolean {
        var tnt = false
        var hive = false

        for (stack in inventory.inputStacks) when {
            stack.isOf(Items.TNT) -> tnt = true
            !stack.isOf(SoulOriginsItems.BEE_BOMB) && stack.nbt?.getCompound("BlockEntityTag")?.contains(BEES_KEY) == true -> hive = true
            stack.isEmpty -> {}
            else -> return false
        }

        return tnt && hive
    }

    override fun craft(inventory: RecipeInputInventory, registryManager: DynamicRegistryManager): ItemStack {
        val hive = inventory.inputStacks.first { it.nbt?.getCompound("BlockEntityTag")?.contains(BEES_KEY) == true }

        return SoulOriginsItems.BEE_BOMB.defaultStack.apply {
            hive.nbt?.let { nbt ->
                setSubNbt("BlockEntityTag", nbt
                    .getList("Bees", NbtCompound.COMPOUND_TYPE)
                    .mapNotNull { (it as? NbtCompound)?.getCompound("EntityTag") }
                    .toNbtList()
                )
            }
        }
    }

    override fun fits(width: Int, height: Int) = width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> = SoulOriginsRecipeSerializers.BEE_BOMB
}
