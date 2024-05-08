package io.github.thebroccolibob.soulorigins.datagen.power.wind

import io.github.thebroccolibob.soulorigins.datagen.lib.JsonObject
import io.github.thebroccolibob.soulorigins.datagen.lib.listOfJson
import io.github.thebroccolibob.soulorigins.datagen.power.LeveledCooldownEntry
import io.github.thebroccolibob.soulorigins.datagen.power.levelAction
import io.github.thebroccolibob.soulorigins.datagen.power.leveledMultiCooldown

private data class KeyDir(val key: String, val axis: String, val invert: Boolean)

private val keyDirs = listOf(
    KeyDir("forward", "z", false),
    KeyDir("back", "z", true),
    KeyDir("left", "x", false),
    KeyDir("right", "x", true)
)

data class TailwindEntry(override val level: Int, val strength: Double, override val recharge: Int, override val charges: Int) :
    LeveledCooldownEntry

val tailwindEntries = listOf(
    TailwindEntry(1, 1.0, 200, 1),
    TailwindEntry(2, 1.0, 140, 1),
    TailwindEntry(3, 1.0, 140, 2),
    TailwindEntry(4, 1.5, 100, 2),
    TailwindEntry(5, 2.0, 100, 3),
)

private val advancement = { (lvl, _, _, _): TailwindEntry -> "soul-origins:wind/tailwind/lvl$lvl" }

fun tailwind(id: String) = leveledMultiCooldown(id, tailwindEntries, "key.origins.secondary_active", advancement,
    otherConditions = listOf(JsonObject {
        "type" to "origins:or"
        "conditions" to keyDirs.map { (key, _, _) -> JsonObject {
            "type" to "apugli:key_pressed"
            "key" to {
                "key" to "key.$key"
                "continuous" to true
            }
        } }
    }),
    otherActions = keyDirs.map { (key, axis, invert) ->
        JsonObject {
            "type" to "origins:if_else"
            "condition" to {
                "type" to "apugli:key_pressed"
                "key" to {
                    "key" to "key.$key"
                    "continuous" to true
                }
            }
            "if_action" to levelAction(tailwindEntries, advancement) { (_, strength, _, _) ->
                "type" to "apoli:add_velocity"
                "client" to true
                "space" to "local_horizontal_normalized"
                axis to (if (invert) -1 else 1) * strength
            }
        }
    } + listOfJson(
        {
            "type" to "origins:grant_power"
            "power" to "soul-origins:wind/dash_active"
            "source" to "soul-origins:toggle"
        },
        {
            "type" to "origins:delay"
            "ticks" to 10
            "action" to {
                "type" to "origins:revoke_power"
                "power" to "soul-origins:wind/dash_active"
                "source" to "soul-origins:toggle"
            }
        },
        {
            "type" to "origins:play_sound"
            "category" to "players"
            "sound" to "minecraft:entity.player.attack.sweep"
        },
        {
            "type" to "origins:spawn_particles"
            "count" to 16
            "particle" to "minecraft:cloud"
            "speed" to 0.05
        },
    ),
    hudRender = {
        "sprite_location" to "origins:textures/gui/community/spiderkolo/resource_bar_03.png"
        "bar_index" to 23
    }
)
