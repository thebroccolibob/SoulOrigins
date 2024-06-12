package io.github.thebroccolibob.soulorigins.datagen

import io.github.apace100.origins.util.ChoseOriginCriterion
import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.datagen.lib.*
import io.github.thebroccolibob.soulorigins.item.SoulOriginsItems
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.loot.condition.EntityPropertiesLootCondition
import net.minecraft.loot.context.LootContext
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.text.Text
import java.util.function.Consumer

val chose_wind = ChoseOriginCriterion.Conditions(LootContextPredicate.EMPTY, SoulOrigins.id("wind_spirit"))

private fun Consumer<Advancement>.advancement(path: String, init: Advancement.Builder.() -> Unit) = advancement(SoulOrigins.id(path), init)

fun Consumer<Advancement>.windLevels(root: Advancement, id: String, item: Item, levels: Int, lvl1Unlocked: Boolean = false) {
    (1..levels).fold(root) { prev, index ->
        advancement("wind/$id/lvl${index}") {
            display(
                item,
                Text.translatable("advancements.soul-origins.wind.$id.lvl$index"),
                Text.translatable("advancements.soul-origins.wind.$id.lvl$index.description"),
                null,
                if (index == levels) AdvancementFrame.CHALLENGE else AdvancementFrame.TASK,
                false,
                false,
                false,
            )

            parent(prev)

            if (index == 1 && lvl1Unlocked) {
                criterion("wind_origin", chose_wind)
            } else {
                criterion(
                    "use_item", consumeItemCondition(
                        if (index == 1) LootContextPredicate.EMPTY else LootContextPredicate(
                            EntityPropertiesLootCondition.builder(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate {
                                    typeSpecific(PlayerPredicate {
                                        advancement(prev.id, true)
                                    })
                                })
                        ), item
                    )
                )
            }
        }
    }
}

fun Consumer<Advancement>.generateAdvancements() {
    val root = advancement("wind/root") {
        display(
            Items.FEATHER,
            Text.translatable("advancements.soul-origins.wind.root"),
            Text.translatable("advancements.soul-origins.wind.root.description"),
            SoulOrigins.id("textures/gui/advancements/backgrounds/wind.png"),
            AdvancementFrame.GOAL,
            false,
            false,
            false,
        )

        criterion("wind_origin", chose_wind)
    }

    windLevels(root, "tailwind", SoulOriginsItems.TAILWIND_SHARD, 5, true)
    windLevels(root, "updraft", SoulOriginsItems.UPDRAFT_SHARD, 5, true)
    windLevels(root, "burst", SoulOriginsItems.BURST_SHARD, 3)
//    windLevels(root, "neutral", SouloriginsItems.NEUTRAL_CRYSTAL, 3, true)
}
