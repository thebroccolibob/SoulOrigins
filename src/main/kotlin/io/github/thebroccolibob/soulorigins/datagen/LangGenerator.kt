package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.datagen.power.LeveledCooldownEntry
import io.github.thebroccolibob.soulorigins.datagen.power.wind.tailwindEntries
import io.github.thebroccolibob.soulorigins.datagen.power.wind.updraftEntries
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import io.github.thebroccolibob.soulorigins.plus
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
        for (entry in entries) {
            val level = entry.level
            add("advancements.$modId.wind.$id.lvl$level", "$name ${romanNumerals[level]}")
            add("advancements.$modId.wind.$id.lvl$level.description", (description(level)?.let {desc->"$desc\n"} ?: "") + "${entry.charges} charge${if (entry.charges == 1) "" else "s"}\n${entry.recharge / 20}s")
        }
    }

    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        with(translationBuilder) {
            add(SouloriginsItems.MARIGOLD_CARD, "Marigold Card")
            add("${SouloriginsItems.MARIGOLD_CARD.translationKey}.empty", "Empty")
            add("${SouloriginsItems.MARIGOLD_CARD.translationKey}.multiple_items", "%s x%s")
            add("container.$modId.inventory.deck", "Deck")

            add("advancements.$modId.wind.root", "Wind Spirit")
            add("advancements.$modId.wind.root.description", "Level up your skills by finding wind crystals!")
            cooldownDescriptions("updraft", "Updraft", updraftEntries, updraftDescription)
            cooldownDescriptions("tailwind", "Tailwind", tailwindEntries, tailwindDescription)

            for (lvl in 1..3) {
                translationBuilder.add("advancements.$modId.wind.burst.lvl$lvl", "Burst ${romanNumerals[lvl]}")
            }

            add("advancements.$modId.wind.burst.lvl1.description", "Unlocks burst\nSpeed & invis 30s\n100% Soul")
            add("advancements.$modId.wind.burst.lvl2.description", "Adds proj prot 30s\nRecharges updraft & tailwind")
            add("advancements.$modId.wind.burst.lvl3.description", "Increases to 1min")

            add("itemGroup.soul-origins.item_group", "Soul Origins")

            add(SouloriginsItems.BURST_SHARD, "Burst Shard")
            add(SouloriginsItems.BURST_SHARD + "flavor", "A shard resonating with an eruptive power")
            add(SouloriginsItems.UPDRAFT_SHARD, "Updraft Shard")
            add(SouloriginsItems.UPDRAFT_SHARD + "flavor", "A shard resonating with an uplifting power")
            add(SouloriginsItems.TAILWIND_SHARD, "Tailwind Shard")
            add(SouloriginsItems.TAILWIND_SHARD + "flavor", "A shard resonating with a turbulent power")
            add("item.soul-origins.wind_shard.origin_exclusive", "Only usable by wind spirits")

            add(SouloriginsItems.ARTIFICER_PLATFORM_BUILDER, "Brass Platform Builder")
            add(SouloriginsItems.ARTIFICER_WALL_BUILDER, "Brass Wall Builder")
            add(SouloriginsItems.ARTIFICER_COLUMN_BUILDER, "Brass Column Builder")

            add(SouloriginsItems.MOB_ORB, "Mob Orb")
        }
    }
}
