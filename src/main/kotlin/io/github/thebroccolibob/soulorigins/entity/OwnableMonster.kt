package io.github.thebroccolibob.soulorigins.entity

import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity

inline var OwnableMonster.owner: PlayerEntity?
    get() = `soulOrigins$getOwner`()
    set(player) {
        `soulOrigins$setOwner`(player)
    }

inline val OwnableMonster.isTamed
    get() = `soulOrigins$isTamed`()

inline var MobEntity.owner: PlayerEntity?
    get() = (this as? OwnableMonster)?.owner
    set(player) {
        if (this is OwnableMonster) {
            (this as OwnableMonster).owner = player
        }
    }

inline val MobEntity.isTamed
    get() = (this as? OwnableMonster)?.isTamed ?: false