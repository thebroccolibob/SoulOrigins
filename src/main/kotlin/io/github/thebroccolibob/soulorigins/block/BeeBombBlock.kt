package io.github.thebroccolibob.soulorigins.block

import io.github.thebroccolibob.soulorigins.block.entity.BeeBombBlockEntity
import io.github.thebroccolibob.soulorigins.entity.BeeBombEntity
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.TntBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import net.minecraft.world.explosion.Explosion


/**
 * @see [io.github.thebroccolibob.soulorigins.mixin.TntBlockMixin]
 */
@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class BeeBombBlock(settings: Settings) : TntBlock(settings), BlockEntityProvider {

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        if (random.nextFloat() > 0.1) return
        world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_BEEHIVE_WORK, SoundCategory.BLOCKS, 1.0f, 1.0f, true)
    }

    // Will not work due to this event being called after the blockstate is set, so that the block entity is no longer accessible
    override fun onDestroyedByExplosion(world: World, pos: BlockPos, explosion: Explosion) {}

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return BeeBombBlockEntity(pos, state)
    }

    companion object {
        /**
         * @see [io.github.thebroccolibob.soulorigins.mixin.TntBlockMixin]
         */
        @JvmStatic
        fun explodeBomb(world: World, pos: BlockPos, igniter: LivingEntity?) {
            if (!world.isClient) {
                val blockEntity = world.getBlockEntity(pos) as BeeBombBlockEntity
                val beeBombEntity =
                    BeeBombEntity(world, blockEntity.bees, pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5, igniter)
                world.spawnEntity(beeBombEntity)
                world.playSound(null, beeBombEntity.x, beeBombEntity.y, beeBombEntity.z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f)
                world.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos)
            }
        }

//        const val SOUND_CHANCE = 0.1
    }
}
