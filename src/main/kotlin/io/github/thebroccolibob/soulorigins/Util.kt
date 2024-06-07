package io.github.thebroccolibob.soulorigins

import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.apace100.apoli.power.Power
import io.github.apace100.calio.data.SerializableData
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*
import net.minecraft.util.Pair as McPair

inline fun FabricItemSettings(init: FabricItemSettings.() -> Unit) = FabricItemSettings().apply(init)

inline fun FabricBlockSettings(init: FabricBlockSettings.() -> Unit): FabricBlockSettings = FabricBlockSettings.create().apply(init)

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

operator fun <T> McPair<T, *>.component1(): T = left
operator fun <T> McPair<*, T>.component2(): T = right

fun <T> Iterable<T>.forEachWithNext(transform: (current: T, next: T?) -> Unit) {
    toList().let {
        it.forEachIndexed { index, item ->
            transform(item, it.getOrNull(index + 1))
        }
    }
}

fun SerializableData(init: SerializableData.() -> Unit) = SerializableData().apply(init)

operator fun Item.plus(suffix: String) = "$translationKey.$suffix"

inline fun <reified T : Power> Entity.hasPower() = PowerHolderComponent.hasPower(this, T::class.java)
inline fun <reified T : Power> Entity.getPowers(): List<T> = PowerHolderComponent.getPowers(this, T::class.java)

fun ItemGroup(init: ItemGroup.Builder.() -> Unit): ItemGroup {
    return FabricItemGroup.builder().apply(init).build()
}

fun ItemGroup.Entries.add(vararg items: ItemConvertible) {
    items.forEach(::add)
}

fun Model(parent: Identifier? = null, variant: String? = null, vararg requiredTextureKeys: TextureKey): Model {
    return Model(parent.toOptional(), variant.toOptional(), *requiredTextureKeys)
}

operator fun Vec3d.times(value: Double): Vec3d = this.multiply(value)
operator fun Vec3d.times(other: Vec3d): Vec3d = this.multiply(other)
operator fun Vec3d.plus(other: Vec3d): Vec3d = this.add(other)
