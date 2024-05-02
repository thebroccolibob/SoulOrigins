package io.github.thebroccolibob.soulorigins.datagen.builder

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject

class JsonObjectBuilder(val jsonObject: JsonObject) {
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

    infix fun String.to(value: Nothing?) {
        jsonObject.add(this, JsonNull.INSTANCE)
    }

    inline infix fun String.to(valueInit: JsonObjectBuilder.() -> Unit) {
        to(JsonObject(valueInit))
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
    return JsonObjectBuilder(JsonObject()).apply(init).jsonObject
}
