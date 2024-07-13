package io.github.thebroccolibob.soulorigins.mixin.client;

import io.github.thebroccolibob.soulorigins.TntEntityRendererBlockStateProvider;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.entity.TntEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TntEntityRenderer.class)
public class TntEntityRendererMixin {
    @ModifyArg(
            method = "render(Lnet/minecraft/entity/TntEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/TntMinecartEntityRenderer;renderFlashingBlock(Lnet/minecraft/client/render/block/BlockRenderManager;Lnet/minecraft/block/BlockState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IZ)V"),
            index = 1
    )
    BlockState checkCustom(BlockState original) {
        if (this instanceof TntEntityRendererBlockStateProvider provider) {
            return provider.provideCustomBlockState();
        }
        return original;
    }
}
