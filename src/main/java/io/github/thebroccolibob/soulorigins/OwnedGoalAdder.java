package io.github.thebroccolibob.soulorigins;

import io.github.thebroccolibob.soulorigins.cca.OwnerComponent;
import io.github.thebroccolibob.soulorigins.entity.ai.goal.MonsterAttackWithOwnerGoal;
import io.github.thebroccolibob.soulorigins.entity.ai.goal.MonsterFollowOwnerGoal;
import io.github.thebroccolibob.soulorigins.entity.ai.goal.MonsterTrackOwnerAttackerGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Unique;

public interface OwnedGoalAdder {
    GoalSelector getGoalSelector();
    GoalSelector getTargetSelector();

    @Unique
    default void soulOrigins$addOwnedGoals() {
        final MobEntity thisRef = (MobEntity) this;

        if (OwnerComponent.isOwned(thisRef)) {
            getGoalSelector().add(4, new MonsterFollowOwnerGoal(thisRef, 1.0, 16f, 8f, false));
            getTargetSelector().add(1, new MonsterTrackOwnerAttackerGoal(thisRef));
            getTargetSelector().add(2, new MonsterAttackWithOwnerGoal(thisRef));
        }
    }
}
