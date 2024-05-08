package io.github.thebroccolibob.soulorigins.datagen.power

import io.github.thebroccolibob.soulorigins.datagen.lib.JsonObject
import io.github.thebroccolibob.soulorigins.datagen.lib.JsonObjectBuilder
import io.github.thebroccolibob.soulorigins.mapWithNext

fun levelAction(levels: Int, advancement: (Int) -> String, action: JsonObjectBuilder.(Int) -> Unit) = levelAction(1..levels, advancement, action)

fun <T> levelAction(levels: Iterable<T>, advancement: (T) -> String, action: JsonObjectBuilder.(T) -> Unit) = JsonObject {
    "type" to "origins:if_else_list"
    "actions" to levels.map { JsonObject {
        "condition" to {
            "type" to "origins:advancement"
            "advancement" to advancement(it)
        }
        "action" to JsonObjectBuilder().apply { action(it) }.jsonObject
    } }.reversed()
}

fun levelCondition(levels: Int, advancement: (Int) -> String, condition: JsonObjectBuilder.(Int) -> Unit) = levelCondition(1..levels, advancement, condition)

fun <T> levelCondition(levels: Iterable<T>, advancement: (T) -> String, condition: JsonObjectBuilder.(T) -> Unit) = JsonObject {
    "type" to "origins:or"
    "conditions" to levels.mapWithNext { level, nextLevel -> JsonObject {
        "type" to "origins:and"
        "conditions" to listOf(
            JsonObject {
                "type" to "origins:advancement"
                "advancement" to advancement(level)
            },
            JsonObjectBuilder().apply { condition(level) }.jsonObject
        ) + if (nextLevel == null) emptyList() else listOf(JsonObject {
            "type" to "origins:advancement"
            "advancement" to advancement(nextLevel)
            "inverted" to true
        })
    }}
}

fun JsonObjectBuilder.levelMultiplePowers(levels: Int, name: (Int) -> String, power: JsonObjectBuilder.(Int) -> Unit) = levelMultiplePowers(1..levels, name, power)

fun <T> JsonObjectBuilder.levelMultiplePowers(levels: Iterable<T>, name: (T) -> String, power: JsonObjectBuilder.(T) -> Unit) {
    for (level in levels) {
        name(level) to JsonObjectBuilder().apply { power(level) }.jsonObject
    }
}
