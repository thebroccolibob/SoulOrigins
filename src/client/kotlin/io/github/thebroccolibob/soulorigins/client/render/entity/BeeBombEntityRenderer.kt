package io.github.thebroccolibob.soulorigins.client.render.entity

import io.github.thebroccolibob.soulorigins.TntEntityRendererBlockStateProvider
import io.github.thebroccolibob.soulorigins.block.SoulOriginsBlocks
import net.minecraft.block.BlockState
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.TntEntityRenderer

class BeeBombEntityRenderer(context: EntityRendererFactory.Context) : TntEntityRenderer(context), TntEntityRendererBlockStateProvider {
    override fun provideCustomBlockState(): BlockState {
        return SoulOriginsBlocks.BEE_BOMB.defaultState
    }
}
