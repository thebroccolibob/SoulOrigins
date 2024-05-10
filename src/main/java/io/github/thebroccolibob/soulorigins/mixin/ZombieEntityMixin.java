package io.github.thebroccolibob.soulorigins.mixin;

import io.github.thebroccolibob.soulorigins.entity.OwnableMonster;
import io.github.thebroccolibob.soulorigins.entity.ai.goal.MonsterAttackWithOwnerGoal;
import io.github.thebroccolibob.soulorigins.entity.ai.goal.MonsterFollowOwnerGoal;
import io.github.thebroccolibob.soulorigins.entity.ai.goal.MonsterTrackOwnerAttackerGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
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

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends HostileEntity implements OwnableMonster {
    @Unique
	@SuppressWarnings("WrongEntityDataParameterClass")
	private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(ZombieEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

	protected ZombieEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(
			method = "initDataTracker",
			at = @At("TAIL")
	)
	private void injectOwnerData(CallbackInfo ci) {
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
		goalSelector.add(4, new MonsterFollowOwnerGoal((ZombieEntity) (Object) this, 1.0, 16f, 8f, false));
		targetSelector.add(1, new MonsterTrackOwnerAttackerGoal((ZombieEntity) (Object) this));
		targetSelector.add(2, new MonsterAttackWithOwnerGoal((ZombieEntity) (Object) this));
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
