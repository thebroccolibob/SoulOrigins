@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.github.thebroccolibob.soulorigins.datagen.lib

import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemConvertible
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.*
import net.minecraft.loot.entry.AlternativeEntry
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.entry.LeafEntry
import net.minecraft.loot.function.ApplyBonusLootFunction
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.operator.BoundedIntUnaryOperator
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.LootNumberProvider
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.predicate.NumberRange
import net.minecraft.predicate.StatePredicate
import net.minecraft.predicate.entity.LocationPredicate
import net.minecraft.predicate.item.EnchantmentPredicate
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.util.Identifier

typealias ItemEntryBuilder = LeafEntry.Builder<*>

inline fun lootTable(init: LootTable.Builder.() -> Unit): LootTable.Builder {
    return LootTable.builder().apply(init)
}

inline fun LootTable.Builder.pool(rolls: LootNumberProvider, init: LootPool.Builder.() -> Unit) {
    pool(LootPool.builder().rolls(rolls).apply(init).build())
}

inline fun LootTable.Builder.pool(rolls: Int = 1, init: LootPool.Builder.() -> Unit) {
    pool(constant(rolls), init)
}

inline fun LootPool.Builder.item(drop: ItemConvertible, init: ItemEntryBuilder.() -> Unit) {
    with(ItemEntry.builder(drop).apply(init).build())
}

inline fun LootPool.Builder.alternatives(init: AlternativeEntry.Builder.() -> Unit) {
    with(AlternativeEntry.builder().apply(init).build())
}

inline fun LootPool.Builder.conditions(init: Conditions.() -> Unit) {
    Conditions(this).init()
}

fun ItemEntryBuilder.count(count: LootNumberProvider) {
    apply(SetCountLootFunction.builder(count))
}

fun ItemEntryBuilder.count(count: Int) {
    count(constant(count))
}

fun ItemEntryBuilder.fortune() {
    apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
}

inline fun ItemEntryBuilder.conditions(init: Conditions.() -> Unit) {
    Conditions(this).init()
}

inline fun AlternativeEntry.Builder.item(drop: ItemConvertible, init: ItemEntryBuilder.() -> Unit) {
    alternatively(ItemEntry.builder(drop).apply(init))
}

class Conditions(private val addCondition: (condition: LootCondition.Builder) -> Unit) {
    constructor(parentBuilder: LootConditionConsumingBuilder<*>) : this(parentBuilder::conditionally)

    operator fun LootCondition.Builder.unaryPlus() {
        addCondition(this)
    }

    fun survivesExplosion() {
        +SurvivesExplosionLootCondition.builder()
    }

    inline fun matchTool(init: ItemPredicate.Builder.() -> Unit) {
        +MatchToolLootCondition.builder(ItemPredicate.Builder.create().apply(init))
    }

    fun randomChance(chance: Float) {
        +RandomChanceLootCondition.builder(chance)
    }

    inline fun allOf(init: Conditions.() -> Unit) {
        +AllOfLootCondition.builder(*mutableListOf<LootCondition.Builder>().apply {
            Conditions(::add).init()
        }.toTypedArray())
    }

    inline fun anyOf(init: Conditions.() -> Unit) {
        +AnyOfLootCondition.builder(*mutableListOf<LootCondition.Builder>().apply {
            Conditions(::add).init()
        }.toTypedArray())
    }

    inline fun inverted(init: Conditions.() -> Unit) {
        lateinit var term: LootCondition.Builder
        Conditions { term = it }.init()
        +InvertedLootCondition.builder(term)
    }

    fun killedByPlayer() {
        +KilledByPlayerLootCondition.builder()
    }

    inline fun locationCheck(init: LocationPredicate.Builder.() -> Unit) {
        +LocationCheckLootCondition.builder(LocationPredicate.Builder.create().apply(init))
    }

    fun randomChanceWithLooting(chance: Float, lootingMultiplier: Float) {
        +RandomChanceWithLootingLootCondition.builder(chance, lootingMultiplier)
    }

    fun reference(id: Identifier) {
        +ReferenceLootCondition.builder(id)
    }

    fun reference(namespace: String, path: String) {
        reference(Identifier(namespace, path))
    }

    fun tableBonus(enchantment: Enchantment, vararg chance: Float) {
        +TableBonusLootCondition.builder(enchantment, *chance)
    }

    fun timeCheck(range: BoundedIntUnaryOperator) {
        +TimeCheckLootCondition.create(range)
    }

    fun timeCheck(range: IntRange) {
        timeCheck(BoundedIntUnaryOperator.create(range.min(), range.max()))
    }

    fun valueCheck(value: LootNumberProvider, range: BoundedIntUnaryOperator) {
        +ValueCheckLootCondition.builder(value, range)
    }

    fun valueCheck(value: LootNumberProvider, range: IntRange) {
        valueCheck(value, BoundedIntUnaryOperator.create(range.min(), range.max()))
    }

    inline fun weatherCheck(init: WeatherCheckLootCondition.Builder.() -> Unit) {
        +WeatherCheckLootCondition.create().apply(init)
    }

    fun blockState(block: Block, init: StatePredicate.Builder.() -> Unit) {
        +BlockStatePropertyLootCondition.builder(block).properties(StatePredicate.Builder.create().apply(init))
    }
}

fun ItemPredicate.Builder.silkTouch() {
    enchantment(EnchantmentPredicate(Enchantments.SILK_TOUCH, NumberRange.IntRange.ANY))
}

fun constant(count: Float): ConstantLootNumberProvider = ConstantLootNumberProvider.create(count)
fun constant(count: Int) = constant(count.toFloat())

fun uniform(min: Float, max: Float): UniformLootNumberProvider = UniformLootNumberProvider.create(min, max)
fun uniform(min: Int, max: Int) = uniform(min.toFloat(), max.toFloat())

inline fun FabricBlockLootTableProvider.addTable(block: Block, init: LootTable.Builder.() -> Unit) {
    addDrop(block, lootTable(init))
}

inline fun FabricBlockLootTableProvider.addPool(block: Block, rolls: Int = 1, init: LootPool.Builder.() -> Unit) {
    addTable(block) {
        pool(rolls, init)
    }
}
