package io.github.thebroccolibob.soulorigins

import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object SoulOriginsTags {
    val SOUL_SORCERER_FRIENDS: TagKey<EntityType<*>> = TagKey.of(RegistryKeys.ENTITY_TYPE, SoulOrigins.id("soul_sorcerer_friends"))

    fun register() {}
}
