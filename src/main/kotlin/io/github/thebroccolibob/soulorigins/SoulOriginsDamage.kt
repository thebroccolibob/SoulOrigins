package io.github.thebroccolibob.soulorigins

import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.world.World


object SoulOriginsDamage {
    @JvmField
    val MEMENTO_MORI_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, SoulOrigins.id("memento_mori"))

    @JvmStatic
    fun RegistryKey<DamageType>.of(world: World): RegistryEntry<DamageType> {
        return world.registryManager.get(RegistryKeys.DAMAGE_TYPE).entryOf(this)
    }

    fun register() {}
}
