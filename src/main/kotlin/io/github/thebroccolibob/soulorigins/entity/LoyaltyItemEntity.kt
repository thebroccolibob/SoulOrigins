package io.github.thebroccolibob.soulorigins.entity

import io.github.thebroccolibob.soulorigins.*
import net.minecraft.entity.*
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World
import java.util.*

class LoyaltyItemEntity(type: EntityType<LoyaltyItemEntity>, world: World) : Entity(type, world), FlyingItemEntity, Ownable {
    private var item by ITEM
    private var owner: Entity? = null

    private var pickupTicks = 0

    constructor(world: World) : this(SoulOriginsEntities.LOYALTY_ITEM, world)

    constructor(world: World, stack: ItemStack, owner: Entity) : this(world) {
        this.item = stack
        this.owner = owner
    }

    init {
        noClip = true
    }

    override fun getOwner() = owner

    override fun initDataTracker() {
        dataTracker.startTracking(ITEM, ItemStack.EMPTY)
        dataTracker.startTracking(OWNER, Optional.empty())
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        item = ItemStack.fromNbt(nbt.getCompound("Item"))
        owner = (world as? ServerWorld)?.let {
            if (nbt.containsUuid("Owner"))
                it.getEntity(nbt.getUuid("Owner"))
            else null
        }
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        nbt.put("Item", item.writeNbt(NbtCompound()))
        owner?.let {
            nbt.putUuid("Owner", it.uuid)
        }
    }

    override fun getStack(): ItemStack = item

    override fun tick() {
        if (!world.isClient) {
            val owner = this.owner

            if (owner?.isAlive != true) {
                dropStack(item, 0.1f)
                discard()
                return
            }

            val diff = owner.eyePos - pos

            velocity = velocity * 0.95 + diff.normalize() * 0.02
        }

        move(MovementType.SELF, velocity)

        super.tick()

    }

    override fun onPlayerCollision(player: PlayerEntity) {
        if (this.owner != player || !player.isAlive) return

        pickupTicks++

        if (pickupTicks <= 5) return

        if (player.giveItemStack(item) ) {
            world.playSound(null, x, y, z, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, world.random.nextFloat() * 2.8f + 0.4f)
            discard()
        } else if (player.abilities.creativeMode) {
            discard()
        }
    }

    companion object {
        val ITEM: TrackedData<ItemStack> = DataTracker.registerData(LoyaltyItemEntity::class.java, TrackedDataHandlerRegistry.ITEM_STACK)
        val OWNER: TrackedData<Optional<UUID>> = DataTracker.registerData(LoyaltyItemEntity::class.java, TrackedDataHandlerRegistry.OPTIONAL_UUID)
    }
}
