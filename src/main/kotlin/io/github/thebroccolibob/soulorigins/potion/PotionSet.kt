package io.github.thebroccolibob.soulorigins.potion

import net.minecraft.potion.Potion

interface LongPotionSet {
    val base: Potion
    val long: Potion

    class Impl(override val base: Potion, override val long: Potion) : LongPotionSet

    companion object : (Potion, Potion) -> LongPotionSet {
        override fun invoke(base: Potion, long: Potion) = Impl(base, long)
    }

}

interface StrongPotionSet {
    val base: Potion
    val strong: Potion

    class Impl(override val base: Potion, override val strong: Potion) : StrongPotionSet

    companion object : (Potion, Potion) -> StrongPotionSet {
        override fun invoke(base: Potion, strong: Potion) = Impl(base, strong)
    }
}

class FullPotionSet(override val base: Potion, override val long: Potion, override val strong: Potion) : LongPotionSet, StrongPotionSet
