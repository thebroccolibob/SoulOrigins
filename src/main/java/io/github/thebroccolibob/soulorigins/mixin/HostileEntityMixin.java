package io.github.thebroccolibob.soulorigins.mixin;

import io.github.thebroccolibob.soulorigins.power.DisengagePower;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HostileEntity.class)
public abstract class HostileEntityMixin extends MobEntity {

    protected HostileEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (DisengagePower.shouldMobForget(this)) {
            setTarget(null);
        }
    }
}
