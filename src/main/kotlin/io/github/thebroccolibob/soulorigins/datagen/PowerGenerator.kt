package io.github.thebroccolibob.soulorigins.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput

class PowerGenerator(dataOutput: FabricDataOutput) : ArbitraryJsonProvider(dataOutput, "powers", "Origins Powers") {
    override fun Writer.generateJsons() {
        json("wind/dash_2") {
            "type" to "origins:active_self"
            "key" to "key.origins.secondary_active"
            "cooldown" to 40
            "hud_render" to {
                "sprite_location" to "origins:textures/gui/community/spiderkolo/resource_bar_03.png"
                "bar_index" to 23
            }
            "condition" to {
                "type" to "origins:or"
                "conditions".to [
                    {
                        "type" to "apugli:key_pressed"
                        "key" to {
                            "key" to "key.forward"
                            "continuous" to true
                        }
                    },
                    {
                        "type" to "apugli:key_pressed"
                        "key" to {
                            "key" to "key.back"
                            "continuous" to true
                        }
                    },
                    {
                        "type" to "apugli:key_pressed"
                        "key" to {
                            "key" to "key.left"
                            "continuous" to true
                        }
                    },
                    {
                        "type" to "apugli:key_pressed"
                        "key" to {
                            "key" to "key.right"
                            "continuous" to true
                        }
                    }
                ]
            }
            "entity_action" to {
                "type" to "origins:and"
                "actions".to [
                    {
                        "type" to "origins:if_else"
                        "condition" to {
                            "type" to "apugli:key_pressed"
                            "key" to {
                                "key" to "key.forward"
                                "continuous" to true
                            }
                        }
                        "if_action" to {
                            "type" to "apoli:add_velocity"
                            "client" to true
                            "space" to "local_horizontal_normalized"
                            "z" to 1
                        }
                    },
                    {
                        "type" to "origins:if_else"
                        "condition" to {
                            "type" to "apugli:key_pressed"
                            "key" to {
                                "key" to "key.back"
                                "continuous" to true
                            }
                        }
                        "if_action" to {
                            "type" to "apoli:add_velocity"
                            "client" to true
                            "space" to "local_horizontal_normalized"
                            "z" to -1
                        }
                    },
                    {
                        "type" to "origins:if_else"
                        "condition" to {
                            "type" to "apugli:key_pressed"
                            "key" to {
                                "key" to "key.left"
                                "continuous" to true
                            }
                        }
                        "if_action" to {
                            "type" to "apoli:add_velocity"
                            "client" to true
                            "space" to "local_horizontal_normalized"
                            "x" to 1
                        }
                    },
                    {
                        "type" to "origins:if_else"
                        "condition" to {
                            "type" to "apugli:key_pressed"
                            "key" to {
                                "key" to "key.right"
                                "continuous" to true
                            }
                        }
                        "if_action" to {
                            "type" to "apoli:add_velocity"
                            "client" to true
                            "space" to "local_horizontal_normalized"
                            "x" to -1
                        }
                    },
                    {
                        "type" to "origins:grant_power"
                        "power" to "soul-origins:wind/dash_active"
                        "source" to "soul-origins:toggle"
                    },
                    {
                        "type" to "origins:delay"
                        "ticks" to 10
                        "action" to {
                            "type" to "origins:revoke_power"
                            "power" to "soul-origins:wind/dash_active"
                            "source" to "soul-origins:toggle"
                        }
                    },
                    {
                        "type" to "origins:play_sound"
                        "category" to "players"
                        "sound" to "minecraft:entity.player.attack.sweep"
                    },
                    {
                        "type" to "origins:spawn_particles"
                        "count" to 16
                        "particle" to "minecraft:cloud"
                        "speed" to 0.05
                    }
                ]
            }
        }
    }
}
