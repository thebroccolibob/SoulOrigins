package io.github.thebroccolibob.soulorigins.item

import io.github.thebroccolibob.soulorigins.entity.SurfaceBuilderProjectileEntity
import net.minecraft.block.Block
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.ClickType
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class AdjustableSurfaceBuilderProjectileItem(block: Block, offsetY: Int, private val distances: IntArray, settings: Settings) :
    SurfaceBuilderProjectileItem(block, offsetY, settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!user.isSneaking) return super.use(world, user, hand)

        val stack = user.getStackInHand(hand)
        val newDistance = distances[(distances.indexOf(stack.placeDistance) + 1) % distances.size]
        stack.placeDistance = newDistance

        if (world.isClient) {
            user.playSound(SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.NEUTRAL, 0.5f, 2.0f)
            user.sendMessage(adjustmentText(newDistance), true)
        }

        return TypedActionResult.consume(stack)
    }

    override fun createProjectile(
        world: World,
        user: PlayerEntity,
        itemStack: ItemStack
    ): SurfaceBuilderProjectileEntity {
        return SurfaceBuilderProjectileEntity(world, user, itemStack, getBlock(user).defaultState, offsetY, itemStack.placeDistance)
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        tooltip.add(adjustmentText(stack.placeDistance).formatted(Formatting.GRAY))
    }

    override fun onStackClicked(stack: ItemStack, slot: Slot, clickType: ClickType, player: PlayerEntity): Boolean {
        if (slot.stack.isOf(this)) {
            stack.placeDistance = slot.stack.placeDistance
        }
        return false
    }

    companion object {
        const val PLACE_DISTANCE_NBT = "PlaceDistance"

        private var ItemStack.placeDistance: Int
            get() = nbt?.getInt(PLACE_DISTANCE_NBT) ?: 0
            set(value) {
                if (value == 0) {
                    removeSubNbt(PLACE_DISTANCE_NBT)
                    return
                }
                orCreateNbt.putInt(PLACE_DISTANCE_NBT, value)
            }

        fun adjustmentText(distance: Int): MutableText = Text.translatable(
            "item.soul-origins.artificer_builder.place_distance",
            if (distance == 0) Text.translatable("item.soul-origins.artificer_builder.place_distance.none") else distance
        )
    }
}
