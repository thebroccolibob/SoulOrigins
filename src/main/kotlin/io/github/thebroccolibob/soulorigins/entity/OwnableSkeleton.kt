package io.github.thebroccolibob.soulorigins.entity

import net.minecraft.entity.player.PlayerEntity

var OwnableSkeleton.owner: PlayerEntity?
    get() = `soulOrigins$getOwner`()
    set(player) {
        `soulOrigins$setOwner`(player)
    }

val OwnableSkeleton.isTamed
    get() = `soulOrigins$isTamed`()

