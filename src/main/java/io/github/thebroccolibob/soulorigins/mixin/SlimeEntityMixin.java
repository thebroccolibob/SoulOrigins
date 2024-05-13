package io.github.thebroccolibob.soulorigins.mixin;

import io.github.thebroccolibob.soulorigins.entity.OwnableMonster;
import io.github.thebroccolibob.soulorigins.entity.ai.goal.MonsterAttackWithOwnerGoal;
import io.github.thebroccolibob.soulorigins.entity.ai.goal.MonsterFollowOwnerGoal;
import io.github.thebroccolibob.soulorigins.entity.ai.goal.MonsterTrackOwnerAttackerGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
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

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin extends MobEntity implements OwnableMonster {
    @Unique
	@SuppressWarnings("WrongEntityDataParameterClass")
	private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(SlimeEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

	protected SlimeEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(
			method = "initDataTracker",
			at = @At("TAIL")
	)
	private void injectOwnerData(CallbackInfo ci) {
		dataTracker.startTracking(OWNER_UUID, Optional.empty());
	}

	@Inject(
			method = "writeCustomDataToNbt",
			at = @At("TAIL")
	)
	private void writeOwner(NbtCompound nbt, CallbackInfo ci) {
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
		goalSelector.add(4, new MonsterFollowOwnerGoal((SlimeEntity) (Object) this, 1.0, 16f, 8f, false));
		targetSelector.add(1, new MonsterTrackOwnerAttackerGoal((SlimeEntity) (Object) this));
		targetSelector.add(2, new MonsterAttackWithOwnerGoal((SlimeEntity) (Object) this));
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