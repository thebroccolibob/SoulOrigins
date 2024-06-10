package io.github.thebroccolibob.soulorigins.item

import io.github.thebroccolibob.soulorigins.FabricItemSettings
import io.github.thebroccolibob.soulorigins.ItemGroup
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.recipe.BrewingRecipeRegistry
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity

object SouloriginsItems {
    private fun <T: Item> register(id: String, item: T): T = Registry.register(Registries.ITEM, Identifier(Soulorigins.MOD_ID, id), item)

    private fun registerShard(type: String, color: Formatting) = register("${type}_shard", WindShardItem(color, FabricItemSettings {
        maxCount(1)
        rarity(Rarity.RARE)
    }))

    val MARIGOLD_CARD = register("marigold_card", MarigoldCardItem(FabricItemSettings {
        maxCount(1)
        rarity(Rarity.UNCOMMON)
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

    val ITEM_GROUP: ItemGroup = Registry.register(
        Registries.ITEM_GROUP,
        Identifier(Soulorigins.MOD_ID, "item_group"),
        ItemGroup {
            displayName(Text.translatable("itemGroup.soul-origins.item_group"))
            icon { MOB_ORB.defaultStack }
            entries { _, entries ->
                entries.add(UPDRAFT_SHARD)
                entries.add(TAILWIND_SHARD)
                entries.add(BURST_SHARD)

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
                        })
                    })
                }
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
