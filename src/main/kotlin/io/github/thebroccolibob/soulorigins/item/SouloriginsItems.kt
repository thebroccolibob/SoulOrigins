package io.github.thebroccolibob.soulorigins.item

import io.github.thebroccolibob.soulorigins.FabricItemSettings
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity

object SouloriginsItems {
    private fun register(id: String, item: Item) = Registry.register(Registries.ITEM, Identifier(Soulorigins.MOD_ID, id), item)

    val MARIGOLD_CARD: Item = register("marigold_card", MarigoldCardItem(FabricItemSettings {
        maxCount(1)
        rarity(Rarity.UNCOMMON)
    }))

    fun register() {}
}
