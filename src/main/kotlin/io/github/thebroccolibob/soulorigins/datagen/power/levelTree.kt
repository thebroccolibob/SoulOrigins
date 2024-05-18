package io.github.thebroccolibob.soulorigins.datagen.power

import com.google.gson.JsonObject
import io.github.thebroccolibob.soulorigins.datagen.lib.JsonObject
import io.github.thebroccolibob.soulorigins.datagen.lib.JsonObjectBuilder
import io.github.thebroccolibob.soulorigins.forEachWithNext
import io.github.thebroccolibob.soulorigins.mapWithNext

fun andCondition(conditions: List<JsonObject>): JsonObject {
    if (conditions.size == 1) {
        return conditions[0]
    }
    return JsonObject {
        "type" to "origins:and"
        "conditions" to conditions
    }
}

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

fun <T> levelCondition(advancement: (T) -> String, level: T, nextLevel: T?): List<JsonObject> {
    return listOf(
        JsonObject {
            "type" to "origins:advancement"
            "advancement" to advancement(level)
        },
    ) + if (nextLevel == null) emptyList() else listOf(JsonObject {
        "type" to "origins:advancement"
        "advancement" to advancement(nextLevel)
        "inverted" to true
    })
}

fun levelCondition(levels: Int, advancement: (Int) -> String, condition: JsonObjectBuilder.(Int) -> Unit) = levelCondition(1..levels, advancement, condition)

fun <T> levelCondition(levels: Iterable<T>, advancement: (T) -> String, condition: JsonObjectBuilder.(T) -> Unit) = JsonObject {
    "type" to "origins:or"
    "conditions" to levels.mapWithNext { level, nextLevel -> JsonObject {
        "type" to "origins:and"
        "conditions" to listOf(JsonObjectBuilder().apply { condition(level) }.jsonObject) + levelCondition(advancement, level, nextLevel)
    }}
}

fun JsonObjectBuilder.levelMultiplePowers(levels: Int, name: (Int) -> String, advancement: (Int) -> String, power: JsonObjectBuilder.(Int) -> Unit) = levelMultiplePowers(1..levels, name, advancement, power)

fun <T> JsonObjectBuilder.levelMultiplePowers(levels: Iterable<T>, name: (T) -> String, advancement: (T) -> String, power: JsonObjectBuilder.(T) -> Unit) {
    levels.forEachWithNext { level, nextLevel ->
        name(level) to JsonObject {
            power(level)
            "condition" to andCondition(levelCondition(advancement, level, nextLevel))
        }
    }
}
