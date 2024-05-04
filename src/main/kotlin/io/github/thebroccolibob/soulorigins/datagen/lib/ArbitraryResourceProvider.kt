package io.github.thebroccolibob.soulorigins.datagen.lib

import com.google.gson.JsonObject
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.util.Identifier
import java.util.*
import java.util.concurrent.CompletableFuture

abstract class ArbitraryResourceProvider(val dataOutput: FabricDataOutput, val directoryName: String, private val name: String = directoryName) : DataProvider {

    abstract fun Writer.generateResources()

    override fun run(writer: DataWriter): CompletableFuture<*> {
        return CompletableFuture.allOf(*Writer(writer).apply { generateResources() }.entries.toTypedArray())
    }

    override fun getName() = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    inner class Writer(private val writer: DataWriter) {
        val entries = mutableListOf<CompletableFuture<*>>()

        fun json(path: String, jsonObject: JsonObject) {
            entries.add(DataProvider.writeToPath(writer, jsonObject, dataOutput.getResolver(DataOutput.OutputType.DATA_PACK, directoryName).resolveJson(Identifier(dataOutput.modId, path))))
        }

        inline fun json(path: String, jsonInit: JsonInit) {
            json(path, JsonObject(jsonInit))
        }

        fun plaintext(path: String, fileExtension: String, text: String) {
            entries.add(writePlaintextToPath(writer, text, dataOutput.getResolver(DataOutput.OutputType.DATA_PACK, directoryName).resolve(Identifier(dataOutput.modId, path), fileExtension)))
        }

        fun function(path: String, commands: String) {
            plaintext(path, "mcfunction", commands)
        }
    }
}
