package io.github.thebroccolibob.soulorigins.datagen.power.wind

import io.github.thebroccolibob.soulorigins.datagen.lib.JsonObject
import io.github.thebroccolibob.soulorigins.datagen.power.levelMultiplePowers

val neutral = JsonObject {
    "type" to "origins:multiple"
    levelMultiplePowers(listOf(2, 3), {"distance$it"}, {"soul-origins:wind/neutral/lvl$it"}) {
        "type" to "soul-origins:disengage"
        "distance" to if (it == 2) 0 else 0.2
    }
}
