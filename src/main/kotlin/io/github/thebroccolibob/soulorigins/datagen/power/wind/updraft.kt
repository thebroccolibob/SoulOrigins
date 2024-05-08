package io.github.thebroccolibob.soulorigins.datagen.power.wind

import io.github.thebroccolibob.soulorigins.datagen.lib.JsonObject
import io.github.thebroccolibob.soulorigins.datagen.lib.listOfJson
import io.github.thebroccolibob.soulorigins.datagen.power.LeveledCooldownEntry
import io.github.thebroccolibob.soulorigins.datagen.power.levelAction
import io.github.thebroccolibob.soulorigins.datagen.power.leveledMultiCooldown

data class UpdraftEntry(override val level: Int, val strength: Double, override val recharge: Int, override val charges: Int):
    LeveledCooldownEntry

val updraftEntries = listOf(
    UpdraftEntry(1, 0.7, 400, 1),
    UpdraftEntry(2, 0.85, 300, 1),
    UpdraftEntry(3, 0.85, 300, 2),
    UpdraftEntry(4, 1.0, 200, 2),
    UpdraftEntry(5, 1.15, 140, 2),
)

private val advancement = { (lvl, _, _, _): UpdraftEntry -> "soul-origins:wind/updraft/lvl$lvl" }

fun updraft(id: String) = leveledMultiCooldown(
    id, updraftEntries, "key.origins.primary_active", advancement,
    otherConditions = listOfJson({
        "type" to "origins:sneaking"
        "inverted" to true
    }),
    otherActions = listOf(
        levelAction(updraftEntries, advancement) {
            "type" to "apoli:add_velocity"
            "client" to true
            "space" to "world"
            "y" to it.strength
        },
        JsonObject {
            "type" to "origins:play_sound"
            "category" to "players"
            "sound" to "minecraft:entity.player.attack.sweep"
        },
        JsonObject {
            "type" to "origins:spawn_particles"
            "count" to 16
            "particle" to "minecraft:cloud"
            "speed" to 0.05
        }
    ),
    hudRender = {
        "sprite_location" to "origins:textures/gui/community/huang/resource_bar_01.png"
        "bar_index" to 8
    }
)