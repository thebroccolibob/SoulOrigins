package io.github.thebroccolibob.soulorigins.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.thebroccolibob.soulorigins.effect.PerceptionEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    @ModifyReturnValue(
            method = "hasOutline",
            at = @At("RETURN")
    )
    boolean checkPerceptionEffect(boolean original, @Local(argsOnly = true) Entity entity) {
        return original || player != null && entity != player && PerceptionEffect.shouldGlow(player, entity);
    }
}
