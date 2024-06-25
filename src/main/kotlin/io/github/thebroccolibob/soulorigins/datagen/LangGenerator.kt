package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.datagen.power.LeveledCooldownEntry
import io.github.thebroccolibob.soulorigins.datagen.power.wind.tailwindEntries
import io.github.thebroccolibob.soulorigins.datagen.power.wind.updraftEntries
import io.github.thebroccolibob.soulorigins.effect.SoulOriginsEffects
import io.github.thebroccolibob.soulorigins.item.SoulOriginsItems
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
        for (entry in entries) {
            val level = entry.level
            add("advancements.$modId.wind.$id.lvl$level", "$name ${romanNumerals[level]}")
            add("advancements.$modId.wind.$id.lvl$level.description", (description(level)?.let {desc->"$desc\n"} ?: "") + "${entry.charges} charge${if (entry.charges == 1) "" else "s"}\n${entry.recharge / 20}s")
        }
    }

    private fun TranslationBuilder.addPotionVariants(potion: Potion, name: String, prefix: String = "") {
        for ((item, translation) in listOf(
            Items.POTION to "Potion of ",
            Items.SPLASH_POTION to "Splash Potion of ",
            Items.LINGERING_POTION to "Lingering Potion of ",
            Items.TIPPED_ARROW to "Arrow of ",
        )) {
            add(potion.finishTranslationKey("${item.translationKey}.effect."), "$prefix$translation$name")
        }
    }

    private fun TranslationBuilder.addDeathVariants(id: String, message: String, playerMessage: String? = null, itemMessage: String? = null) {
        add("death.attack.$id", message)
        playerMessage?.let { add("death.attack.$id.player", it)}
        itemMessage?.let { add("death.attack.$id.item", it)}
    }

    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        with(translationBuilder) {
            add("advancements.$modId.wind.root", "Wind Spirit")
            add("advancements.$modId.wind.root.description", "Level up your skills by finding wind crystals!")
            cooldownDescriptions("updraft", "Updraft", updraftEntries, updraftDescription)
            cooldownDescriptions("tailwind", "Tailwind", tailwindEntries, tailwindDescription)

            for (lvl in 1..3) {
                translationBuilder.add("advancements.$modId.wind.burst.lvl$lvl", "Burst ${romanNumerals[lvl]}")
            }

            add("advancements.$modId.wind.burst.lvl1.description", "Unlocks burst\nSpeed & invis 15s\n100% Soul")
            add("advancements.$modId.wind.burst.lvl2.description", "Adds proj prot 30s\nRecharges updraft & tailwind")
            add("advancements.$modId.wind.burst.lvl3.description", "Increases to 30s")

            add("itemGroup.soul-origins.item_group", "Soul Origins")

            add(SoulOriginsItems.BURST_SHARD, "Burst Shard")
            add(SoulOriginsItems.BURST_SHARD + "flavor", "A shard resonating with an eruptive power")
            add(SoulOriginsItems.UPDRAFT_SHARD, "Updraft Shard")
            add(SoulOriginsItems.UPDRAFT_SHARD + "flavor", "A shard resonating with an uplifting power")
            add(SoulOriginsItems.TAILWIND_SHARD, "Tailwind Shard")
            add(SoulOriginsItems.TAILWIND_SHARD + "flavor", "A shard resonating with a turbulent power")
            add("item.soul-origins.wind_shard.origin_exclusive", "Only usable by wind spirits")

            add(SoulOriginsItems.ARTIFICER_PLATFORM_BUILDER, "Brass Platform Builder")
            add(SoulOriginsItems.ARTIFICER_WALL_BUILDER, "Brass Wall Builder")
            add(SoulOriginsItems.ARTIFICER_COLUMN_BUILDER, "Brass Column Builder")
            add("item.soul-origins.artificer_builder.place_distance", "Place Distance: %s")
            add("item.soul-origins.artificer_builder.place_distance.none", "Unset")

            add(SoulOriginsItems.MOB_ORB, "Mob Orb")
            add(SoulOriginsItems.MOB_ORB + "empty", "Empty")
            add(SoulOriginsItems.MOB_ORB + "multiple_items", "%s x%s")

            add("container.soul-origins.suspicious_brewing", "Suspicious Brewing")

            add(SoulOriginsItems.SUSPICIOUS_BREW, "Suspicious Brew")
            add(SoulOriginsItems.SPLASH_SUSPICIOUS_BREW, "Splash Suspicious Brew")
            add(SoulOriginsItems.LINGERING_SUSPICIOUS_BREW, "Lingering Suspicious Brew")
            add("item.soul-origins.suspicious_brew.tooltip", "Effects Unknown")

            add(SoulOriginsEffects.PRECISION, "Precision")
            add(SoulOriginsEffects.NECROSIS, "Necrosis")
            add(SoulOriginsEffects.PERCEPTION, "Perception")
            add(SoulOriginsEffects.THRONGLED, "Throngled")
            // Italic
            add(SoulOriginsEffects.MEMENTO_MORI, "\u00A7oMemento Mori")

            addPotionVariants(SoulOriginsPotions.PRECISION.base, "Precision")
            addPotionVariants(SoulOriginsPotions.NECROSIS.base, "Necrosis")
            addPotionVariants(SoulOriginsPotions.PERCEPTION.base, "Perception")
            addPotionVariants(SoulOriginsPotions.THRONGLED.base, "Throngling")
            addPotionVariants(SoulOriginsPotions.MementoMori.final, "Memento Mori")
            addPotionVariants(SoulOriginsPotions.MementoMori.stage0, "Memento Mori", prefix = "Incomplete ")

            addDeathVariants("soul-origins.memento_mori_passive",
                message = "%s soul dwindled",
                playerMessage = "%s soul gave out whilst fighting %s"
            )
            addDeathVariants("soul-origins.memento_mori_active",
                message = "%s soul dwindled via magic by %s",
                itemMessage = "%s soul dwindled via magic by %s using %s"
            )
        }
    }
}
