package io.github.thebroccolibob.soulorigins.entity

import io.github.thebroccolibob.soulorigins.SoulOrigins
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object SoulOriginsEntities {
    private fun <T: Entity> register(path: String, entityType: EntityType<T>): EntityType<T> = Registry.register(Registries.ENTITY_TYPE, SoulOrigins.id(path), entityType)

    val SURFACE_BUILDER_PROJECTILE = register("surface_builder_projectile", FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::SurfaceBuilderProjectileEntity).apply {
        dimensions(EntityDimensions.fixed(0.25f, 0.25f))
        trackRangeChunks(8)
    }.build())

    val LOYALTY_ITEM = register("loyalty_item", FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::LoyaltyItemEntity).apply {
        dimensions(EntityDimensions.fixed(0.25f, 0.25f))
        trackRangeChunks(8)
    }.build())

    fun register() {}
}
