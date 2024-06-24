package io.github.thebroccolibob.soulorigins.cca

import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent
import io.github.thebroccolibob.soulorigins.SoulOrigins
import io.github.thebroccolibob.soulorigins.toNullable
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound

@Suppress("UnstableApiUsage")
class WhiteSpaceComponent(private val entity: PlayerEntity) : PlayerComponent<WhiteSpaceComponent> {
    var isWhitespaced = false

    override fun readFromNbt(tag: NbtCompound) {
        isWhitespaced = tag.getBoolean("Activated")
    }

    override fun writeToNbt(tag: NbtCompound) {
        tag.putBoolean("Activated", isWhitespaced)
    }

    companion object {
        @JvmField
        val KEY: ComponentKey<WhiteSpaceComponent> = ComponentRegistry.getOrCreate(SoulOrigins.id("whitespace"), WhiteSpaceComponent::class.java)

        @JvmStatic
        var PlayerEntity.isWhitespaced: Boolean
            get() = KEY.maybeGet(this).toNullable()?.isWhitespaced ?: false
            set(value) {
                KEY.get(this).isWhitespaced = value
            }
    }
}

