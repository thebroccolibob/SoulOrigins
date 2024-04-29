package io.github.thebroccolibob.soulorigins.item

import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.PowerTypeRegistry
import io.github.apace100.apoli.power.VariableIntPower
import io.github.thebroccolibob.soulorigins.*
import io.github.thebroccolibob.soulorigins.entity.OwnableSkeleton
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.mob.AbstractSkeletonEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.*
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.tag.ItemTags
import net.minecraft.screen.slot.Slot
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.*
import net.minecraft.world.World
import io.github.thebroccolibob.soulorigins.entity.owner
import kotlin.Pair

class MarigoldCardItem(settings: Settings) : Item(settings) {
    override fun useOnEntity(stack: ItemStack, user: PlayerEntity, entity: LivingEntity, hand: Hand): ActionResult {
        if (stack.hasEntity) return ActionResult.PASS

        if (entity !is AbstractSkeletonEntity) return ActionResult.PASS

        stack.orCreateNbt.put(ENTITY_NBT, NbtCompound().apply {
            entity.saveSelfNbt(this)
            remove("Pos")
            remove("UUID")
        })
        entity.customName?.let(stack::setCustomName)
        entity.discard()

        // mana refund :3
        changeSoulMeter(2, user)
        return ActionResult.SUCCESS
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val (stack, player, blockPos, world, _, side) = context
        val nbt = stack.nbt

        if (nbt?.contains(ENTITY_NBT) != true) return ActionResult.PASS

        // mana check fails return
        if (getSoulMeter(player!!) < 2) return  ActionResult.PASS

        if (world !is ServerWorld) return ActionResult.SUCCESS

        val spawnPosition = if (world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty) blockPos else blockPos.offset(side)

        val skeleton = EntityType.fromNbt(nbt.getCompound(ENTITY_NBT)).toNullable()?.spawnFromItemStack(world, stack, player, spawnPosition, SpawnReason.SPAWN_EGG, true, false)

        (skeleton as? OwnableSkeleton)?.owner = player
        (skeleton as? MobEntity)?.setPersistent()

        nbt.remove(ENTITY_NBT)
        stack.removeCustomName()

        // mana expense >:3
        changeSoulMeter(-2, player)
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
        val armorItems = entityNbt.getList("ArmorItems", NbtCompound.COMPOUND_TYPE)
        val handItems = entityNbt.getList("HandItems", NbtCompound.COMPOUND_TYPE)
        (handItems + armorItems).forEach {
            if (!(it as NbtCompound).isEmpty) {
                val gearStack = ItemStack.fromNbt(it)
                if (gearStack.count > 1)
                    tooltip.add(Text.translatable("item.soul-origins.marigold_card.multiple_items", gearStack.name, gearStack.count))
                else
                    tooltip.add(gearStack.name)
            }
        }
    }

    override fun onClicked(stack: ItemStack, otherStack: ItemStack, slot: Slot, clickType: ClickType, player: PlayerEntity, cursorStackReference: StackReference): Boolean {
        if (clickType != ClickType.RIGHT) return false
        if (!stack.hasEntity) return false

        val stackNbt = if(otherStack.isEmpty) NbtCompound() else otherStack.writeNbt(NbtCompound())

        val reference = (if (otherStack.isEmpty) getNbtRefForRemoval(stack) else getNbtRefForPreferredSlot(otherStack, stack))

        if (reference == null) return false

        val (list, index) = reference

        val prevItem = list.getCompound(index)

        list[index] = stackNbt

        cursorStackReference.set(if (prevItem.isEmpty) ItemStack.EMPTY else ItemStack.fromNbt(prevItem))

        return true
    }

    companion object {
        const val ENTITY_NBT = "EntityTag"

        private fun ItemStack.getCreateArmorItems() =
            orCreateNbt.getOrCreateCompound(ENTITY_NBT).getOrCreateList("ArmorItems", NbtElement.COMPOUND_TYPE)

        private val ItemStack.armorItems
            get() = nbt?.getCompound(ENTITY_NBT)?.getList("ArmorItems", NbtElement.COMPOUND_TYPE) ?: NbtList()

        private fun ItemStack.getCreateHandItems() =
            orCreateNbt.getOrCreateCompound(ENTITY_NBT).getOrCreateList("HandItems", NbtElement.COMPOUND_TYPE)

        private val ItemStack.handItems
            get() = nbt?.getCompound(ENTITY_NBT)?.getList("HandItems", NbtElement.COMPOUND_TYPE) ?: NbtList()

        val ItemStack.hasEntity
            get() = nbt?.contains(ENTITY_NBT) ?: false

        private fun getNbtRefForPreferredSlot(insertStack: ItemStack, targetStack: ItemStack): Pair<NbtList, Int> {
            if (insertStack.isIn(ItemTags.ARROWS) || insertStack.isOf(Items.TOTEM_OF_UNDYING))
                return targetStack.getCreateHandItems() to 1

            if (insertStack.isOf(Items.CARVED_PUMPKIN))
                return targetStack.getCreateArmorItems() to 3

            return when ((insertStack.item as? Equipment)?.slotType) {
                EquipmentSlot.FEET -> targetStack.getCreateArmorItems() to 0
                EquipmentSlot.LEGS -> targetStack.getCreateArmorItems() to 1
                EquipmentSlot.CHEST -> targetStack.getCreateArmorItems() to 2
                EquipmentSlot.HEAD -> targetStack.getCreateArmorItems() to 3
                EquipmentSlot.OFFHAND -> targetStack.getCreateHandItems() to 1
                else -> targetStack.getCreateHandItems() to 0
            }
        }

        private fun getNbtRefForRemoval(targetStack: ItemStack): Pair<NbtList, Int>? {
            val handItems = targetStack.handItems
            val armorItems = targetStack.armorItems

            return listOf(
                handItems to 0,
                handItems to 1,
                armorItems to 3,
                armorItems to 2,
                armorItems to 1,
                armorItems to 0,
            ).firstOrNull { (list, index) ->
                !list.getCompound(index).isEmpty
            }
        }
        fun changeSoulMeter(change: Int, entity: PlayerEntity) {
            val component = PowerHolderComponent.KEY[entity]
            val powerType: PowerType<*> = PowerTypeRegistry.get(Identifier("soul-origins", "soul_meter"))
            val p: Power = component.getPower(powerType)
            if (p is VariableIntPower) {
                val newValue: Int = p.value + change
                p.setValue(newValue)
                PowerHolderComponent.syncPower(entity, powerType)
            }
        }
        fun getSoulMeter(entity: PlayerEntity): Int {
            val component = PowerHolderComponent.KEY[entity]
            val powerType: PowerType<*> = PowerTypeRegistry.get(Identifier("soul-origins", "soul_meter"))
            val p: Power = component.getPower(powerType)
            if (p is VariableIntPower) {
                return p.value
            }
            return 0
        }
    }
}
