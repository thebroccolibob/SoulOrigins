package io.github.thebroccolibob.soulorigins.entity

import io.github.thebroccolibob.soulorigins.*
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.FlyingItemEntity
import net.minecraft.entity.MovementType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World
import java.util.*

class LoyaltyItemEntity(type: EntityType<LoyaltyItemEntity>, world: World?) : Entity(type, world), FlyingItemEntity {
    var item by ITEM
    private var ownerUUID by OWNER

    var owner: PlayerEntity?
        get() = world.getPlayerByUuid(ownerUUID)
        set(player) {
            ownerUUID = player?.uuid
        }

    constructor(world: World) : this(SoulOriginsEntities.LOYALTY_ITEM, world)

    constructor(world: World, stack: ItemStack, owner: PlayerEntity) : this(world) {
        this.item = stack
        this.owner = owner
    }

    init {
        noClip = true
    }

    override fun initDataTracker() {
        dataTracker.startTracking(ITEM, Items.STONE.defaultStack)
        dataTracker.startTracking(OWNER, Optional.empty())
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        item = ItemStack.fromNbt(nbt.getCompound("Item"))
        ownerUUID = nbt.getUuid("Owner")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        nbt.put("Item", item.writeNbt(NbtCompound()))
        nbt.putUuid("Owner", ownerUUID)
    }

    override fun getStack(): ItemStack = item

    override fun tick() {
        val owner = this.owner

        if (owner?.isAlive != true) {
            if (!world.isClient) dropStack(item, 0.1f)
            discard()
            return
        }

        val diff = owner.eyePos - pos

        velocity = velocity * 0.95 + diff.normalize() * 0.1

        move(MovementType.SELF, velocity)

        super.tick()

    }

    override fun onPlayerCollision(player: PlayerEntity) {
        val owner = this.owner

        if (owner != player || !owner.isAlive) return

        if (owner.giveItemStack(item) ) {
            world.playSound(null, x, y, z, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, world.random.nextFloat() * 2.8f + 0.4f)
            discard()
        } else if ( owner.abilities.creativeMode) {
            discard()
        }
    }

    companion object {
        val ITEM: TrackedData<ItemStack> = DataTracker.registerData(LoyaltyItemEntity::class.java, TrackedDataHandlerRegistry.ITEM_STACK)
        val OWNER: TrackedData<Optional<UUID>> = DataTracker.registerData(LoyaltyItemEntity::class.java, TrackedDataHandlerRegistry.OPTIONAL_UUID)
    }
}
