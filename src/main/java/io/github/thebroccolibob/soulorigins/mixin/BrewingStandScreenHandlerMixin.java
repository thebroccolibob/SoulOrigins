package io.github.thebroccolibob.soulorigins.mixin;

import io.github.thebroccolibob.soulorigins.IngredientSlotProvider;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BrewingStandScreenHandler.class)
public class BrewingStandScreenHandlerMixin implements IngredientSlotProvider {
    @ModifyArg(
            method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/screen/PropertyDelegate;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/BrewingStandScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 3)
    )
    private Slot replaceWithProvider(Slot par1) {
        return this.replaceIngredientSlot(par1);
    }
}
