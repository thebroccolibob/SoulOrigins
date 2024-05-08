package io.github.thebroccolibob.soulorigins.datagen.power.wind

import io.github.thebroccolibob.soulorigins.datagen.lib.JsonObject
import io.github.thebroccolibob.soulorigins.datagen.power.levelCondition

private val condition = levelCondition(listOf(1,3), {"soul-origins:wind/tailwind/lvl$it"}) {
    "type" to "origins:health"
    "comparison" to "<="
    "compare_to" to when(it) {
        3 -> 8
        else -> 5
    }
}

val barrier = JsonObject {
    "type" to "origins:multiple"
    "swirl" to {
        "type" to "apugli:energy_swirl"
        "texture_location" to "minecraft:textures/entity/wither/wither_armor.png"
        "speed" to 0.05
        "condition" to condition
    }
    "prevent_damage" to {
        "type" to "origins:modify_damage_taken"
        "damage_condition" to {
            "type" to "origins:projectile"
        }
        "modifier" to {
            "operation" to "set_total"
            "value" to 0
        }
        "condition" to condition
    }
    "shader" to {
        "type" to "origins:shader"
        "shader" to "minecraft:shaders/post/deconverge.json"
        "condition" to condition
    }
    "redirect" to {
        "type" to "apugli:action_when_projectile_hit"
        "bientity_action" to {
            "type" to "origins:invert"
            "action" to {
                "type" to "apoli:add_velocity"
                "set" to true
                "reference" to "position"
                "z" to -6
            }
        }
        "hud_render" to {
            "should_render" to false
        }
        "condition" to condition
    }
}
