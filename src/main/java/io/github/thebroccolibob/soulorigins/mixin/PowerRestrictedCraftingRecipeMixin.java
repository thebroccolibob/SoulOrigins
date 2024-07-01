package io.github.thebroccolibob.soulorigins.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.util.PowerRestrictedCraftingRecipe;
import io.github.thebroccolibob.soulorigins.power.ActionOnRestrictedRecipePower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PowerRestrictedCraftingRecipe.class)
public class PowerRestrictedCraftingRecipeMixin {
    @Inject(
            method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
            at = @At(value = "RETURN", ordinal = 0)
    )
    private void executeAction(RecipeInputInventory inventory, DynamicRegistryManager registryManager, CallbackInfoReturnable<ItemStack> cir, @Local PlayerEntity player, @Local Recipe<CraftingInventory> recipe) {
        PowerHolderComponent.getPowers(player, ActionOnRestrictedRecipePower.class).forEach(it -> it.tryExecute(recipe.getId()));
    }
}
