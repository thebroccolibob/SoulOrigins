package io.github.thebroccolibob.soulorigins

import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.calio.data.SerializableData
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.data.client.Model
import net.minecraft.data.client.TextureKey
import net.minecraft.entity.Entity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.*
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.registry.Registries
import net.minecraft.screen.PropertyDelegate
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World
import org.apache.commons.lang3.tuple.Triple
import org.joml.Vector3f
import java.util.*
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import net.minecraft.util.Pair as McPair
import net.minecraft.util.math.random.Random as MCRandom

inline fun FabricItemSettings(init: FabricItemSettings.() -> Unit) = FabricItemSettings().apply(init)

inline fun FabricBlockSettings(init: FabricBlockSettings.() -> Unit): FabricBlockSettings = FabricBlockSettings.create().apply(init)

operator fun ItemUsageContext.component1(): ItemStack = stack
operator fun ItemUsageContext.component2(): PlayerEntity? = player
operator fun ItemUsageContext.component3(): BlockPos = blockPos
operator fun ItemUsageContext.component4(): World = world
operator fun ItemUsageContext.component5(): Hand = hand
operator fun ItemUsageContext.component6(): Direction = side

operator fun BlockHitResult.component1(): Vec3d = this.pos
operator fun BlockHitResult.component2(): Direction = this.side
operator fun BlockHitResult.component3(): BlockPos = this.blockPos

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
operator fun <T> Triple<T, *, *>.component1(): T = left
operator fun <T> Triple<*, T, *>.component2(): T = middle
operator fun <T> Triple<*, *, T>.component3(): T = right

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
fun <T: Power> Entity.getPower(type: PowerType<T>): T? = PowerHolderComponent.KEY[this].getPower(type)
fun Entity.syncPower(type: PowerType<*>) {
    PowerHolderComponent.syncPower(this, type)
}

fun ItemGroup(init: ItemGroup.Builder.() -> Unit): ItemGroup {
    return FabricItemGroup.builder().apply(init).build()
}

fun ItemGroup.Builder.entries(vararg items: ItemConvertible) {
    entries { _, entries ->
        entries.addAll(items.map { it.asItem().defaultStack })
    }
}
fun ItemGroup.Builder.entries(vararg items: ItemStack) {
    entries { _, entries ->
        entries.addAll(items.toList())
    }
}

fun Model(parent: Identifier? = null, variant: String? = null, vararg requiredTextureKeys: TextureKey): Model {
    return Model(parent.toOptional(), variant.toOptional(), *requiredTextureKeys)
}

operator fun Vec3i.plus(other: Vec3i): Vec3i = this.add(other)
operator fun BlockPos.plus(other: Vec3i): BlockPos = this.add(other)
operator fun Vec3i.minus(other: Vec3i): Vec3i = this.subtract(other)
operator fun BlockPos.minus(other: Vec3i): BlockPos = this.subtract(other)

operator fun Vec3d.plus(other: Vec3d): Vec3d = this.add(other)
operator fun Vec3d.plus(other: Vector3f): Vec3d = this.add(other.x.toDouble(), other.y.toDouble(), other.z.toDouble())
operator fun Vec3d.minus(other: Vec3d): Vec3d = this.subtract(other)
operator fun Vec3d.times(scalar: Double): Vec3d = this.multiply(scalar)
operator fun Vec3d.div(scalar: Double): Vec3d = this * (1.0 / scalar)
operator fun Vec3d.times(other: Vec3d): Vec3d = this.multiply(other)

val Block.id
    get() = Registries.BLOCK.getId(this)

fun BlockPos.copy() = BlockPos(x, y, z)

operator fun <T> TrackedData<T>.getValue(thisRef: Entity, property: KProperty<*>): T {
    return thisRef.dataTracker.get(this)
}

operator fun <T> TrackedData<T>.setValue(thisRef: Entity, property: KProperty<*>, value: T) {
    thisRef.dataTracker.set(this, value)
}

@JvmName("getValueOptional")
operator fun <T: Any> TrackedData<Optional<T>>.getValue(thisRef: Entity, property: KProperty<*>): T? {
    return thisRef.dataTracker.get(this).toNullable()
}

@JvmName("setValueOptional")
operator fun <T : Any> TrackedData<Optional<T>>.setValue(thisRef: Entity, property: KProperty<*>, value: T?) {
    thisRef.dataTracker.set(this, value.toOptional())
}

infix fun Double.floorMod(other: Double) = (this % other + other) % other

fun PropertyDelegate(vararg properties: KMutableProperty0<Int>) = object : PropertyDelegate {
    override fun get(index: Int) = properties[index].get()

    override fun set(index: Int, value: Int) {
        properties[index].set(value)
    }

    override fun size() = properties.size
}

fun DefaultedList<ItemStack>.toInventory(): Inventory {
    val list = this
    return object : Inventory {
        override fun clear() {
            list.clear()
        }

        override fun size() = list.size

        override fun isEmpty() = list.isEmpty()

        override fun getStack(slot: Int) = list[slot]

        override fun removeStack(slot: Int, amount: Int) = list[slot].split(amount)

        override fun removeStack(slot: Int) = list[slot].also {
            setStack(slot, ItemStack.EMPTY)
        }

        override fun setStack(slot: Int, stack: ItemStack) {
            list[slot] = stack
        }

        override fun markDirty() {}

        override fun canPlayerUse(player: PlayerEntity) = true
    }
}

operator fun <T> List<T>.get(range: IntRange): List<T> = range.map(this::get)

interface TrackedDataRegister<T: Entity>

inline fun <reified T : Entity, U> TrackedDataRegister<T>.registerData(handler: TrackedDataHandler<U>): TrackedData<U> {
    return DataTracker.registerData(T::class.java, handler)
}

public fun <T> Collection<T>.random(random: MCRandom): T {
    if (isEmpty())
        throw NoSuchElementException("Collection is empty.")
    return elementAt(random.nextInt(size))
}
