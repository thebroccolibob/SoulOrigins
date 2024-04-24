package io.github.thebroccolibob.soulorigins

import io.wispforest.owo.ui.base.BaseUIModelScreen
import io.wispforest.owo.ui.container.FlowLayout
import net.minecraft.util.Identifier

class OfrendaScreen : BaseUIModelScreen<FlowLayout>(FlowLayout::class.java, DataSource.asset(Identifier("soul-origins", "ofrenda_ui_model"))) {
    override fun build(rootComponent: FlowLayout) {}
}