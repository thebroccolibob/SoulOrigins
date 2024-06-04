package io.github.thebroccolibob.soulorigins.datagen.lib

import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
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
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.LootNumberProvider
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.predicate.NumberRange
import net.minecraft.predicate.item.EnchantmentPredicate
import net.minecraft.predicate.item.ItemPredicate

inline fun lootTable(init: LootTable.Builder.() -> Unit): LootTable.Builder {
    return LootTable.builder().apply(init)
}

inline fun LootTable.Builder.pool(rolls: LootNumberProvider, init: LootPool.Builder.() -> Unit) {
    pool(LootPool.builder().rolls(rolls).apply(init).build())
}

inline fun LootTable.Builder.pool(rolls: Int = 1, init: LootPool.Builder.() -> Unit) {
    pool(constant(rolls), init)
}

inline fun LootPool.Builder.item(drop: ItemConvertible, init: LeafEntry.Builder<*>.() -> Unit) {
    with(ItemEntry.builder(drop).apply(init).build())
}

inline fun LootPool.Builder.alternatives(init: AlternativeEntry.Builder.() -> Unit) {
    with(AlternativeEntry.builder().apply(init).build())
}

inline fun LootPool.Builder.conditions(init: Conditions.() -> Unit) {
    Conditions(this).init()
}

fun LeafEntry.Builder<*>.count(count: LootNumberProvider) {
    apply(SetCountLootFunction.builder(count))
}

val LeafEntry.Builder<*>.fortune: Unit
    get() {
        apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
    }

inline fun LeafEntry.Builder<*>.conditions(init: Conditions.() -> Unit) {
    Conditions(this).init()
}

inline fun AlternativeEntry.Builder.item(drop: ItemConvertible, init: LeafEntry.Builder<*>.() -> Unit) {
    alternatively(ItemEntry.builder(drop).apply(init))
}

class Conditions(private val parentBuilder: LootConditionConsumingBuilder<*>) {
    fun survivesExplosion() {
        parentBuilder.conditionally(SurvivesExplosionLootCondition.builder())
    }

    fun tool(init: ItemPredicate.Builder.() -> Unit) {
        parentBuilder.conditionally(MatchToolLootCondition.builder(ItemPredicate.Builder.create().apply(init)))
    }

    fun randomChance(chance: Float) {
        parentBuilder.conditionally(RandomChanceLootCondition.builder(chance))
    }
}

fun ItemPredicate.Builder.silkTouch() {
    enchantment(EnchantmentPredicate(Enchantments.SILK_TOUCH, NumberRange.IntRange.ANY))
}

fun constant(count: Float): ConstantLootNumberProvider = ConstantLootNumberProvider.create(count)
fun constant(count: Int) = constant(count.toFloat())

fun uniform(min: Float, max: Float): UniformLootNumberProvider = UniformLootNumberProvider.create(min, max)
fun uniform(min: Int, max: Int) = uniform(min.toFloat(), max.toFloat())

operator fun LootCondition.Builder.not(): LootCondition.Builder = this.invert()

inline fun FabricBlockLootTableProvider.addTable(block: Block, init: LootTable.Builder.() -> Unit) {
    addDrop(block, lootTable(init))
}
