package io.github.thebroccolibob.soulorigins.mixin;

import io.github.thebroccolibob.soulorigins.potion.ColoredEmptyPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionUtil.class)
public abstract class PotionUtilMixin {
    @Inject(
            method = "getColor(Lnet/minecraft/item/ItemStack;)I",
            at = @At("TAIL"),
            cancellable = true)
    private static void checkCustomColorStack(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (PotionUtil.getPotion(stack) instanceof ColoredEmptyPotion colored) {
            cir.setReturnValue(colored.getCustomColor());
        }
    }

    @Inject(
            method = "getColor(Lnet/minecraft/potion/Potion;)I",
            at = @At("HEAD"),
            cancellable = true)
    private static void checkCustomColorPotion(Potion potion, CallbackInfoReturnable<Integer> cir) {
        if (potion instanceof ColoredEmptyPotion colored) cir.setReturnValue(colored.getCustomColor());
    }
}
