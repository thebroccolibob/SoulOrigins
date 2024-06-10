package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.datagen.power.LeveledCooldownEntry
import io.github.thebroccolibob.soulorigins.datagen.power.wind.tailwindEntries
import io.github.thebroccolibob.soulorigins.datagen.power.wind.updraftEntries
import io.github.thebroccolibob.soulorigins.effect.SoulOriginsEffects
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import io.github.thebroccolibob.soulorigins.plus
import io.github.thebroccolibob.soulorigins.potion.SoulOriginsPotions
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.item.Items
import net.minecraft.potion.Potion

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

    private fun TranslationBuilder.addPotionVariants(potion: Potion, name: String) {
        for ((item, translation) in listOf(
            Items.POTION to "Potion of ",
            Items.SPLASH_POTION to "Splash Potion of ",
            Items.LINGERING_POTION to "Lingering Potion of ",
            Items.TIPPED_ARROW to "Arrow of ",
        )) {
            add(potion.finishTranslationKey("${item.translationKey}.effect."), "$translation$name")
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

        translationBuilder.add(SouloriginsItems.BURST_SHARD, "Burst Shard")
        translationBuilder.add(SouloriginsItems.BURST_SHARD + "flavor", "A shard resonating with an eruptive power")
        translationBuilder.add(SouloriginsItems.UPDRAFT_SHARD, "Updraft Shard")
        translationBuilder.add(SouloriginsItems.UPDRAFT_SHARD + "flavor", "A shard resonating with an uplifting power")
        translationBuilder.add(SouloriginsItems.TAILWIND_SHARD, "Tailwind Shard")
        translationBuilder.add(SouloriginsItems.TAILWIND_SHARD + "flavor", "A shard resonating with a turbulent power")
        translationBuilder.add("item.soul-origins.wind_shard.origin_exclusive", "Only usable by wind spirits")

        translationBuilder.add("container.soul-origins.suspicious_brewing", "Suspicious Brewing")

        translationBuilder.add(SouloriginsItems.SUSPICIOUS_BREW, "Suspicious Brew")
        translationBuilder.add(SouloriginsItems.SPLASH_SUSPICIOUS_BREW, "Splash Suspicious Brew")
        translationBuilder.add(SouloriginsItems.LINGERING_SUSPICIOUS_BREW, "Lingering Suspicious Brew")
        translationBuilder.add("item.soul-origins.suspicious_brew.tooltip", "Effects Unknown")

        translationBuilder.add(SoulOriginsEffects.PRECISION, "Precision")
        translationBuilder.add(SoulOriginsEffects.NECROSIS, "Necrosis")
        translationBuilder.add(SoulOriginsEffects.PERCEPTION, "Perception")
        translationBuilder.add(SoulOriginsEffects.THRONGLED, "Throngled")

        translationBuilder.addPotionVariants(SoulOriginsPotions.PRECISION.base, "Precision")
        translationBuilder.addPotionVariants(SoulOriginsPotions.NECROSIS.base, "Necrosis")
        translationBuilder.addPotionVariants(SoulOriginsPotions.PERCEPTION.base, "Perception")
        translationBuilder.addPotionVariants(SoulOriginsPotions.THRONGLED.base, "Throngling")
    }
}
