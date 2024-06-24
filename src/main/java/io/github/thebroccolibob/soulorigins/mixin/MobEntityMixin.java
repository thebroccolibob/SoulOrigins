package io.github.thebroccolibob.soulorigins.mixin;

import io.github.thebroccolibob.soulorigins.OwnedGoalAdder;
import io.github.thebroccolibob.soulorigins.WorldsKt;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
abstract class MobEntityMixin extends LivingEntity implements OwnedGoalAdder {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Accessor @Override public abstract GoalSelector getGoalSelector();
    @Accessor @Override public abstract GoalSelector getTargetSelector();

    @Inject(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;initGoals()V")
    )
    void addOwnedGoals(EntityType<? extends MobEntity> entityType, World world, CallbackInfo ci) {
        this.soulOrigins$addOwnedGoals();
    }

    @Inject(
            method = "checkDespawn",
            at = @At("HEAD"),
            cancellable = true)
    void checkNoMobDimension(CallbackInfo ci) {
        if (getWorld().getRegistryKey().equals(WorldsKt.WHITESPACE_DIMENSION)) {
            this.discard();
            ci.cancel();
        }
    }
}
