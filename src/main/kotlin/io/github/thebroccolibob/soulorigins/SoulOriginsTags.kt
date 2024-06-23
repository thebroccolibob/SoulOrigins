package io.github.thebroccolibob.soulorigins

import net.minecraft.entity.EntityType
import net.minecraft.potion.Potion
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object SoulOriginsTags {
    val SOUL_SORCERER_FRIENDS: TagKey<EntityType<*>> = TagKey.of(RegistryKeys.ENTITY_TYPE, SoulOrigins.id("soul_sorcerer_friends"))

    @JvmField
    val SECRET_POTIONS: TagKey<Potion> = TagKey.of(RegistryKeys.POTION, SoulOrigins.id("secret_potions"))

    fun register() {}
}
