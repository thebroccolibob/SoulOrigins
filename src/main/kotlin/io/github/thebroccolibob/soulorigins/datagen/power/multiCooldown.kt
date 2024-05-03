package io.github.thebroccolibob.soulorigins.datagen.power

import com.google.gson.JsonObject
import io.github.thebroccolibob.soulorigins.datagen.builder.JsonObject

fun multiCooldown(id: String, recharge: Int, charges: Int, otherConditions: Iterable<JsonObject> = emptyList(), otherActions: Iterable<JsonObject> = emptyList()) = JsonObject {
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
            "conditions" to
                listOf(JsonObject {
                    "type" to "origins:resource"
                    "resource" to "soul-origins:${id}_cooldown"
                    "comparison" to "<="
                    "compare_to" to recharge * (charges - 1)
                }) + otherConditions
        }
        "entity_action" to {
            "type" to "origins:and"
            "actions" to otherActions + listOf(
                JsonObject {
                    "type" to "soul-origins:change_cooldown"
                    "cooldown" to "soul-origins:${id}_cooldown"
                    "change" to recharge
                },
            )
        }
    }
}

