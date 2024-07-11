package io.github.thebroccolibob.soulorigins.mixin;

import io.github.thebroccolibob.soulorigins.SoulOriginsDamage;
import io.github.thebroccolibob.soulorigins.effect.SoulOriginsEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DamageTracker.class)
public class DamageTrackerMixin {
    @Shadow @Final private LivingEntity entity;

    @ModifyVariable(
            method = "getDeathMessage",
            at = @At("STORE"),
            ordinal = 0
    )
    DamageSource checkMementoMori(DamageSource value) {
        if (!entity.hasStatusEffect(SoulOriginsEffects.MEMENTO_MORI)) return value;

        return new DamageSource(
                SoulOriginsDamage.of(
                        value.getAttacker() == null ? SoulOriginsDamage.MEMENTO_MORI_PASSIVE : SoulOriginsDamage.MEMENTO_MORI_ACTIVE,
                        entity.getWorld()
                ),
                value.getSource(),
                value.getAttacker()
        );
    }
}
