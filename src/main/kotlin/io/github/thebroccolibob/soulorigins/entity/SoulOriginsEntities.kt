package io.github.thebroccolibob.soulorigins.entity

import io.github.thebroccolibob.soulorigins.Soulorigins
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object SoulOriginsEntities {
    private fun <T: Entity> register(path: String, entityType: EntityType<T>): EntityType<T> = Registry.register(Registries.ENTITY_TYPE, Identifier(Soulorigins.MOD_ID, path), entityType)

    val SURFACE_BUILDER_PROJECTILE = register("surface_builder_projectile", FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::SurfaceBuilderProjectileEntity).apply {
        dimensions(EntityDimensions.fixed(0.25f, 0.25f))
        trackRangeChunks(8)
    }.build())

    fun register() {}
}
