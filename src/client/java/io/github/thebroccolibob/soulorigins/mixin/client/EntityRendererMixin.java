package io.github.thebroccolibob.soulorigins.mixin.client;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.thebroccolibob.soulorigins.power.HideNamePower;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(
            method = "renderLabelIfPresent",
            at = @At("HEAD"),
            cancellable = true
    )
    private <T extends Entity> void hideNameIfPowered(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (PowerHolderComponent.hasPower(entity, HideNamePower.class)) {
            ci.cancel();
        }
    }
}
