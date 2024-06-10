package io.github.thebroccolibob.soulorigins.power

import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.Active
import io.github.apace100.apoli.power.Active.Key
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.calio.data.SerializableDataTypes
import io.github.thebroccolibob.soulorigins.SerializableData
import io.github.thebroccolibob.soulorigins.Soulorigins
import io.github.thebroccolibob.soulorigins.item.SouloriginsItems
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.potion.PotionUtil
import net.minecraft.util.Identifier
import java.util.function.BiFunction

class SuspiciousBrewingStandPower(
    type: PowerType<BrewingStandPower>,
    entity: LivingEntity?,
    key: Active.Key,
    fuelPowerType: PowerType<*>?,
    dropOnDeath: Boolean,
    recoverable: Boolean
) : BrewingStandPower(type, entity, key, fuelPowerType, dropOnDeath, recoverable) {
    override fun processResult(stack: ItemStack): ItemStack {
        if (PotionUtil.getPotion(stack).effects.isEmpty()) return stack

        val suspiciousItem = when(stack.item) {
            Items.POTION -> SouloriginsItems.SUSPICIOUS_BREW
            Items.SPLASH_POTION -> SouloriginsItems.SPLASH_SUSPICIOUS_BREW
            Items.LINGERING_POTION -> SouloriginsItems.LINGERING_SUSPICIOUS_BREW
            else -> return stack
        }

        return suspiciousItem.defaultStack.apply {
            PotionUtil.setPotion(this, PotionUtil.getPotion(stack))
        }
    }

    companion object {
        fun createFactory(): PowerFactory<BrewingStandPower> {
            return PowerFactory<BrewingStandPower>(
                Identifier(Soulorigins.MOD_ID, "suspicious_brewing_stand"),
                SerializableData {
                    add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, Key())
                    add("fuel_resource", ApoliDataTypes.POWER_TYPE, null)
                    add("drop_on_death", SerializableDataTypes.BOOLEAN, true)
                    add("recoverable", SerializableDataTypes.BOOLEAN, true)
                }
            ) { data ->
                BiFunction { powerType, livingEntity ->
                    SuspiciousBrewingStandPower(
                        powerType,
                        livingEntity,
                        data.get("key"),
                        data.get("fuel_resource"),
                        data.get("drop_on_death"),
                        data.get("recoverable")
                    )
                }
            }.allowCondition()
        }
    }
}
