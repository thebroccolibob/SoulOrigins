package io.github.thebroccolibob.soulorigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.thebroccolibob.soulorigins.power.BrewingStandPower;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"))
    private void dropBrewingStandInventory(CallbackInfo ci) {
        PowerHolderComponent.getPowers(this, BrewingStandPower.class).forEach(BrewingStandPower::onDeath);
    }

}
