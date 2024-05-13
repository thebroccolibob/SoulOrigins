package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.merchantpug.apugli.power.TextureOrUrlPower
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import java.util.function.BiFunction

class EmissiveOverlayPower(
    type: PowerType<*>,
    entity: LivingEntity?,
    textureLocation: Identifier?,
    textureUrl: String?
) : TextureOrUrlPower(type, entity, textureLocation, textureUrl) {
    companion object {
        val factory: PowerFactory<EmissiveOverlayPower> = PowerFactory(
            Identifier(Soulorigins.MOD_ID, "emissive_overlay"),
            getSerializableData()
        ) { data -> BiFunction {
                type: PowerType<EmissiveOverlayPower>, entity: LivingEntity? -> EmissiveOverlayPower(type, entity, data.getId("texture_location"), data.getString("texture_url"))
        } }
    }
}