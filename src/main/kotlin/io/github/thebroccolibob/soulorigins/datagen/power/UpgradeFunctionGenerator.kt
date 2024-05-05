package io.github.thebroccolibob.soulorigins.datagen.power

import io.github.thebroccolibob.soulorigins.datagen.lib.ArbitraryResourceProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput

class UpgradeFunctionGenerator(dataOutput: FabricDataOutput) : ArbitraryResourceProvider(dataOutput, "functions", "Upgrade Functions") {
    private fun Writer.windLevels(type: String, count: Int) {
        for (lvl in 2..count) {
            function("powers/wind/upgrade/$type/lvl$lvl", """
                power revoke @s soul-origins:wind/$type/lvl${lvl-1} soul-origins:wind_spirit
                power grant @s soul-origins:wind/$type/lvl$lvl soul-origins:wind_spirit
            """.trimIndent())
        }
    }

    override fun Writer.generateResources() {
        windLevels("jump", 5)
        windLevels("dash", 5)
    }
}
