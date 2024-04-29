package io.github.thebroccolibob.soulorigins.entity

import net.minecraft.entity.mob.AbstractSkeletonEntity
import net.minecraft.entity.player.PlayerEntity

inline var OwnableSkeleton.owner: PlayerEntity?
    get() = `soulOrigins$getOwner`()
    set(player) {
        `soulOrigins$setOwner`(player)
    }

inline val OwnableSkeleton.isTamed
    get() = `soulOrigins$isTamed`()

inline var AbstractSkeletonEntity.owner: PlayerEntity?
    get() = (this as OwnableSkeleton).owner
    set(player) {
        (this as OwnableSkeleton).owner = player
    }

inline val AbstractSkeletonEntity.isTamed
    get() = (this as OwnableSkeleton).isTamed

val a = OwnableSkeleton::owner
