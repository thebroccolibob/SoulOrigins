package io.github.thebroccolibob.soulorigins.datagen.power

import io.github.thebroccolibob.soulorigins.datagen.builder.JsonArray
import io.github.thebroccolibob.soulorigins.datagen.builder.JsonObject

data class KeyDir(val key: String, val axis: String, val invert: Boolean)

val keyDirs = listOf(
    KeyDir("forward", "z", false),
    KeyDir("back", "z", true),
    KeyDir("left", "x", false),
    KeyDir("right", "x", true)
)

fun windDash(id: String, strength: Double, recharge: Int, charges: Int) = JsonObject {
    "type" to "origins:multiple"
    "cooldown" to {
        "type" to "origins:cooldown"
        "cooldown" to recharge * charges
        "hud_render" to {
            "sprite_location" to "origins:textures/gui/community/spiderkolo/resource_bar_03.png"
            "bar_index" to 23
        }
    }
    "activate" to {
        "type" to "origins:active_self"
        "key" to "key.origins.secondary_active"
        "condition" to {
            "type" to "origins:and"
            "conditions".to [
                {
                    "type" to "origins:resource"
                    "resource" to "soul-origins:${id}_cooldown"
                    "comparison" to "<="
                    "compare_to" to recharge * (charges - 1)
                },
                {
                    "type" to "origins:or"
                    "conditions" to keyDirs.map { (key, _, _) ->
                        JsonObject {
                            "type" to "apugli:key_pressed"
                            "key" to {
                                "key" to "key.$key"
                                "continuous" to true
                            }
                        }
                    }
                }
            ]
        }
        "entity_action" to {
            "type" to "origins:and"
            "actions" to keyDirs.map { (key, axis, invert) ->
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
                        axis to if (invert) -strength else strength
                    }
                }
            } + JsonArray(
                {
                    "type" to "soul-origins:change_cooldown"
                    "cooldown" to "soul-origins:${id}_cooldown"
                    "change" to recharge
                },
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
}
