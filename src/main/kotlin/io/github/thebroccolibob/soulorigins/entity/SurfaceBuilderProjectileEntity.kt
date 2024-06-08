package io.github.thebroccolibob.soulorigins.entity

import io.github.thebroccolibob.soulorigins.*
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtHelper
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.SoundCategory
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class SurfaceBuilderProjectileEntity : ThrownItemEntity {
    constructor(type: EntityType<SurfaceBuilderProjectileEntity>, world: World) : super(type, world)
    constructor(world: World, user: LivingEntity) : super(SoulOriginsEntities.SURFACE_BUILDER_PROJECTILE, user, world)
//    constructor(world: World, x: Double, y: Double, z: Double) : super(SoulOriginsEntities.SURFACE_BUILDER_PROJECTILE, x, y, z, world)

    var blockToPlace by BLOCK
    var offsetY by OFFSET_Y

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(BLOCK, Blocks.STONE.defaultState)
        dataTracker.startTracking(OFFSET_Y, 0)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.put("Block", NbtHelper.fromBlockState(blockToPlace))
        nbt.putInt("OffsetY", offsetY)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        blockToPlace = NbtHelper.toBlockState(world.createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("Block"))
        offsetY = nbt.getInt("OffsetY")
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        val (_, side, blockPos) = blockHitResult
        tryPlaceAt(blockPos.offset(side))
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        tryPlaceAt(entityHitResult.entity.blockPos)
    }

    private fun tryPlaceAt(basePos: BlockPos) {
        val placePos = basePos.up(offsetY)

        if (world.getBlockState(placePos).isReplaceable) {
            world.setBlockState(placePos, blockToPlace)
            world.playSound(null, placePos, blockToPlace.soundGroup.placeSound, SoundCategory.BLOCKS, 1.0f, 1.0f)
        } else {
            dropStack(item)
        }
        this.discard()
    }

    override fun getDefaultItem(): Item = Items.STONE

    companion object {
        val BLOCK: TrackedData<BlockState> = DataTracker.registerData(SurfaceBuilderProjectileEntity::class.java, TrackedDataHandlerRegistry.BLOCK_STATE)
        val OFFSET_Y: TrackedData<Int> = DataTracker.registerData(SurfaceBuilderProjectileEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }
}
