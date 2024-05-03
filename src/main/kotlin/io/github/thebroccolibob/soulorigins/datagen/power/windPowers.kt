package io.github.thebroccolibob.soulorigins.datagen.power

import io.github.thebroccolibob.soulorigins.datagen.builder.JsonObject
import io.github.thebroccolibob.soulorigins.datagen.builder.listOfJson

data class KeyDir(val key: String, val axis: String, val invert: Boolean)

val keyDirs = listOf(
    KeyDir("forward", "z", false),
    KeyDir("back", "z", true),
    KeyDir("left", "x", false),
    KeyDir("right", "x", true)
)

fun windDash(id: String, strength: Double, recharge: Int, charges: Int) = multiCooldown(
    id, recharge, charges,
    otherConditions = listOfJson ({
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
    }),
    otherActions = listOfJson(
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
    ) + keyDirs.map { (key, axis, invert) ->
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
    }
)

