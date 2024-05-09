package io.github.thebroccolibob.soulorigins

import io.github.apace100.calio.data.SerializableData
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.*

inline fun FabricItemSettings(init: FabricItemSettings.() -> Unit) = FabricItemSettings().apply(init)

operator fun ItemUsageContext.component1(): ItemStack = stack
operator fun ItemUsageContext.component2(): PlayerEntity? = player
operator fun ItemUsageContext.component3(): BlockPos = blockPos
operator fun ItemUsageContext.component4(): World = world
operator fun ItemUsageContext.component5(): Hand = hand
operator fun ItemUsageContext.component6(): Direction = side

fun NbtCompound.getOrCreateCompound(key: String): NbtCompound {
    if (key in this) return getCompound(key)
    return NbtCompound().also { put(key, it) }
}
fun NbtCompound.getOrCreateList(key: String, type: Byte): NbtList {
    if (key in this) return getList(key, type)
    return NbtList().also { put(key, it) }
}

fun NbtList.setIfEmpty(index: Int, element: NbtElement) : Boolean {
    if (!getCompound(index).isEmpty) return false

    set(index, element)
    return true
}

fun NbtCompound.getList(key: String, type: Byte): NbtList = getList(key, type.toInt())

fun <T: Any> Optional<T>.toNullable(): T? = orElse(null)
fun <T: Any> T?.toOptional() = Optional.ofNullable(this)

fun <T, R> Iterable<T>.mapWithNext(transform: (current: T, next: T?) -> R): List<R> {
    return toList().let {
        it.mapIndexed { index, item ->
            transform(item, it.getOrNull(index + 1))
        }
    }
}

fun <T> Iterable<T>.forEachWithNext(transform: (current: T, next: T?) -> Unit) {
    toList().let {
        it.forEachIndexed { index, item ->
            transform(item, it.getOrNull(index + 1))
        }
    }
}

fun SerializableData(init: SerializableData.() -> Unit) = SerializableData().apply(init)
