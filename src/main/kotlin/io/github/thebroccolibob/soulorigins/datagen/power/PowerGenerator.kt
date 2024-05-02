package io.github.thebroccolibob.soulorigins.datagen.power

import io.github.thebroccolibob.soulorigins.datagen.builder.ArbitraryJsonProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput

class PowerGenerator(dataOutput: FabricDataOutput) : ArbitraryJsonProvider(dataOutput, "powers", "Origins Powers") {
    override fun Writer.generateJsons() {
        json("wind/dash/lvl1", windDash("wind/dash/lvl1", 1.0, 200, 1))
        json("wind/dash/lvl2", windDash("wind/dash/lvl2", 1.0, 140, 1))
        json("wind/dash/lvl3", windDash("wind/dash/lvl3", 1.0, 140, 2))
        json("wind/dash/lvl4", windDash("wind/dash/lvl4", 1.5, 100, 2))
        json("wind/dash/lvl5", windDash("wind/dash/lvl5", 1.5, 100, 3))
    }
}
