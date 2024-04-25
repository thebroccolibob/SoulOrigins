package io.github.thebroccolibob.soulorigins.screen

import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.container.FlowLayout
import net.minecraft.util.Identifier

class LaBaraja : BaseUIModelScreen<FlowLayout>(FlowLayout::class.java, DataSource.asset(Identifier("soul-origins", "la_baraja"))){
    protected override fun build(rootComponent: FlowLayout?) {}
}