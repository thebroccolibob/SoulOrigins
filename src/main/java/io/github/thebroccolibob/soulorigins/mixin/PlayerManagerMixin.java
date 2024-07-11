package io.github.thebroccolibob.soulorigins.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.thebroccolibob.soulorigins.WorldsKt;
import io.github.thebroccolibob.soulorigins.cca.WhiteSpaceComponent;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @WrapOperation(
            method = "respawnPlayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getSpawnPointPosition()Lnet/minecraft/util/math/BlockPos;")
    )
    private BlockPos checkWhitespacePosition(ServerPlayerEntity instance, Operation<BlockPos> original) {
        return WhiteSpaceComponent.isWhitespaced(instance) ? null : original.call(instance);
    }

    @ModifyVariable(
            method = "respawnPlayer",
            at = @At("STORE"),
            ordinal = 1
    )
    private ServerWorld checkWhitespaceDimension(ServerWorld value, @Local(argsOnly = true) ServerPlayerEntity player) {
        if (!WhiteSpaceComponent.isWhitespaced(player)) return value;
        @Nullable ServerWorld whitespace = value.getServer().getWorld(WorldsKt.WHITESPACE_DIMENSION);
        if (whitespace != null) return whitespace;
        return value;
    }
}
