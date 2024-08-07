package io.github.thebroccolibob.soulorigins.mixin;

import com.mojang.authlib.GameProfile;
import io.github.thebroccolibob.soulorigins.cca.WhiteSpaceComponent;
import io.github.thebroccolibob.soulorigins.effect.SoulOriginsEffects;
import io.github.thebroccolibob.soulorigins.power.ActionOnDeathPower;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Shadow @Final public MinecraftServer server;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void setWhitespace(DamageSource damageSource, CallbackInfo ci) {
        if (hasStatusEffect(SoulOriginsEffects.MEMENTO_MORI)) {
            WhiteSpaceComponent.setWhitespaced(this, true);
        }
    }

    @Inject(
            method = "onDeath",
            at = @At("HEAD")
    )
    private void executeOnDeathActions(DamageSource damageSource, CallbackInfo ci) {
        ActionOnDeathPower.executeAll(this);
    }
}
