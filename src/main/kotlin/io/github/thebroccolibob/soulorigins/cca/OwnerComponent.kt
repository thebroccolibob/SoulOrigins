package io.github.thebroccolibob.soulorigins.cca

import dev.onyxstudios.cca.api.v3.component.Component
import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.toNullable
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import java.util.*

class OwnerComponent(private val entity: MobEntity) : Component, AutoSyncedComponent {
    private var uuid: UUID? = null

    var owner: LivingEntity? = null
        get() {
            if (entity.world.isClient || uuid == null) return null
            if (field == null || field!!.uuid != uuid) {
                field = (entity.world as ServerWorld).getEntity(uuid) as? LivingEntity
            }
            return field
        }
        set(value) {
            if (entity.world.isClient) return
            uuid = value?.uuid
            field = value
        }

    val isOwned
        get() = uuid != null

    override fun readFromNbt(tag: NbtCompound) {
        uuid = if (tag.containsUuid("Owner"))
            tag.getUuid("Owner")
        else null
    }

    override fun writeToNbt(tag: NbtCompound) {
        if (uuid != null) tag.putUuid("Owner", uuid)
    }

    companion object {
        @JvmField
        val KEY: ComponentKey<OwnerComponent> = ComponentRegistry.getOrCreate(SoulOrigins.id("owner"), OwnerComponent::class.java)

        @JvmStatic
        var MobEntity.owner: LivingEntity?
            get() = KEY.maybeGet(this).toNullable()?.owner
            set(value) {
                KEY.get(this).owner = value
            }

        @JvmStatic
        val MobEntity.isOwned get() = KEY.maybeGet(this).toNullable()?.isOwned == true
    }
}

