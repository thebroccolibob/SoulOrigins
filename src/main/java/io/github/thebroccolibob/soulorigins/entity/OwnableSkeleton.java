package io.github.thebroccolibob.soulorigins.entity;

import net.minecraft.entity.Ownable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface OwnableSkeleton extends Ownable {
    World getWorld();
    
    @Nullable UUID soulOrigins$getOwnerUuid();
    void soulOrigins$setOwnerUuid(@Nullable UUID uuid);
    
    default @Nullable PlayerEntity getOwner() {
        UUID uuid = soulOrigins$getOwnerUuid();
        if (uuid == null) return null;
        return getWorld().getPlayerByUuid(uuid);
    }
    
    default void setOwner(PlayerEntity player) {
        soulOrigins$setOwnerUuid(player.getUuid());
    }
    
    default boolean isTamed() {
        return soulOrigins$getOwnerUuid() != null;
    }
}
