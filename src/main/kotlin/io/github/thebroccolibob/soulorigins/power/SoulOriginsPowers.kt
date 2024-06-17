package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.thebroccolibob.soulorigins.SoulOrigins
import net.minecraft.entity.LivingEntity
import net.minecraft.registry.Registry

private fun register(powerFactory: PowerFactory<*>) {
    Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.serializerId, powerFactory)
}

class UseWindShardsPower(type: PowerType<*>, entity: LivingEntity?) : Power(type, entity)
class UseMobOrbPower(type: PowerType<*>, entity: LivingEntity?) : Power(type, entity)
class GardenWalker(type: PowerType<*>, entity: LivingEntity?) : Power(type, entity)
class DrowningPower(type: PowerType<*>, entity: LivingEntity?): Power(type, entity)

fun registerSoulOriginsPowers() {
    register(BowSpeedPower.factory)
    register(EmissiveOverlayPower.factory)
    register(DisengagePower.factory)
    register(Power.createSimpleFactory(::UseWindShardsPower, SoulOrigins.id("use_wind_shards")))
    register(Power.createSimpleFactory(::UseMobOrbPower, SoulOrigins.id("use_mob_orb")))
    register(Power.createSimpleFactory(::GardenWalker, SoulOrigins.id("walk_on_sculk_garden")))
    register(Power.createSimpleFactory(::DrowningPower, SoulOrigins.id("drowning")))
    register(BrewingStandPower.factory)
    register(SuspiciousBrewingStandPower.factory)
    register(EntityStorePower.factory)
}
