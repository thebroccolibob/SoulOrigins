package io.github.thebroccolibob.soulorigins.entity

import io.github.thebroccolibob.soulorigins.block.entity.LoyaltySurfaceBuilderBlockEntity
import io.github.thebroccolibob.soulorigins.component1
import io.github.thebroccolibob.soulorigins.component2
import io.github.thebroccolibob.soulorigins.component3
import io.github.thebroccolibob.soulorigins.plus
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtHelper
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.RaycastContext
import net.minecraft.world.World

class SurfaceBuilderProjectileEntity : ThrownItemEntity {
    constructor(type: EntityType<SurfaceBuilderProjectileEntity>, world: World) : super(type, world)
    constructor(world: World, user: LivingEntity) : super(SoulOriginsEntities.SURFACE_BUILDER_PROJECTILE, user, world)
    constructor(world: World, user: LivingEntity, item: ItemStack, blockToPlace: BlockState, offsetY: Int, placeDistance: Int = 0) : this(world, user) {
        setItem(item)
        this.blockToPlace = blockToPlace
        this.offsetY = offsetY
        this.placeDistance = placeDistance
    }

    private var blockToPlace: BlockState = Blocks.AIR.defaultState
    private var offsetY: Int = 0
    private var placeDistance: Int = 0

    private var traveledDistance: Double = 0.0

    private var lastInAir = world.getFluidState(blockPos).isEmpty

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.put("Block", NbtHelper.fromBlockState(blockToPlace))
        nbt.putInt("OffsetY", offsetY)
        nbt.putInt("PlaceDistance", placeDistance)
        nbt.putDouble("DistanceTraveled", traveledDistance)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        blockToPlace = NbtHelper.toBlockState(world.createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("Block"))
        offsetY = nbt.getInt("OffsetY")
        placeDistance = nbt.getInt("PlaceDistance")
        traveledDistance = nbt.getDouble("DistanceTraveled")
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        if (world.isClient) return
        val (_, side, blockPos) = blockHitResult
        tryPlaceAt(if (world.getBlockState(blockPos).isReplaceable) blockPos else blockPos.offset(side))
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        if (world.isClient) return
        tryPlaceAt(entityHitResult.entity.blockPos)
    }

    override fun tick() {
        traveledDistance += velocity.length()

        super.tick()

        if (this.isRemoved) return

        if (placeDistance > 0 && traveledDistance > placeDistance) {
            tryPlaceAt(blockPos)
            return
        }

        if (lastInAir) {
            val fluidHit = world.raycast(
                RaycastContext(
                    pos,
                    pos + velocity,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.ANY,
                    this
                )
            )

            if (fluidHit.type == HitResult.Type.BLOCK) {
                fluidHit as BlockHitResult
                if (world.getBlockState(fluidHit.blockPos).let {
                        !it.fluidState.isEmpty && it.isReplaceable
                    }) {
                    onBlockHit(fluidHit)
                }
            }
        }

        lastInAir = world.getFluidState(blockPos).isEmpty
    }

    private fun tryPlaceAt(basePos: BlockPos) {
        val placePos = basePos.up(offsetY)

        if (world.getBlockState(placePos).isReplaceable) {
            world.setBlockState(placePos, blockToPlace)
            (owner as? PlayerEntity)?.let {
                (world.getBlockEntity(placePos) as? LoyaltySurfaceBuilderBlockEntity)?.run {
                    owner = it
                    item = this@SurfaceBuilderProjectileEntity.item
                }
            }
            world.playSound(null, placePos, blockToPlace.soundGroup.placeSound, SoundCategory.BLOCKS, 1.0f, 1.0f)
        } else {
            dropStack(item)
        }

        this.discard()
    }

    override fun getDefaultItem(): Item = Items.STONE

    override fun getName(): Text {
        return customName ?: item.name
    }
}
