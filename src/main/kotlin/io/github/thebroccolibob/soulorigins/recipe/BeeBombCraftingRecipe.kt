package io.github.thebroccolibob.soulorigins.recipe

import io.github.thebroccolibob.soulorigins.SoulOriginsTags
import io.github.thebroccolibob.soulorigins.getList
import io.github.thebroccolibob.soulorigins.item.SoulOriginsItems
import io.github.thebroccolibob.soulorigins.toNbtList
import net.minecraft.block.entity.BeehiveBlockEntity.BEES_KEY
import net.minecraft.inventory.RecipeInputInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
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
            isHive(stack) -> hive = true
            stack.isEmpty -> {}
            else -> return false
        }

        return tnt && hive
    }

    private fun isHive(stack: ItemStack) =
        !stack.isOf(SoulOriginsItems.BEE_BOMB) && (stack.isIn(SoulOriginsTags.BEE_BOMB_INGREDIENT)) || stack.nbt?.getCompound(
            "BlockEntityTag"
        )?.contains(BEES_KEY) == true

    override fun craft(inventory: RecipeInputInventory, registryManager: DynamicRegistryManager): ItemStack {
        val hive = inventory.inputStacks.firstOrNull(::isHive) ?: return ItemStack.EMPTY

        return SoulOriginsItems.BEE_BOMB.defaultStack.apply {
            setSubNbt("BlockEntityTag", NbtCompound().apply {
                put("Bees",
                    hive.nbt
                        ?.getCompound("BlockEntityTag")
                        ?.getList("Bees", NbtCompound.COMPOUND_TYPE)
                        ?.mapNotNull { (it as? NbtCompound)?.getCompound("EntityTag") }
                        ?.toNbtList()
                    ?: NbtList()
                )
            })
        }
    }

    override fun fits(width: Int, height: Int) = width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> = SoulOriginsRecipeSerializers.BEE_BOMB
}
