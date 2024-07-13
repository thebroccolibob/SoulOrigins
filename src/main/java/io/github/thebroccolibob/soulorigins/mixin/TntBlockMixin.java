package io.github.thebroccolibob.soulorigins.mixin;

import io.github.thebroccolibob.soulorigins.block.BeeBombBlock;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntBlock.class)
public class TntBlockMixin {
    @Inject(
            method = "primeTnt(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/LivingEntity;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void checkBeeBomb(World world, BlockPos pos, LivingEntity igniter, CallbackInfo ci) {
        if (world.getBlockState(pos).getBlock() instanceof BeeBombBlock) {
            BeeBombBlock.explodeBomb(world, pos, igniter);
            ci.cancel();
        }
    }
}
