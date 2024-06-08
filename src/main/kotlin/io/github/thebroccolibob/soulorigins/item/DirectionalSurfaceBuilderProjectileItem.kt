package io.github.thebroccolibob.soulorigins.item

import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerEntity

class DirectionalSurfaceBuilderProjectileItem(private val ewBlock: Block, private val nsBlock: Block, offsetY: Int, settings: Settings) :
    SurfaceBuilderProjectileItem(ewBlock, offsetY, settings) {

    override fun getBlock(user: PlayerEntity): Block {
        return if (user.headYaw in -135f..-45f || user.headYaw in 45f..135f) ewBlock else nsBlock
    }
}
