package io.github.thebroccolibob.soulorigins.datagen.power

import io.github.thebroccolibob.soulorigins.datagen.lib.ArbitraryResourceProvider
import io.github.thebroccolibob.soulorigins.datagen.lib.listOfJson

fun windJump(id: String, strength: Double, recharge: Int, charges: Int) = multiCooldown(
    id, "key.origins.primary_active", recharge, charges,
    otherConditions = listOfJson({
        "type" to "origins:sneaking"
        "inverted" to true
    }),
    otherActions = listOfJson(
        {
            "type" to "apoli:add_velocity"
            "client" to true
            "space" to "world"
            "y" to strength
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
)

fun ArbitraryResourceProvider.Writer.jsonWindJump(level: Int, strength: Double, recharge: Int, charges: Int) {
    val path = "wind/jump/lvl$level"
    json(path, windJump(path, strength, recharge, charges))
}
