package io.github.thebroccolibob.soulorigins.item

import io.github.thebroccolibob.soulorigins.FabricItemSettings
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity

object SouloriginsItems {
    private fun register(id: String, item: Item): Item = Registry.register(Registries.ITEM, Identifier(Soulorigins.MOD_ID, id), item)

    private fun registerCrystal(type: String) = register("wind_crystal_$type", WindCrystalItem(FabricItemSettings {
        maxCount(1)
        rarity(Rarity.RARE)
    }))

    val MARIGOLD_CARD = register("marigold_card", MarigoldCardItem(FabricItemSettings {
        maxCount(1)
        rarity(Rarity.UNCOMMON)
    }))

    val UPDRAFT_CRYSTAL = registerCrystal("updraft")
    val TAILWIND_CRYSTAL = registerCrystal("tailwind")
    val BURST_CRYSTAL = registerCrystal("burst")
    val NEUTRAL_CRYSTAL = registerCrystal("neutral")

    fun register() {}
}
