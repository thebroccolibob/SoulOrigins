package io.github.thebroccolibob.soulorigins

import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.world.World


object SoulOriginsDamage {
    @JvmField
    val MEMENTO_MORI_ACTIVE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, SoulOrigins.id("memento_mori_active"))
    @JvmField
    val MEMENTO_MORI_PASSIVE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, SoulOrigins.id("memento_mori_passive"))

    @JvmStatic
    fun RegistryKey<DamageType>.of(world: World): RegistryEntry<DamageType> {
        return world.registryManager.get(RegistryKeys.DAMAGE_TYPE).entryOf(this)
    }

    fun register() {}
}
