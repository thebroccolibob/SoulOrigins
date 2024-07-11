package io.github.thebroccolibob.soulorigins.cca

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy
import net.minecraft.entity.mob.MobEntity

class SoulOriginsComponents : EntityComponentInitializer {
    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerFor(MobEntity::class.java, OwnerComponent.KEY, ::OwnerComponent)
        registry.registerForPlayers(WhiteSpaceComponent.KEY, ::WhiteSpaceComponent, RespawnCopyStrategy.CHARACTER)
    }
}
