package io.github.thebroccolibob.soulorigins.item

import io.github.thebroccolibob.soulorigins.FabricItemSettings
import io.github.thebroccolibob.soulorigins.ItemGroup
import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import io.github.thebroccolibob.soulorigins.put
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry
import net.minecraft.entity.EntityType
import net.minecraft.item.*
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.recipe.BrewingRecipeRegistry
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Rarity

object SoulOriginsItems {
    private fun <T: Item> register(id: String, item: T): T = Registry.register(Registries.ITEM, SoulOrigins.id(id), item)

    private fun registerShard(type: String, color: Formatting) = register("${type}_shard", WindShardItem(color, FabricItemSettings {
        maxCount(1)
        rarity(Rarity.RARE)
    }))

    val MOB_ORB = register("mob_orb", MobOrbItem(FabricItemSettings {
        maxCount(1)
        rarity(Rarity.EPIC)
    }))

    val UPDRAFT_SHARD = registerShard("updraft", Formatting.AQUA)
    val TAILWIND_SHARD = registerShard("tailwind", Formatting.GREEN)
    val BURST_SHARD = registerShard("burst", Formatting.LIGHT_PURPLE)

    @JvmField
    val SUSPICIOUS_BREW = register("suspicious_brew", SuspiciousBrewItem(FabricItemSettings {
        maxCount(1)
    }))
    @JvmField
    val SPLASH_SUSPICIOUS_BREW = register("splash_suspicious_brew", SplashSuspiciousBrewItem(FabricItemSettings {
        maxCount(1)
    }))
    @JvmField
    val LINGERING_SUSPICIOUS_BREW = register("lingering_suspicious_brew", LingeringSuspiciousBrewItem(FabricItemSettings {
        maxCount(1)
    }))

    val ARTIFICER_CORE = register("artificer_core", Item(FabricItemSettings {
        rarity(Rarity.UNCOMMON)
    }))

    val ARTIFICER_PLATFORM_BUILDER = register("artificer_platform_builder", AdjustableSurfaceBuilderProjectileItem(SoulOriginsBlocks.ARTIFICER_PLATFORM_BUILDER, 0, intArrayOf(0, 5, 7, 10, 15), FabricItemSettings {
        maxCount(16)
    }))
    val ARTIFICER_WALL_BUILDER = register("artificer_wall_builder", DirectionalSurfaceBuilderProjectileItem(SoulOriginsBlocks.ARTIFICER_NS_WALL_BUILDER, SoulOriginsBlocks.ARTIFICER_EW_WALL_BUILDER, 1, FabricItemSettings {
        maxCount(16)
    }))
    val ARTIFICER_COLUMN_BUILDER = register("artificer_column_builder", SurfaceBuilderProjectileItem(SoulOriginsBlocks.ARTIFICER_COLUMN_BUILDER, 0, FabricItemSettings {
        maxCount(16)
    }))

    val TEST_GUN = register("test_gun", CooldownGunItem(3, 60, FabricItemSettings {
        maxCount(1)
    }))

    val MANDALORIAN_HELMET = register("mandalorian_helmet", ArmorItem(object : ArmorMaterial by ArmorMaterials.NETHERITE {
        override fun getName() = "beskar"
    }, ArmorItem.Type.HELMET, FabricItemSettings {
        maxCount(1)
    }))

    val ITEM_GROUP: ItemGroup = Registry.register(
        Registries.ITEM_GROUP,
        SoulOrigins.id("item_group"),
        ItemGroup {
            displayName(Text.translatable("itemGroup.soul-origins.item_group"))
            icon { MOB_ORB.defaultStack }
            entries { _, entries ->
                entries.add(UPDRAFT_SHARD)
                entries.add(TAILWIND_SHARD)
                entries.add(BURST_SHARD)

                entries.add(ARTIFICER_PLATFORM_BUILDER)
                entries.add(ARTIFICER_WALL_BUILDER)
                entries.add(ARTIFICER_COLUMN_BUILDER)

                listOf(
                    EntityType.ZOMBIE,
                    EntityType.HUSK,
                    EntityType.DROWNED,
                    EntityType.SKELETON,
                    EntityType.STRAY,
                    EntityType.WITHER_SKELETON,
                    EntityType.SPIDER,
                    EntityType.CAVE_SPIDER,
                    EntityType.CREEPER,
                    EntityType.ENDERMAN,
                    EntityType.SLIME,
                    EntityType.WARDEN,
                ).forEach {
                    entries.add(ItemStack(MOB_ORB, 1).apply {
                        setSubNbt(MobOrbItem.ENTITY_NBT, NbtCompound().apply {
                            putString("id", Registries.ENTITY_TYPE.getId(it).toString())
                            when (it) {
                                EntityType.SKELETON, EntityType.STRAY -> Items.BOW
                                EntityType.WITHER_SKELETON -> Items.STONE_SWORD
                                else -> null
                            }?.let { handItem ->
                                put("HandItems", NbtList().apply {
                                    add(handItem.defaultStack.writeNbt(NbtCompound()))
                                })
                            }
                            if (it == EntityType.WARDEN) {
                                put("Brain") {
                                    put("memories") {
                                        put("minecraft:dig_cooldown") {
                                            put("value") {}
                                            putLong("ttl", 1200)
                                        }
                                    }
                                }
                            }
                        })
                    })
                }

                entries.add(MANDALORIAN_HELMET)
                entries.add(TEST_GUN)
            }
        }
    )

    fun register() {
        BrewingRecipeRegistry.registerPotionType(SUSPICIOUS_BREW)
        BrewingRecipeRegistry.registerPotionType(SPLASH_SUSPICIOUS_BREW)
        BrewingRecipeRegistry.registerPotionType(LINGERING_SUSPICIOUS_BREW)
        FabricBrewingRecipeRegistry.registerItemRecipe(SUSPICIOUS_BREW, Ingredient.ofItems(Items.GUNPOWDER), SPLASH_SUSPICIOUS_BREW)
        FabricBrewingRecipeRegistry.registerItemRecipe(SPLASH_SUSPICIOUS_BREW, Ingredient.ofItems(Items.DRAGON_BREATH), LINGERING_SUSPICIOUS_BREW)
    }
}
