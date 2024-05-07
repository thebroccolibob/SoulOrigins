package io.github.thebroccolibob.soulorigins.datagen.lib

import net.minecraft.advancement.Advancement
import net.minecraft.advancement.criterion.ConsumeItemCriterion
import net.minecraft.item.ItemConvertible
import net.minecraft.loot.condition.LootCondition
import net.minecraft.predicate.NbtPredicate
import net.minecraft.predicate.NumberRange
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.predicate.entity.LootContextPredicate
import net.minecraft.predicate.entity.PlayerPredicate
import net.minecraft.predicate.item.EnchantmentPredicate
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.util.Identifier
import java.util.function.Consumer

inline fun EntityPredicate(init: EntityPredicate.Builder.() -> Unit): EntityPredicate {
    return EntityPredicate.Builder.create().apply(init).build()
}

inline fun PlayerPredicate(init: PlayerPredicate.Builder.() -> Unit): PlayerPredicate {
    return PlayerPredicate.Builder.create().apply(init).build()
}

fun LootContextPredicate(vararg conditions: LootCondition): LootContextPredicate {
    return LootContextPredicate.create(*conditions)
}

fun LootContextPredicate(vararg conditions: LootCondition.Builder): LootContextPredicate {
    return LootContextPredicate.create(*conditions.map(LootCondition.Builder::build).toTypedArray())
}

fun Consumer<Advancement>.advancement(id: Identifier, init: Advancement.Builder.() -> Unit): Advancement {
    return Advancement.Builder.createUntelemetered().apply(init).build(id).also(::accept)
}

fun consumeItemCondition(predicate: LootContextPredicate, item: ItemConvertible): ConsumeItemCriterion.Conditions {
    return ConsumeItemCriterion.Conditions(
        predicate,
        ItemPredicate(
            null,
            setOf(item.asItem()),
            NumberRange.IntRange.ANY,
            NumberRange.IntRange.ANY,
            EnchantmentPredicate.ARRAY_OF_ANY,
            EnchantmentPredicate.ARRAY_OF_ANY,
            null,
            NbtPredicate.ANY
        )
    )
}
