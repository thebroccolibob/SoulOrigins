package io.github.thebroccolibob.soulorigins.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.thebroccolibob.soulorigins.power.DrowningPower;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @WrapOperation(
            method = "baseTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z")
    )
    private boolean checkDrowningPower(LivingEntity instance, TagKey<Fluid> tagKey, Operation<Boolean> original, @Share("hasDrowningPower") LocalBooleanRef hasPowerRef) {
        if (original.call(instance, tagKey)) return true;

        boolean hasPower = PowerHolderComponent.hasPower(instance, DrowningPower.class);

        hasPowerRef.set(hasPower);
        return hasPower;
    }

    @WrapOperation(
            method = "baseTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z")
    )
    private boolean bypassSecondCheck(BlockState instance, Block block, Operation<Boolean> original, @Share("hasDrowningPower") LocalBooleanRef hasPowerRef) {
        return original.call(instance, block) && !hasPowerRef.get();
    }
}
