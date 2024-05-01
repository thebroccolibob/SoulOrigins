package io.github.thebroccolibob.soulorigins.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.util.Identifier
import java.util.*
import java.util.concurrent.CompletableFuture

abstract class ArbitraryJsonProvider(val dataOutput: FabricDataOutput, val directoryName: String, private val name: String = directoryName) : DataProvider {

    abstract fun Writer.generateJsons()

    override fun run(writer: DataWriter): CompletableFuture<*> {
        return CompletableFuture.allOf(*Writer(writer).apply { generateJsons() }.entries.toTypedArray())
    }

    override fun getName() = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    inner class Writer(val writer: DataWriter) {
        val entries = mutableListOf<CompletableFuture<*>>()

        inline fun json(path: String, jsonInit: JsonObjectBuilder.() -> Unit) {
            entries.add(DataProvider.writeToPath(writer, JsonObject(jsonInit), dataOutput.getResolver(DataOutput.OutputType.DATA_PACK, directoryName).resolveJson(Identifier(dataOutput.modId, path))))
        }
    }
}
