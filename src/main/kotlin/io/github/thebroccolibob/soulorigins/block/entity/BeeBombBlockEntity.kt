package io.github.thebroccolibob.soulorigins.block.entity

import io.github.thebroccolibob.soulorigins.getList
import io.github.thebroccolibob.soulorigins.hasPower
import io.github.thebroccolibob.soulorigins.random
import net.merchantpug.apugli.power.PreventBeeAngerPower
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.BeeEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box

class BeeBombBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(SoulOriginsBlockEntities.BEE_BOMB, pos, state) {
    var bees: List<NbtCompound> = listOf()
        private set

    override fun writeNbt(nbt: NbtCompound) {
        nbt.put(BEES_KEY, NbtList().apply { bees.forEach(::add) })
    }

    override fun readNbt(nbt: NbtCompound) {
        bees = nbt.getList(BEES_KEY, NbtCompound.COMPOUND_TYPE).map { it as NbtCompound }
    }

    fun spawnAll() {
        if (world == null) return
        val entities = world!!.getOtherEntities(null, Box.of(pos.toCenterPos(), ANGER_RANGE, ANGER_RANGE, ANGER_RANGE)) { it.isLiving && it !is BeeEntity && !it.hasPower<PreventBeeAngerPower>() }
        for (bee in bees) {
            EntityType.BEE.create(world)?.apply {
                readNbt(bee.getCompound("EntityData"))
                refreshPositionAndAngles(this@BeeBombBlockEntity.pos, yaw, pitch)
                world!!.spawnEntity(this)
                universallyAnger()
                if (entities.isNotEmpty()) {
                    target = entities.random(world.random) as LivingEntity
                }
            }
        }
    }

    companion object {
        const val BEES_KEY = "Bees"
        const val ANGER_RANGE = 16.0
    }
}
