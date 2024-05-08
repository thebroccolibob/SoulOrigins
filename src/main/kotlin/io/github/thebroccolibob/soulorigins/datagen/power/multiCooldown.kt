package io.github.thebroccolibob.soulorigins.datagen.power

import com.google.gson.JsonObject
import io.github.thebroccolibob.soulorigins.datagen.lib.JsonInit
import io.github.thebroccolibob.soulorigins.datagen.lib.JsonObject

interface LeveledCooldownEntry {
    val level: Int
    val recharge: Int
    val charges: Int
}

fun <T: LeveledCooldownEntry> leveledMultiCooldown(id: String, entries: Iterable<T>, key: String, advancement: (T) -> String, otherConditions: Iterable<JsonObject> = emptyList(), otherActions: Iterable<JsonObject> = emptyList(), hudRender: JsonInit) = JsonObject {
    "type" to "origins:multiple"

    levelMultiplePowers(entries, {"cooldown${it.level}"}) {
        "type" to "origins:cooldown"
        "cooldown" to it.charges * it.recharge
        "hud_render" to hudRender
    }

    "activate" to {
        "type" to "origins:active_self"
        "key" to key
        "condition" to {
            "type" to "origins:and"
            "conditions" to listOf(levelCondition(entries, advancement) {
                "type" to "origins:resource"
                "resource" to "soul-origins:${id}_cooldown${it.level}"
                "comparison" to "<="
                "compare_to" to it.recharge * (it.charges - 1)
            }) + otherConditions
        }
        "entity_action" to {
            "type" to "origins:and"
            "actions" to otherActions + levelAction(entries, advancement) {
                "type" to "soul-origins:change_cooldown"
                "cooldown" to "soul-origins:${id}_cooldown${it.level}"
                "change" to it.recharge
            }
        }
    }
}

