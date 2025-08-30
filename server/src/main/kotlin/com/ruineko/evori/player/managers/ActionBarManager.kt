package com.ruineko.evori.player.managers

import com.ruineko.evori.player.EvoriPlayer
import net.kyori.adventure.text.Component

class ActionBarManager(private val evoriPlayer: EvoriPlayer) {
    var message: String? = null

    fun update() {
        val player = evoriPlayer.base() ?: return
        val message = message ?: return
        player.sendActionBar(Component.text(message))
    }
}