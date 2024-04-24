package io.github.thebroccolibob.soulorigins

import io.github.thebroccolibob.soulorigins.screen.LaOfrenda
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.option.KeyBinding
import org.lwjgl.glfw.GLFW

object SouloriginsClient : ClientModInitializer {
	private val TEST = KeyBinding("advancements.adventure.root.title", GLFW.GLFW_KEY_H, "gamerule.category.misc")

	override fun onInitializeClient() {
		KeyBindingHelper.registerKeyBinding(TEST)
		ClientTickEvents.END_CLIENT_TICK.register {client ->
			while(TEST.wasPressed()) {
				client.setScreen(LaOfrenda())
			}
		}
	}
}