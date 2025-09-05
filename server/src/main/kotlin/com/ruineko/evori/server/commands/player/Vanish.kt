package com.ruineko.evori.server.commands.player

import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.commands.PlayerCommand
import com.ruineko.evori.server.extensions.severe
import com.ruineko.evori.server.extensions.success
import com.ruineko.evori.server.objects.EvoriPlayerManager
import org.bukkit.command.Command
import org.bukkit.entity.Player

class Vanish(private val plugin: EvoriServer): PlayerCommand() {
    override fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        if (plugin.protocolLibAvailable) {
            player.severe("ProtocolLib is not available. You cannot use this command.")
            return true
        }

        val evoriPlayer = EvoriPlayerManager.get(player.uniqueId)

        if (evoriPlayer.isVanished) {
            evoriPlayer.appear()
            player.success("You have reappeared!")
            evoriPlayer.actionBarManager.clearMessage()
        } else {
            evoriPlayer.vanish()
            player.success("You have disappeared!")
            evoriPlayer.actionBarManager.setMessage("<white>You are currently <RED>VANISHED")
        }
        return true
    }
}