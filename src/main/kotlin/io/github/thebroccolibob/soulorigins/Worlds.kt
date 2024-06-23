package io.github.thebroccolibob.soulorigins

import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.world.World

@JvmField
val WHITESPACE_DIMENSION: RegistryKey<World> = RegistryKey.of(RegistryKeys.WORLD, Identifier("dyedvoid", "whitespace"))
