package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.thebroccolibob.soulorigins.SoulOrigins
import net.minecraft.entity.LivingEntity
import net.minecraft.registry.Registry
import java.util.function.BiFunction

private fun register(powerFactory: PowerFactory<*>) {
    Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.serializerId, powerFactory)
}

fun register(path: String, factory: BiFunction<PowerType<*>, LivingEntity?, Power>) {
    register(Power.createSimpleFactory(factory, SoulOrigins.id(path)))
}

class UseWindShardsPower(type: PowerType<*>, entity: LivingEntity?) : Power(type, entity)
class UseMobOrbPower(type: PowerType<*>, entity: LivingEntity?) : Power(type, entity)
class GardenWalker(type: PowerType<*>, entity: LivingEntity?) : Power(type, entity)
class DrowningPower(type: PowerType<*>, entity: LivingEntity?): Power(type, entity)
class HideNamePower(type: PowerType<*>, entity: LivingEntity?): Power(type, entity)
class PreventEndermanAngerPower(type: PowerType<*>, entity: LivingEntity?): Power(type, entity)
class CraftBeeBombPower(type: PowerType<*>, entity: LivingEntity?): Power(type, entity)

fun registerSoulOriginsPowers() {
    register(BowSpeedPower.factory)
    register(EmissiveOverlayPower.factory)
    register(DisengagePower.factory)
    register("use_wind_shards", ::UseWindShardsPower)
    register("use_mob_orb", ::UseMobOrbPower)
    register("walk_on_sculk_garden", ::GardenWalker)
    register("drowning", ::DrowningPower)
    register("hide_name", ::HideNamePower)
    register("prevent_enderman_anger", ::PreventEndermanAngerPower)
    register("craft_bee_bomb", ::CraftBeeBombPower)
    register(BrewingStandPower.factory)
    register(SuspiciousBrewingStandPower.factory)
    register(EntityStorePower.factory)
    register(ActionOnRestrictedRecipePower.factory)
    register(ActionOnDeathPower.factory)
}
