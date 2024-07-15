package io.github.thebroccolibob.soulorigins.block.entity

import io.github.thebroccolibob.soulorigins.getList
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.util.math.BlockPos

class BeeBombBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(SoulOriginsBlockEntities.BEE_BOMB, pos, state) {
    var bees: List<NbtCompound> = listOf(NbtCompound(), NbtCompound(), NbtCompound())
        private set

    override fun writeNbt(nbt: NbtCompound) {
        nbt.put(BEES_KEY, NbtList().apply { bees.forEach(::add) })
    }

    override fun readNbt(nbt: NbtCompound) {
        bees = nbt.getList(BEES_KEY, NbtCompound.COMPOUND_TYPE).map { it as NbtCompound }
    }

    companion object {
        const val BEES_KEY = "Bees"
    }
}
