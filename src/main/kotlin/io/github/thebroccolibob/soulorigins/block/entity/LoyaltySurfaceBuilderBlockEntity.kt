package io.github.thebroccolibob.soulorigins.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

class LoyaltySurfaceBuilderBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(SoulOriginsBlockEntities.SURFACE_BUILDER, pos, state) {

    var owner: Entity? = null

    override fun writeNbt(nbt: NbtCompound) {
        owner?.let {
            nbt.putUuid("Owner", it.uuid)
        }
    }

    override fun readNbt(nbt: NbtCompound) {
        (world as? ServerWorld)?.let {
            if (nbt.containsUuid("Owner")) it.getEntity(nbt.getUuid("Owner")) else null
        }
    }
}
