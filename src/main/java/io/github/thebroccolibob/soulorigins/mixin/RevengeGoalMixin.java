package io.github.thebroccolibob.soulorigins.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.thebroccolibob.soulorigins.cca.OwnerComponent;
import io.github.thebroccolibob.soulorigins.power.DisengagePower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RevengeGoal.class)
public abstract class RevengeGoalMixin extends TrackTargetGoal {
    public RevengeGoalMixin(MobEntity mob, boolean checkVisibility) {
        super(mob, checkVisibility);
    }

    @WrapOperation(
            method = "canStart",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/RevengeGoal;canTrack(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/ai/TargetPredicate;)Z")
    )
    private boolean preventTeamAggro(RevengeGoal instance, LivingEntity livingEntity, TargetPredicate targetPredicate, Operation<Boolean> original) {
        if (livingEntity instanceof MobEntity targetEntity && OwnerComponent.getOwner(mob) == OwnerComponent.getOwner(targetEntity))
            return false;

        return original.call(instance, livingEntity, targetPredicate);
    }

    @Override
    public boolean shouldContinue() {
        if (DisengagePower.shouldMobForget(mob)) return false;

        return super.shouldContinue();
    }
}
