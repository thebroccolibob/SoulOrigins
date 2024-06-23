package io.github.thebroccolibob.soulorigins.potion

import net.minecraft.potion.Potion

interface LongPotionSet {
    val base: Potion
    val long: Potion

    companion object : (Potion, Potion) -> LongPotionSet {
        override fun invoke(base: Potion, long: Potion) = object : LongPotionSet {
            override val base = base
            override val long = long
        }
    }
}

interface StrongPotionSet {
    val base: Potion
    val strong: Potion

    companion object : (Potion, Potion) -> StrongPotionSet {
        override fun invoke(base: Potion, strong: Potion) = object : StrongPotionSet {
            override val base = base
            override val strong = strong
        }
    }
}

class FullPotionSet(override val base: Potion, override val long: Potion, override val strong: Potion) : LongPotionSet, StrongPotionSet
