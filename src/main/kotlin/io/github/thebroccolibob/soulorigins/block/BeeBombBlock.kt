package io.github.thebroccolibob.soulorigins.block

import io.github.thebroccolibob.soulorigins.block.entity.BeeBombBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.minecraft.world.World.ExplosionSourceType

@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class BeeBombBlock(settings: Settings) : Block(settings), BlockEntityProvider {
    init {
        defaultState = defaultState
            .with(LIT, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(LIT)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        val stack = player.getStackInHand(hand)
        if (!stack.isOf(Items.FLINT_AND_STEEL) && !stack.isOf(Items.FIRE_CHARGE))
            return super.onUse(state, world, pos, player, hand, hit)

        world.setBlockState(pos, state.with(LIT, true))
        world.playSound(null, pos, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS)
        world.scheduleBlockTick(pos, this, DELAY)

        return ActionResult.SUCCESS
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        val blockEntity = world.getBlockEntity(pos) as BeeBombBlockEntity
        world.breakBlock(pos, false)
        world.createExplosion(null, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 1f, ExplosionSourceType.BLOCK)
        blockEntity.spawnAll()
    }

    companion object {
        val LIT: BooleanProperty = Properties.LIT
        const val DELAY = 80
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = BeeBombBlockEntity(pos, state)
}
