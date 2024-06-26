package io.github.thebroccolibob.soulorigins.datagen.power

import com.google.gson.JsonObject
import io.github.thebroccolibob.soulorigins.datagen.lib.ArbitraryResourceProvider
import io.github.thebroccolibob.soulorigins.datagen.power.wind.burst
import io.github.thebroccolibob.soulorigins.datagen.power.wind.tailwind
import io.github.thebroccolibob.soulorigins.datagen.power.wind.updraft
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput

class PowerGenerator(dataOutput: FabricDataOutput) : ArbitraryResourceProvider(dataOutput, "powers", name = "Powers") {
    private fun Writer.json(path: String, power: (String) -> JsonObject) {
        json(path, power(path))
    }

    override fun Writer.generateResources() {
        json("wind/tailwind", ::tailwind)
        json("wind/updraft", ::updraft)
//        json("wind/neutral", neutral)
        json("wind/burst", burst)
    }
}
