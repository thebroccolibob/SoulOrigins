package io.github.thebroccolibob.soulorigins.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.thebroccolibob.soulorigins.power.BowSpeedPower;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModelPredicateProviderRegistry.class)
public class ModelPredicateProviderRegistryMixin {
    @ModifyReturnValue(
            method = "method_27890",
            at = @At("RETURN")
    )
    private static float applyMultiplier(float original, @Local(argsOnly = true) LivingEntity entity) {
        return (float) (BowSpeedPower.getMultiplier(entity) * original);
    }
}
