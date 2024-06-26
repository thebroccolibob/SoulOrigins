package io.github.thebroccolibob.soulorigins

import io.github.thebroccolibob.soulorigins.datagen.lib.conditions
import io.github.thebroccolibob.soulorigins.datagen.lib.count
import io.github.thebroccolibob.soulorigins.datagen.lib.item
import io.github.thebroccolibob.soulorigins.datagen.lib.pool
import io.github.thebroccolibob.soulorigins.item.SoulOriginsItems
import net.fabricmc.fabric.api.loot.v2.LootTableEvents
import net.minecraft.loot.LootTables
import net.minecraft.util.Identifier

fun registerSoulOriginsLootModifier() {
    val locations = listOf(
        LootTables.SIMPLE_DUNGEON_CHEST,
        LootTables.ABANDONED_MINESHAFT_CHEST,
        LootTables.NETHER_BRIDGE_CHEST,
        LootTables.STRONGHOLD_LIBRARY_CHEST,
        LootTables.STRONGHOLD_CROSSING_CHEST,
        LootTables.STRONGHOLD_CORRIDOR_CHEST,
        LootTables.DESERT_PYRAMID_CHEST,
        LootTables.JUNGLE_TEMPLE_CHEST,
        LootTables.JUNGLE_TEMPLE_DISPENSER_CHEST,
        LootTables.IGLOO_CHEST_CHEST,
        LootTables.WOODLAND_MANSION_CHEST,
        LootTables.UNDERWATER_RUIN_SMALL_CHEST,
        LootTables.UNDERWATER_RUIN_BIG_CHEST,
        LootTables.BURIED_TREASURE_CHEST,
        LootTables.SHIPWRECK_MAP_CHEST,
        LootTables.SHIPWRECK_SUPPLY_CHEST,
        LootTables.SHIPWRECK_TREASURE_CHEST,
        LootTables.PILLAGER_OUTPOST_CHEST,
        LootTables.BASTION_TREASURE_CHEST,
        LootTables.BASTION_OTHER_CHEST,
        LootTables.BASTION_BRIDGE_CHEST,
        LootTables.BASTION_HOGLIN_STABLE_CHEST,
        LootTables.ANCIENT_CITY_CHEST,
        LootTables.ANCIENT_CITY_ICE_BOX_CHEST,
        LootTables.RUINED_PORTAL_CHEST,
        Identifier("terralith", "underground/giant_bee_hive"),
        Identifier("terralith", "underground/chest"),
        Identifier("terralith", "igloo"),
        Identifier("terralith", "spire/common"),
        Identifier("terralith", "spire/junk"),
        Identifier("terralith", "spire/rare"),
        Identifier("terralith", "spire/treasure"),
        Identifier("terralith", "royal_jelly"),
        Identifier("terralith", "lodge_drinkables"),
        Identifier("terralith", "mage/barracks"),
        Identifier("terralith", "mage/extras"),
        Identifier("terralith", "mage/treasure"),
        Identifier("terralith", "desert_outpost"),
        Identifier("terralith", "ruin/glacial/main_cs"),
        Identifier("terralith", "ruin/glacial/junk"),
        Identifier("betterwitchhuts", "chests/hut_0"),
        Identifier("betternether", "chests/city_surprise"),
        Identifier("betternether", "chests/ghast_hive"),
        Identifier("betternether", "chests/wither_tower_bonus"),
        Identifier("betternether", "chests/city"),
        Identifier("betternether", "chests/city_common"),
        Identifier("betternether", "chests/library"),
        Identifier("betternether", "chests/wither_tower"),
        Identifier("betterjungletemples", "chests/campsite"),
        Identifier("betterjungletemples", "chests/treasure"),
        Identifier("betterdungeons", "small_nether_dungeon/chests/common"),
        Identifier("betterdungeons", "spider_dungeon/chests/egg_room"),
        Identifier("betterdungeons", "zombie_dungeon/chests/tombstone"),
        Identifier("betterdungeons", "zombie_dungeon/chests/common"),
        Identifier("betterdungeons", "zombie_dungeon/chests/special"),
        Identifier("betterdungeons", "small_dungeon/chests/loot_piles"),
        Identifier("betterdungeons", "skeleton_dungeon/chests/middle"),
        Identifier("betterdungeons", "skeleton_dungeon/chests/common"),
        Identifier("betterfortresses", "chests/worship"),
        Identifier("betterfortresses", "chests/puzzle"),
        Identifier("betterfortresses", "chests/hall"),
        Identifier("betterfortresses", "chests/obsidian"),
        Identifier("betterfortresses", "chests/storage"),
        Identifier("betterfortresses", "chests/quarters"),
        Identifier("betterfortresses", "chests/keep"),
        Identifier("betterfortresses", "chests/extra"),
        Identifier("betterfortresses", "chests/beacon"),
        Identifier("minecraft", "chests/illager_mansion/generic"),
        Identifier("minecraft", "chests/illager_mansion/pillager_chest"),
        Identifier("minecraft", "chests/illager_mansion/vindicator_chest"),
        Identifier("minecraft", "chests/illager_mansion/kitchen"),
        Identifier("minecraft", "chests/illager_mansion/library_chest"),
        Identifier("minecraft", "chests/illager_mansion/witch_chest"),
        Identifier("minecraft", "chests/illager_mansion/secret_room"),
        Identifier("minecraft", "chests/illager_mansion/evoker_chest"),
        Identifier("minecraft", "chests/illager_mansion/smithing_room"),
        Identifier("minecraft", "chests/illager_mansion/wool"),
        Identifier("minecraft", "chests/illager_mansion/map_chest"),
        Identifier("minecraft", "chests/illager_mansion/ancient_city_raid_chest"),
        Identifier("minecraft", "chests/illager_mansion/ravager_chest"),
        Identifier("minecraft", "chests/nether_fortress/fort_inside"),
        Identifier("minecraft", "chests/nether_fortress/fort_inside_generic"),
        Identifier("betterdeserttemples", "chests/tomb"),
        Identifier("betterdeserttemples", "chests/wardrobe"),
        Identifier("betterdeserttemples", "chests/pharaoh_hidden"),
        Identifier("betterdeserttemples", "chests/tomb_pharaoh"),
        Identifier("betterdeserttemples", "chests/storage"),
        Identifier("betterdeserttemples", "chests/lab"),
        Identifier("betterdeserttemples", "chests/statue"),
        Identifier("betterdeserttemples", "chests/library"),
        Identifier("betterdeserttemples", "chests/pot"),
        Identifier("betterdeserttemples", "chests/food_storage"),
        Identifier("betteroceanmonuments", "chests/upper_side_chamber"),
        Identifier("nova_structures", "chests/illager_hideout_random"),
        Identifier("nova_structures", "chests/illager_hideout_trash"),
        Identifier("nova_structures", "chests/firewatch_tower"),
        Identifier("nova_structures", "chests/mining_supply"),
        Identifier("nova_structures", "chests/illager_hideout_utility"),
        Identifier("nova_structures", "chests/dungeon_3"),
        Identifier("nova_structures", "chests/water_supply"),
        Identifier("nova_structures", "chests/tavern_quest"),
        Identifier("nova_structures", "chests/dungeon_6"),
        Identifier("nova_structures", "chests/desert_ruins/desert_ruin_grave"),
        Identifier("nova_structures", "chests/desert_ruins/desert_ruin_lesser_treasure"),
        Identifier("nova_structures", "chests/desert_ruins/desert_ruin_main_temple"),
        Identifier("nova_structures", "chests/desert_ruins/desert_ruin_house"),
        Identifier("nova_structures", "chests/hamlet/hamlet_tresure"),
        Identifier("nova_structures", "chests/hamlet/hamlet"),
        Identifier("nova_structures", "chests/conduit_ruin/conduit_ruin_main"),
        Identifier("nova_structures", "chests/conduit_ruin/conduit_ruin_small"),
        Identifier("nova_structures", "chests/conduit_ruin/conduit_ruin_big"),
        Identifier("nova_structures", "chests/creeping_crypt/crypt_grave"),
        Identifier("nova_structures", "chests/creeping_crypt/crypt_hallway"),
        Identifier("nova_structures", "chests/illager_hideout_meat"),
        Identifier("nova_structures", "chests/illager_hideout_weaponry"),
        Identifier("nova_structures", "chests/dungeon_7"),
        Identifier("nova_structures", "chests/illager_hideout_heart_loot"),
        Identifier("nova_structures", "chests/illager_hideout_brewing"),
        Identifier("nova_structures", "chests/illager_hideout_library"),
        Identifier("nova_structures", "chests/illager_hideout_vegitarian"),
        Identifier("nova_structures", "chests/illager_hideout_raw_ores"),
        Identifier("nova_structures", "chests/badland_miner_outpost"),
        Identifier("nova_structures", "chests/undead_crypts_grave"),
        Identifier("nova_structures", "chests/dungeon_2"),
        Identifier("nova_structures", "chests/dungeon_5"),
        Identifier("nova_structures", "chests/nether_port/nether_port_supplies"),
        Identifier("nova_structures", "chests/nether_port/nether_port_chest"),
        Identifier("nova_structures", "chests/badland_miner_outpost_towers"),
        Identifier("nova_structures", "chests/pillager_outpost_kitchen"),
        Identifier("nova_structures", "chests/jungle_ruins/jungle_ruins_main_temple"),
        Identifier("nova_structures", "chests/food_supply"),
        Identifier("nova_structures", "chests/badland_miner_outpost_forge"),
        Identifier("nova_structures", "chests/pillager_outpost_treasure"),
        Identifier("nova_structures", "chests/illager_hideout_tresure"),
        Identifier("nova_structures", "chests/ruin_loot_master"),
        Identifier("nova_structures", "chests/witch_villa/music_room"),
        Identifier("nova_structures", "chests/witch_villa/bedroom"),
        Identifier("nova_structures", "chests/witch_villa/kitchen"),
        Identifier("nova_structures", "chests/witch_villa/pumpkin_room"),
        Identifier("nova_structures", "chests/witch_villa/general_storage"),
        Identifier("nova_structures", "chests/witch_villa/slime_room"),
        Identifier("nova_structures", "chests/witch_villa/dirty_clothes"),
        Identifier("nova_structures", "chests/witch_villa/lab"),
        Identifier("nova_structures", "chests/witch_villa/potion_brewing"),
        Identifier("nova_structures", "chests/witch_villa/gardening"),
        Identifier("nova_structures", "chests/witch_villa/library"),
        Identifier("nova_structures", "chests/nether_skeleton_tower/skeleton_tower_chest"),
        Identifier("nova_structures", "chests/nether_skeleton_tower/skeleton_tower_camp"),
        Identifier("nova_structures", "chests/nether_skeleton_tower/skeleton_tower_supply"),
        Identifier("nova_structures", "chests/dungeon_4"),
        Identifier("nova_structures", "chests/tavern_quest/badlands_miner_outpost"),
        Identifier("nova_structures", "chests/tavern_quest/undead_crypt"),
        Identifier("nova_structures", "chests/tavern_quest/ruin_town"),
        Identifier("nova_structures", "chests/tavern_quest/stray_fort"),
        Identifier("nova_structures", "chests/tavern_quest/illager_manor"),
        Identifier("nova_structures", "chests/tavern_quest/illager_hideout"),
        Identifier("nova_structures", "chests/tavern_quest/jungle_ruin"),
        Identifier("nova_structures", "chests/tavern_quest/creeping_crypt"),
        Identifier("nova_structures", "chests/tavern_quest/ancient_city"),
        Identifier("nova_structures", "chests/tavern_quest/desert_ruin"),
        Identifier("nova_structures", "chests/tavern_quest/witch_villa"),
        Identifier("nova_structures", "chests/tavern_quest/conduit_ruin"),
        Identifier("nova_structures", "chests/piglin_outstation/outstation_generic"),
        Identifier("nova_structures", "chests/piglin_outstation/outstation_treasure"),
        Identifier("nova_structures", "chests/piglin_outstation/outstation_tower"),
        Identifier("nova_structures", "chests/illager_camp"),
        Identifier("nova_structures", "chests/stray_fort_tresure"),
        Identifier("nova_structures", "chests/dungeon_1"),
        Identifier("nova_structures", "chests/stray_fort_loot_generic"),
        Identifier("nova_structures", "chests/mangrove_witchhud"),
        Identifier("nova_structures", "chests/illager_hideout_fishing"),
        Identifier("nova_structures", "chests/stray_fort_archer"),
        Identifier("nova_structures", "chests/illager_hideout_lesser_tresure"),
        Identifier("betterstrongholds", "chests/library_md"),
        Identifier("betterstrongholds", "chests/prison_lg"),
        Identifier("betterstrongholds", "chests/grand_library"),
        Identifier("betterstrongholds", "chests/common"),
        Identifier("betterstrongholds", "chests/trap"),
        Identifier("betterstrongholds", "chests/crypt"),
        Identifier("betterstrongholds", "chests/treasure"),
        Identifier("betterstrongholds", "chests/armoury"),
        Identifier("betterstrongholds", "chests/mess"),
    )

    LootTableEvents.MODIFY.register {
        resourceManager, lootManager, id, tableBuilder, source ->
        if (!source.isBuiltin || id !in locations) return@register

        tableBuilder.pool {
            item(SoulOriginsItems.BURST_SHARD) {
                count(1)
                weight(1)
            }
            item(SoulOriginsItems.TAILWIND_SHARD) {
                count(1)
                weight(1)
            }
            item(SoulOriginsItems.UPDRAFT_SHARD) {
                count(1)
                weight(1)
            }

            conditions {
                randomChance(0.2f)
            }
        }
    }
}
