package io.github.thebroccolibob.soulorigins.datagen.lib

import com.google.common.hash.Hashing
import com.google.common.hash.HashingOutputStream
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.util.Util
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

fun writePlaintextToPath(writer: DataWriter, text: String, path: Path): CompletableFuture<*> {
    return CompletableFuture.runAsync({
        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val hashingOutputStream = HashingOutputStream(Hashing.sha1(), byteArrayOutputStream)
            val streamWriter = OutputStreamWriter(
                hashingOutputStream,
                StandardCharsets.UTF_8
            )

            streamWriter.write(text)
            streamWriter.close()
            writer.write(path, byteArrayOutputStream.toByteArray(), hashingOutputStream.hash())
        } catch (var10: IOException) {
            DataProvider.LOGGER.error("Failed to save file to {}", path, var10)
        }
    }, Util.getMainWorkerExecutor())
}
