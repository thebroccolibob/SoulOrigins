package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import io.github.thebroccolibob.soulorigins.block.entity.BeeBombBlockEntity
import io.github.thebroccolibob.soulorigins.datagen.lib.*
import io.github.thebroccolibob.soulorigins.item.SoulOriginsItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.item.Items
import net.minecraft.loot.function.CopyNbtLootFunction
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider

class BlockLootTableGenerator(dataOutput: FabricDataOutput) : FabricBlockLootTableProvider(dataOutput) {
    override fun generate() {
        addPool(SoulOriginsBlocks.DECAYING_SLIME) {
            item(Items.SLIME_BALL) {
                count(constant(1))
            }
            conditions {
                randomChance(0.5f)
                survivesExplosion()
            }
        }

        addPool(SoulOriginsBlocks.DECAYING_ROTTEN_FLESH) {
            item(Items.ROTTEN_FLESH) {
                count(constant(1))
            }
            conditions {
                randomChance(0.25f)
                survivesExplosion()
            }
        }

        addPool(SoulOriginsBlocks.FALLING_DECAYING_SAND) {
            item(Items.SAND) {
                count(1)
            }
            conditions {
                randomChance(0.125f)
                survivesExplosion()
            }
        }

        addPool(SoulOriginsBlocks.DECAYING_SAND) {
            item(Items.SAND) {
                count(1)
            }
            conditions {
                randomChance(0.125f)
                survivesExplosion()
            }
        }

        addPool(SoulOriginsBlocks.DECAYING_COBWEB_BLOCK) {
            item(Items.STRING) {
                count(1)
            }
            conditions {
                randomChance(0.25f)
                survivesExplosion()
            }
        }

        addDrop(SoulOriginsBlocks.ARTIFICER_PLATFORM_BUILDER, SoulOriginsItems.ARTIFICER_PLATFORM_BUILDER)
        addDrop(SoulOriginsBlocks.ARTIFICER_EW_WALL_BUILDER, SoulOriginsItems.ARTIFICER_WALL_BUILDER)
        addDrop(SoulOriginsBlocks.ARTIFICER_NS_WALL_BUILDER, SoulOriginsItems.ARTIFICER_WALL_BUILDER)
        addDrop(SoulOriginsBlocks.ARTIFICER_COLUMN_BUILDER, SoulOriginsItems.ARTIFICER_COLUMN_BUILDER)

        addPool(SoulOriginsBlocks.BEE_BOMB) {
            item(SoulOriginsItems.BEE_BOMB) {
                count(1)
            }

            apply(CopyNbtLootFunction.builder(ContextLootNbtProvider.BLOCK_ENTITY).apply {
                withOperation(BeeBombBlockEntity.BEES_KEY, "BlockEntityTag.${BeeBombBlockEntity.BEES_KEY}")
            })
        }

        lootTables.put(SoulOrigins.id("blocks/bee_silk_touch", ))
    }
}
