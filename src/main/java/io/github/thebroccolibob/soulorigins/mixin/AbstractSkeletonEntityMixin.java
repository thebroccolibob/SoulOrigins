package io.github.thebroccolibob.soulorigins.mixin;

import io.github.thebroccolibob.soulorigins.Soulorigins;
import io.github.thebroccolibob.soulorigins.entity.OwnableSkeleton;
import io.github.thebroccolibob.soulorigins.entity.ai.goal.SkeletonAttackWithOwnerGoal;
import io.github.thebroccolibob.soulorigins.entity.ai.goal.SkeletonFollowOwnerGoal;
import io.github.thebroccolibob.soulorigins.entity.ai.goal.SkeletonTrackOwnerAttackerGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends HostileEntity implements OwnableSkeleton {
    @Unique
	@SuppressWarnings("WrongEntityDataParameterClass")
	private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(AbstractSkeletonEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);;

	protected AbstractSkeletonEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		Soulorigins.logger.info("{}", OWNER_UUID);
		dataTracker.startTracking(OWNER_UUID, Optional.empty());
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if (this.soulOrigins$getOwnerUuid() != null) {
			nbt.putUuid("Owner", this.soulOrigins$getOwnerUuid());
		}
	}

	@Inject(
			method = "readCustomDataFromNbt",
			at = @At("TAIL")
	)
	private void readOwner(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.containsUuid("Owner")) {
			soulOrigins$setOwnerUuid(nbt.getUuid("Owner"));
		}
	}

	@SuppressWarnings("DataFlowIssue")
    @Inject(
			method = "initGoals",
			at = @At("TAIL")
	)
	private void injectGoals(CallbackInfo ci) {
		goalSelector.add(4, new SkeletonFollowOwnerGoal((AbstractSkeletonEntity) (Object) this, 1.0, 16f, 8f, false));
		targetSelector.add(1, new SkeletonTrackOwnerAttackerGoal((AbstractSkeletonEntity) (Object) this));
		targetSelector.add(2, new SkeletonAttackWithOwnerGoal((AbstractSkeletonEntity) (Object) this));
	}

	@Override
	public UUID soulOrigins$getOwnerUuid() {
		return dataTracker.get(OWNER_UUID).orElse(null);
	}

	@Unique
	public void soulOrigins$setOwnerUuid(@Nullable UUID uuid) {
		dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
	}
}
