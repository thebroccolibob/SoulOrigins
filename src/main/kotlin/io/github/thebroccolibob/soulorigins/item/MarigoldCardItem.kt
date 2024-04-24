package io.github.thebroccolibob.soulorigins.item

import io.github.thebroccolibob.soulorigins.*
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.mob.AbstractSkeletonEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.Equipment
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.registry.tag.ItemTags
import net.minecraft.screen.slot.Slot
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.ClickType
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.world.World

class MarigoldCardItem(settings: Settings) : Item(settings) {
    override fun useOnEntity(stack: ItemStack, user: PlayerEntity, entity: LivingEntity, hand: Hand): ActionResult {
        if (stack.nbt?.contains(ENTITY_NBT) == true) return ActionResult.PASS

        if (entity !is AbstractSkeletonEntity) return ActionResult.PASS

        stack.orCreateNbt.put(ENTITY_NBT, NbtCompound().apply {
            entity.saveSelfNbt(this)
            remove("Pos")
            remove("UUID")
        })
        entity.customName?.let(stack::setCustomName)
        entity.discard()
        return ActionResult.SUCCESS
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val (stack, player, blockPos, world, _, side) = context
        val nbt = stack.nbt

        if (nbt?.contains(ENTITY_NBT) != true) return ActionResult.PASS

        if (world !is ServerWorld) return ActionResult.SUCCESS

        val spawnPosition = if (world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty) blockPos else blockPos.offset(side)

        EntityType.fromNbt(nbt.getCompound(ENTITY_NBT)).toNullable()!!.spawnFromItemStack(world, stack, player, spawnPosition, SpawnReason.SPAWN_EGG, true, false)

        nbt.remove(ENTITY_NBT)
        stack.removeCustomName()

        return ActionResult.CONSUME
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        val entityNbt = stack.nbt?.getCompound(ENTITY_NBT)

        if (entityNbt == null || entityNbt.isEmpty) {
            tooltip.add(Text.translatable("item.soul-origins.marigold_card.empty").apply {
                formatted(Formatting.GRAY)
            })
            return
        }

        EntityType.fromNbt(entityNbt).toNullable()?.name?.let(tooltip::add)
    }

    override fun onClicked(stack: ItemStack, otherStack: ItemStack, slot: Slot, clickType: ClickType, player: PlayerEntity, cursorStackReference: StackReference): Boolean {
        if (clickType != ClickType.RIGHT) return false

        val stackNbt = otherStack.writeNbt(NbtCompound())

        val (list, index) = if (otherStack.isIn(ItemTags.ARROWS))
            stack.handItems to 1
        else
            when ((otherStack.item as? Equipment)?.slotType) {
                EquipmentSlot.FEET -> stack.armorItems to 0
                EquipmentSlot.CHEST -> stack.armorItems to 1
                EquipmentSlot.LEGS -> stack.armorItems to 2
                EquipmentSlot.HEAD -> stack.armorItems to 3
                EquipmentSlot.OFFHAND -> stack.handItems to 1
                else -> stack.handItems to 0
            }

        val prevItem = list.getCompound(index)

        list[index] = stackNbt

        cursorStackReference.set(if (prevItem.isEmpty) ItemStack.EMPTY else ItemStack.fromNbt(prevItem))

        return true
    }

    companion object {
        const val ENTITY_NBT = "EntityTag"

        val ItemStack.armorItems
            get() = orCreateNbt.getOrCreateCompound(ENTITY_NBT).getOrCreateList("ArmorItems", NbtElement.COMPOUND_TYPE)

        val ItemStack.handItems
            get() = orCreateNbt.getOrCreateCompound(ENTITY_NBT).getOrCreateList("HandItems", NbtElement.COMPOUND_TYPE)
    }
}
