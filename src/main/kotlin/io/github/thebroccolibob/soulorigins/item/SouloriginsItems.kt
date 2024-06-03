package io.github.thebroccolibob.soulorigins.item

import io.github.thebroccolibob.soulorigins.FabricItemSettings
import io.github.thebroccolibob.soulorigins.ItemGroup
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity

object SouloriginsItems {
    private fun register(id: String, item: Item): Item = Registry.register(Registries.ITEM, Identifier(Soulorigins.MOD_ID, id), item)

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

    val ITEM_GROUP = Registry.register(
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
                        setSubNbt("EntityTag", NbtCompound().apply {
                            putString("id", Registries.ENTITY_TYPE.getId(it).toString())
                        })
                    })
                }
            }
        }
    )

    fun register() {}
}
