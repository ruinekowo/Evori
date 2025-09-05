package com.ruineko.evori.server.player.managers

import com.ruineko.evori.common.utils.ComponentUtils
import com.ruineko.evori.server.player.EvoriPlayer
import net.kyori.adventure.text.Component

class ActionBarManager(
    private val evoriPlayer: EvoriPlayer,
) {
    var message: Component? = null

    fun update() {
        val player = evoriPlayer.base() ?: return
        val message = message ?: return

        player.sendActionBar(message)
    }

    fun setMessage(message: String) {
        this.message = ComponentUtils.parse(message)
    }

    fun clearMessage() {
        message = null
    }
}