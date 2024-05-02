package io.github.thebroccolibob.soulorigins.datagen.power

import io.github.thebroccolibob.soulorigins.datagen.builder.ArbitraryJsonProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput

class PowerGenerator(dataOutput: FabricDataOutput) : ArbitraryJsonProvider(dataOutput, "powers", "Origins Powers") {
    override fun Writer.generateJsons() {
        json("wind/dash_2", windDash())
    }
}
