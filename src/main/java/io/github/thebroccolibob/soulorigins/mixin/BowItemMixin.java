package io.github.thebroccolibob.soulorigins.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.thebroccolibob.soulorigins.effect.PrecisionEffect;
import io.github.thebroccolibob.soulorigins.power.BowSpeedPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BowItem.class)
public class BowItemMixin {
    @WrapOperation(
            method = "onStoppedUsing",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BowItem;getPullProgress(I)F")
    )
    private float applyBowSpeedModifier(int useTicks, Operation<Float> original, @Local(argsOnly = true) LivingEntity user) {
        return Math.min(1, original.call(BowSpeedPower.applyMultiplier(user, useTicks)));
    }

    @ModifyArg(
            method = "onStoppedUsing",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V"),
            index = 5
    )
    private float applyPrecisionModifier(float par2, @Local(argsOnly = true) LivingEntity user) {
        return PrecisionEffect.modify(user, par2);
    }
}
