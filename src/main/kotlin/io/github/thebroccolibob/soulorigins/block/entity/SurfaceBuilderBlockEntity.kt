package io.github.thebroccolibob.soulorigins.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import java.util.*

class SurfaceBuilderBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(SoulOriginsBlockEntities.SURFACE_BUILDER, pos, state) {

    private var ownerUUID: UUID? = null

    var owner: PlayerEntity?
        get() = world?.getPlayerByUuid(ownerUUID)
        set(player) {
            ownerUUID = player?.uuid
        }

    override fun writeNbt(nbt: NbtCompound) {
        ownerUUID?.let {
            nbt.putUuid("Owner", ownerUUID)
        }
    }

    override fun readNbt(nbt: NbtCompound) {
        ownerUUID = if (nbt.containsUuid("Owner")) nbt.getUuid("Owner") else null
    }
}
