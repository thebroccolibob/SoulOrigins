package io.github.thebroccolibob.soulorigins.datagen.power

import io.github.thebroccolibob.soulorigins.datagen.builder.JsonArray
import io.github.thebroccolibob.soulorigins.datagen.builder.JsonObject
import io.github.thebroccolibob.soulorigins.datagen.builder.plus
import io.github.thebroccolibob.soulorigins.datagen.builder.toJson

data class KeyDir(val key: String, val axis: String, val distance: Int)

val keyDirs = listOf(
    KeyDir("forward", "z", 1),
    KeyDir("back", "z", -1),
    KeyDir("left", "x", 1),
    KeyDir("right", "x", -1)
)

fun windDash() = JsonObject {
    "type" to "origins:active_self"
    "key" to "key.origins.secondary_active"
    "cooldown" to 40
    "hud_render" to {
        "sprite_location" to "origins:textures/gui/community/spiderkolo/resource_bar_03.png"
        "bar_index" to 23
    }
    "condition" to {
        "type" to "origins:or"
        "conditions" to keyDirs.map { (key, _, _) ->
            JsonObject {
                "type" to "apugli:key_pressed"
                "key" to {
                    "key" to "key.$key"
                    "continuous" to true
                }
            }
        }.toJson()
    }
    "entity_action" to {
        "type" to "origins:and"
        "actions" to keyDirs.map { (key, axis, distance) ->
            JsonObject {
                "type" to "origins:if_else"
                "condition" to {
                    "type" to "apugli:key_pressed"
                    "key" to {
                        "key" to "key.$key"
                        "continuous" to true
                    }
                }
                "if_action" to {
                    "type" to "apoli:add_velocity"
                    "client" to true
                    "space" to "local_horizontal_normalized"
                    axis to distance
                }
            }
        }.toJson() + JsonArray(
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
            }
        )
    }
}
