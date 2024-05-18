package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.datagen.power.LeveledCooldownEntry
import io.github.thebroccolibob.soulorigins.datagen.power.wind.tailwindEntries
import io.github.thebroccolibob.soulorigins.datagen.power.wind.updraftEntries
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

class LangGenerator(dataOutput: FabricDataOutput) : FabricLanguageProvider(dataOutput) {
    private val modId = dataOutput.modId

    private val romanNumerals = mapOf(
        1 to "I",
        2 to "II",
        3 to "III",
        4 to "IV",
        5 to "V"
    )

    private val updraftDescription = fun(level: Int) = when(level) {
        1 -> "Unlocks jumping"
        2 -> "Increases height"
        3 -> null
        4 -> "Increases height"
        5 -> "Increases height"
        else -> null
    }

    private val tailwindDescription = fun(level: Int) = when(level) {
        1 -> "Unlocks dashing"
        2 -> "Increases speed"
        3 -> null
        4 -> "Increases speed"
        5 -> "Increases speed"
        else -> null
    }

    private fun TranslationBuilder.cooldownDescriptions(id: String, name: String, entries: Iterable<LeveledCooldownEntry>, description: (Int) -> String?) {
        entries.forEach {
            val level = it.level
            add("advancements.$modId.wind.$id.lvl$level", "$name ${romanNumerals[level]}")
            add("advancements.$modId.wind.$id.lvl$level.description", (description(level)?.let {desc->"$desc\n"} ?: "") + "${it.charges} charge${if (it.charges == 1) "" else "s"}\n${it.recharge / 20}s")
        }
    }

    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        translationBuilder.add(SouloriginsItems.MARIGOLD_CARD, "Marigold Card")
        translationBuilder.add("${SouloriginsItems.MARIGOLD_CARD.translationKey}.empty", "Empty")
        translationBuilder.add("${SouloriginsItems.MARIGOLD_CARD.translationKey}.multiple_items", "%s x%s")
        translationBuilder.add("container.$modId.inventory.deck", "Deck")

        translationBuilder.add("advancements.$modId.wind.root", "Wind Spirit")
        translationBuilder.add("advancements.$modId.wind.root.description", "Level up your skills by finding wind crystals!")
        translationBuilder.cooldownDescriptions("updraft", "Updraft", updraftEntries, updraftDescription)
        translationBuilder.cooldownDescriptions("tailwind", "Tailwind", tailwindEntries, tailwindDescription)
        (1..3).forEach {
            translationBuilder.add("advancements.$modId.wind.burst.lvl$it", "Burst ${romanNumerals[it]}")
        }
        translationBuilder.add("advancements.$modId.wind.burst.lvl1.description", "Unlocks burst\nSpeed & invis 30s\n100% Soul")
        translationBuilder.add("advancements.$modId.wind.burst.lvl2.description", "Adds proj prot 30s\nRecharges updraft & tailwind")
        translationBuilder.add("advancements.$modId.wind.burst.lvl3.description", "Increases to 1min")

        translationBuilder.add(SouloriginsItems.BURST_CRYSTAL, "Burst Wind Crystal")
        translationBuilder.add(SouloriginsItems.UPDRAFT_CRYSTAL, "Updraft Wind Crystal")
        translationBuilder.add(SouloriginsItems.TAILWIND_CRYSTAL, "Tailwind Wind Crystal")
    }
}
