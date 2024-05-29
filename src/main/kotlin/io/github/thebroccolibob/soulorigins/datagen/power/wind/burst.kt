package io.github.thebroccolibob.soulorigins.datagen.power.wind

import io.github.thebroccolibob.soulorigins.datagen.lib.JsonObject
import io.github.thebroccolibob.soulorigins.datagen.lib.listOfJson
import io.github.thebroccolibob.soulorigins.datagen.power.levelAction

private val advancement = {lvl: Int -> "soul-origins:wind/burst/lvl$lvl"}

val burst = JsonObject {
    "type" to "origins:active_self"
    "key" to "key.origins.secondary_active"
    "condition" to {
        "type" to "origins:and"
        "conditions".to [
            {
                "type" to "origins:sneaking"
            },
            {
                "type" to "origins:resource"
                "resource" to "soul-origins:soul_meter"
                "comparison" to ">="
                "compare_to" to 10
            },
            {
                "type" to "origins:advancement"
                "advancement" to advancement(1)
            }
        ]
    }
    "entity_action" to {
        "type" to "origins:and"
        "actions" to listOfJson(
            {
                "type" to "origins:play_sound"
                "sound" to "minecraft:entity.wither.ambient"
                "pitch" to 0.7
                "volume" to 0.5
            },
            {
                "type" to "origins:add_velocity"
                "space" to "local"
                "y" to 1
                "z" to -2
                "set" to true
            },
            {
                "type" to "origins:change_resource"
                "resource" to "soul-origins:soul_meter"
                "operation" to "add"
                "change" to -10
            },
            {
                "type" to "origins:area_of_effect"
                "shape" to "sphere"
                "radius" to 8
                "bientity_action" to {
                    "type" to "apoli:add_velocity"
                    "reference" to "position"
                    "z" to 2
                    "y" to 0.2
                }
            },
            {
                "type" to "origins:change_resource"
                "resource" to "soul-origins:wind/dash"
                "operation" to "set"
                "change" to 0
            },
            {
                "type" to "origins:change_resource"
                "resource" to "soul-origins:wind/jump"
                "operation" to "set"
                "change" to 0
            },
            {
                "type" to "origins:spawn_particles"
                "count" to 1
                "particle" to "soul-origins:gust_emitter_large"
                "speed" to 0
            },
            {
                "type" to "origins:if_else"
                "condition" to {
                    "type" to "origins:advancement"
                    "advancement" to advancement(2)
                }
                "if_action" to {
                    "type" to "origins:and"
                    "actions" to listOfJson(
                        {
                            "type" to "origins:grant_power"
                            "power" to "soul-origins:wind/shield"
                            "source" to "soul-origins:wind/burst"
                        },
                    ) + (1..5).flatMap { listOf("tailwind", "updraft").map { ability ->
                        JsonObject {
                            "type" to "origins:set_resource"
                            "resource" to "soul-origins:wind/${ability}_cooldown$it"
                            "value" to 400
                        }
                    } }
                }
            }
        ) + levelAction(listOf(1, 3), advancement) {
            "type" to "origins:and"
            "actions".to [
                {
                    "type" to "origins:apply_effect"
                    "effect" to {
                        "effect" to "minecraft:speed"
                        "duration" to if (it == 3) 1200 else 600
                        "is_ambient" to true
                    }
                },
                {
                    "type" to "origins:apply_effect"
                    "effect" to {
                        "effect" to "minecraft:invisibility"
                        "duration" to if (it == 3) 1200 else 600
                        "is_ambient" to true
                    }
                },
            ]
        }
    }
}

