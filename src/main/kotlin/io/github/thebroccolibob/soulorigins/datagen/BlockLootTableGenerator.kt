package io.github.thebroccolibob.soulorigins.datagen

import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import io.github.thebroccolibob.soulorigins.datagen.lib.*
import io.github.thebroccolibob.soulorigins.item.SoulOriginsItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.item.Items

class BlockLootTableGenerator(dataOutput: FabricDataOutput) : FabricBlockLootTableProvider(dataOutput) {
    override fun generate() {
        addTable(SoulOriginsBlocks.DECAYING_SLIME) {
            pool {
                item(Items.SLIME_BALL) {
                    count(constant(1))
                }
                conditions {
                    randomChance(0.5f)
                    survivesExplosion()
                }
            }
        }

        addTable(SoulOriginsBlocks.DECAYING_ROTTEN_FLESH) {
            pool {
                item(Items.ROTTEN_FLESH) {
                    count(constant(1))
                }
                conditions {
                    randomChance(0.25f)
                    survivesExplosion()
                }
            }
        }

        addTable(SoulOriginsBlocks.FALLING_DECAYING_SAND) {
            pool {
                item(Items.SAND) {
                    count(1)
                }
                conditions {
                    randomChance(0.125f)
                    survivesExplosion()
                }
            }
        }

        addTable(SoulOriginsBlocks.DECAYING_SAND) {
            pool {
                item(Items.SAND) {
                    count(1)
                }
                conditions {
                    randomChance(0.125f)
                    survivesExplosion()
                }
            }
        }

        addTable(SoulOriginsBlocks.DECAYING_COBWEB_BLOCK) {
            pool {
                item(Items.STRING) {
                    count(1)
                }
                conditions {
                    randomChance(0.25f)
                    survivesExplosion()
                }
            }
        }

        addDrop(SoulOriginsBlocks.ARTIFICER_PLATFORM_BUILDER, SoulOriginsItems.ARTIFICER_PLATFORM_BUILDER)
        addDrop(SoulOriginsBlocks.ARTIFICER_EW_WALL_BUILDER, SoulOriginsItems.ARTIFICER_WALL_BUILDER)
        addDrop(SoulOriginsBlocks.ARTIFICER_NS_WALL_BUILDER, SoulOriginsItems.ARTIFICER_WALL_BUILDER)
        addDrop(SoulOriginsBlocks.ARTIFICER_COLUMN_BUILDER, SoulOriginsItems.ARTIFICER_COLUMN_BUILDER)
    }
}
