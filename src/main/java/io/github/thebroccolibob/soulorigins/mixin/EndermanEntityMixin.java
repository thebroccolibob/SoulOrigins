package io.github.thebroccolibob.soulorigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.thebroccolibob.soulorigins.power.PreventEndermanAngerPower;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin {
    @Inject(
            method = "isPlayerStaring",
            at = @At("HEAD"),
            cancellable = true
    )
    void checkPreventAngerPower(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (PowerHolderComponent.hasPower(player, PreventEndermanAngerPower.class)) {
            cir.setReturnValue(false);
        }
    }
}
