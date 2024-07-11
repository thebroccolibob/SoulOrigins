package io.github.thebroccolibob.soulorigins.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.thebroccolibob.soulorigins.SoulOriginsTags;
import net.minecraft.item.ItemGroups;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemGroups.class)
public class ItemGroupsMixin {
    @WrapOperation(
            method = "method_48944",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/entry/RegistryEntry$Reference;matchesKey(Lnet/minecraft/registry/RegistryKey;)Z")
    )
    private static boolean preventSecretPotions(RegistryEntry.Reference<Potion> instance, RegistryKey<Potion> key, Operation<Boolean> original) {
        return original.call(instance, key) || instance.isIn(SoulOriginsTags.SECRET_POTIONS);
    }
}
