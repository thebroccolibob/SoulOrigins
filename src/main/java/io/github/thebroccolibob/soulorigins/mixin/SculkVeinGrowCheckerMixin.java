package io.github.thebroccolibob.soulorigins.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.block.SculkVeinBlock$SculkVeinGrowChecker")
public class SculkVeinGrowCheckerMixin {
    @WrapOperation(
            method = "canGrow(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/block/BlockState;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z", ordinal = 0)
    )
    private boolean fix(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) || original.call(instance, SoulOriginsBlocks.GARDEN_SCULK);
    }
}
