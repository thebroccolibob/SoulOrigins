package io.github.thebroccolibob.soulorigins.item

import io.github.thebroccolibob.soulorigins.*
import io.github.thebroccolibob.soulorigins.entity.OwnableMonster
import io.github.thebroccolibob.soulorigins.entity.owner
import io.github.thebroccolibob.soulorigins.power.UseMobOrbPower
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.*
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.tag.ItemTags
import net.minecraft.screen.slot.Slot
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.ClickType
import net.minecraft.util.Formatting
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World

class MobOrbItem(settings: Settings) : Item(settings) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val (stack, player, blockPos, world, _, side) = context
        if (player?.hasPower<UseMobOrbPower>() == false) return ActionResult.FAIL
        val nbt = stack.nbt

        if (nbt?.contains(ENTITY_NBT) != true) return ActionResult.PASS

        val soulMeter = player?.soulMeter

        // Mana check fails return
        if (player?.abilities?.creativeMode != true && (soulMeter?.value ?: 0) < 2) return ActionResult.PASS

        if (world !is ServerWorld) return ActionResult.SUCCESS


        val monster = stack.getEntity(world) ?: return ActionResult.FAIL

        val spawnPosition = if (world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty) blockPos else blockPos.offset(side)

        monster.setPosition(spawnPosition.x + 0.5, spawnPosition.y.toDouble(), spawnPosition.z + 0.5)

        (monster as? OwnableMonster)?.owner = player
        (monster as? MobEntity)?.setPersistent()

        world.spawnEntity(monster)


        // Mana Expense
        if (player?.abilities?.creativeMode != true) {
            soulMeter -= 2
            player?.syncSoulMeter()
        }

        stack.decrement(1)
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

        getEntityType(stack)?.name?.let(tooltip::add)

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

        val entity = stack.getEntity(player.world)

        if (entity !is MobEntity) return false

        val targetSlot = if (otherStack.isEmpty) getNextRemovalSlot(entity) else getPreferredSlot(otherStack)

        if (targetSlot === null) return false

        val prevItem = entity[targetSlot]

        entity[targetSlot] = otherStack

        cursorStackReference.set(prevItem)

        stack.setEntity(entity)

        entity.discard()

        return true
    }

    companion object {
        const val ENTITY_NBT = "EntityTag"

        val ItemStack.hasEntity
            get() = nbt?.contains(ENTITY_NBT) ?: false

        fun getEntity(itemStack: ItemStack, world: World): Entity? {
            return itemStack.nbt?.let {
                EntityType.getEntityFromNbt(it.getCompound(ENTITY_NBT), world).toNullable()
            }
        }

        fun setEntity(stack: ItemStack, entity: Entity) {
            stack.orCreateNbt.put(ENTITY_NBT, NbtCompound().apply {
                // Get mob NBT data
                entity.saveSelfNbt(this)
                remove("Pos")
                remove("UUID")
            })
        }

        @JvmName("getEntityExt")
        private fun ItemStack.getEntity(world: World) = getEntity(this, world)

        @JvmName("setEntityExt")
        private fun ItemStack.setEntity(entity: Entity) {
            setEntity(this, entity)
        }

        fun getEntityType(itemStack: ItemStack): EntityType<*>? {
            return itemStack.nbt?.getCompound(ENTITY_NBT)?.let {
                EntityType.fromNbt(it).toNullable()
            }
        }

        fun getMobType(itemStack: ItemStack): Float {
            return when (getEntityType(itemStack)) {
                EntityType.ZOMBIE -> 0.01f
                EntityType.HUSK -> 0.02f
                EntityType.DROWNED -> 0.03f
                EntityType.SKELETON -> 0.04f
                EntityType.STRAY -> 0.05f
                EntityType.WITHER_SKELETON -> 0.06f
                EntityType.SPIDER -> 0.07f
                EntityType.CAVE_SPIDER -> 0.08f
                EntityType.CREEPER -> 0.09f
                EntityType.ENDERMAN -> 0.10f
                EntityType.SLIME -> 0.11f
                EntityType.WARDEN -> 0.12f
                else -> 0f
            }
        }

        private fun getPreferredSlot(insertStack: ItemStack): EquipmentSlot  {
            return when {
                insertStack.isIn(ItemTags.ARROWS) || insertStack.isOf(Items.TOTEM_OF_UNDYING) -> EquipmentSlot.OFFHAND
                insertStack.isOf(Items.CARVED_PUMPKIN) -> EquipmentSlot.HEAD
                insertStack.item is Equipment -> (insertStack.item as Equipment).slotType
                else -> EquipmentSlot.MAINHAND
            }
        }

        private fun getNextRemovalSlot(entity: MobEntity): EquipmentSlot? {
            return listOf(
                EquipmentSlot.MAINHAND,
                EquipmentSlot.OFFHAND,
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
            ).firstOrNull {
                !entity[it].isEmpty
            }
        }

        private operator fun MobEntity.get(slot: EquipmentSlot): ItemStack {
            return if (slot.type === EquipmentSlot.Type.HAND)
                (this.handItems as DefaultedList<ItemStack>)[slot.entitySlotId]
            else
                (this.armorItems as DefaultedList<ItemStack>)[slot.entitySlotId]
        }

        private operator fun MobEntity.set(slot: EquipmentSlot, stack: ItemStack) {
            if (slot.type === EquipmentSlot.Type.HAND)
                (this.handItems as DefaultedList<ItemStack>)[slot.entitySlotId] = stack
            else
                (this.armorItems as DefaultedList<ItemStack>)[slot.entitySlotId] = stack
        }
    }
}
