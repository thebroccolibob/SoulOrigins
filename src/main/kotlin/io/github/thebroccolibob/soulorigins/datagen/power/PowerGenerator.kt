package io.github.thebroccolibob.soulorigins.datagen.power

import io.github.thebroccolibob.soulorigins.datagen.lib.ArbitraryJsonProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput

class PowerGenerator(dataOutput: FabricDataOutput) : ArbitraryJsonProvider(dataOutput, "powers", "Powers") {
    override fun Writer.generateJsons() {
        jsonWindDash(1, 1.0, 200, 1)
        jsonWindDash(2, 1.0, 140, 1)
        jsonWindDash(3, 1.0, 140, 2)
        jsonWindDash(4, 1.5, 100, 2)
        jsonWindDash(5, 2.0, 100, 3)

        jsonWindJump(1, 0.7, 400, 1)
        jsonWindJump(2, 0.85, 300, 1)
        jsonWindJump(3, 0.85, 300, 2)
        jsonWindJump(4, 1.0, 200, 2)
        jsonWindJump(5, 1.15, 140, 2)
    }
}
