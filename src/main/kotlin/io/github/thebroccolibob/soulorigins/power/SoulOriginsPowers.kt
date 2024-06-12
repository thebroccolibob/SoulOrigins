package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.thebroccolibob.soulorigins.Soulorigins
import net.minecraft.entity.LivingEntity
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

private fun register(powerFactory: PowerFactory<*>) {
    Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.serializerId, powerFactory)
}

class UseWindShardsPower(type: PowerType<*>, entity: LivingEntity?) : Power(type, entity)
class UseMobOrbPower(type: PowerType<*>, entity: LivingEntity?) : Power(type, entity)
class GardenWalker(type: PowerType<*>, entity: LivingEntity?) : Power(type, entity)


fun registerSoulOriginsPowers() {
    register(BowSpeedPower.factory)
    register(EmissiveOverlayPower.factory)
    register(DisengagePower.factory)
    register(Power.createSimpleFactory(::UseWindShardsPower, Identifier(Soulorigins.MOD_ID, "use_wind_shards")))
    register(Power.createSimpleFactory(::UseMobOrbPower, Identifier(Soulorigins.MOD_ID, "use_mob_orb")))
    register(Power.createSimpleFactory(::GardenWalker, Identifier(Soulorigins.MOD_ID, "walk_on_sculk_garden")))
    register(BrewingStandPower.createFactory())
    register(SuspiciousBrewingStandPower.createFactory())

}
