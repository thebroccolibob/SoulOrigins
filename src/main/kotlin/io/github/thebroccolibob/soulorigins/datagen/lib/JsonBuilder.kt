package io.github.thebroccolibob.soulorigins.datagen.lib

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject

typealias JsonInit = JsonObjectBuilder.() -> Unit

class JsonObjectBuilder(val jsonObject: JsonObject = JsonObject()) {
    @Deprecated("Warning: Second value is not a JSON element.")
    infix fun String.to (other: Any) {}

    infix fun String.to(value: String) {
        jsonObject.addProperty(this, value)
    }

    infix fun String.to(value: Number) {
        jsonObject.addProperty(this, value)
    }

    infix fun String.to(value: Boolean) {
        jsonObject.addProperty(this, value)
    }

    infix fun String.to(value: JsonElement) {
        jsonObject.add(this, value)
    }

    infix fun String.to(value: JsonArray) {
        jsonObject.add(this, value)
    }

    infix fun String.to(value: Nothing?) {
        jsonObject.add(this, JsonNull.INSTANCE)
    }

    inline infix fun String.to(valueInit: JsonInit) {
        to(JsonObject(valueInit))
    }

    infix fun String.to(array: Iterable<JsonElement>) {
        this to array.toJson()
    }

    @JvmName("toStringArray")
    infix fun String.to(array: Iterable<String>) {
        this to array.toJson()
    }

    @JvmName("toNumberArray")
    infix fun String.to(array: Iterable<Number>) {
        this to array.toJson()
    }

    @JvmName("toBooleanArray")
    infix fun String.to(array: Iterable<Boolean>) {
        this to array.toJson()
    }

    val String.to
        get() = ArrayBuilder(this@JsonObjectBuilder, this)

    class ArrayBuilder(private val parent: JsonObjectBuilder, private val property: String) {
        operator fun get(vararg values: JsonElement) {
            parent.apply {
                property to JsonArray().apply {
                    values.forEach(::add)
                }
            }
        }

        operator fun get(vararg values: String) {
            parent.apply {
                property to JsonArray().apply {
                    values.forEach(::add)
                }
            }
        }

        operator fun get(vararg values: Number) {
            parent.apply {
                property to JsonArray().apply {
                    values.forEach(::add)
                }
            }
        }

        operator fun get(vararg values: Boolean) {
            parent.apply {
                property to JsonArray().apply {
                    values.forEach(::add)
                }
            }
        }

        operator fun get(vararg values: JsonObjectBuilder.() -> Unit) {
            get(*values.map(::JsonObject).toTypedArray())
        }
    }
}

inline fun JsonObject(init: JsonObjectBuilder.() -> Unit): JsonObject {
    return JsonObjectBuilder().apply(init).jsonObject
}

fun JsonArray(vararg jsonInit: JsonObjectBuilder.() -> Unit) = jsonInit.map(::JsonObject).toJson()

fun JsonArray(vararg elements: JsonElement) = elements.asIterable().toJson()
fun JsonArray(vararg elements: String) = elements.asIterable().toJson()
fun JsonArray(vararg elements: Number) = elements.asIterable().toJson()
fun JsonArray(vararg elements: Boolean) = elements.asIterable().toJson()

fun Iterable<JsonElement>.toJson() = JsonArray().apply {
    this@toJson.forEach(::add)
}

@JvmName("toJsonStrings")
fun Iterable<String>.toJson() = JsonArray().apply {
    this@toJson.forEach(::add)
}

@JvmName("toJsonNumbers")
fun Iterable<Number>.toJson() = JsonArray().apply {
    this@toJson.forEach(::add)
}

@JvmName("toJsonBooleans")
fun Iterable<Boolean>.toJson() = JsonArray().apply {
    this@toJson.forEach(::add)
}

fun listOfJson(vararg jsonInit: JsonInit) = jsonInit.map(::JsonObject)
