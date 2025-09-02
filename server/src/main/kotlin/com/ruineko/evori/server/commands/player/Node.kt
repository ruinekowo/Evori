package com.ruineko.evori.server.commands.player

import com.ruineko.evori.common.utils.ComponentUtils
import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.commands.PlayerCommand
import com.ruineko.evori.server.extensions.success
import com.ruineko.evori.server.extensions.warning
import org.bukkit.command.Command
import org.bukkit.entity.Player

class Node(private val plugin: EvoriServer) : PlayerCommand() {
    override fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        val serverName = plugin.serverName

        if (serverName != null) {
            player.sendMessage(ComponentUtils.parse("<aqua>You are currently connected to server <gold>$serverName<aqua>."))
        } else {
            player.warning("This server is running independently (no node ID assigned).")
        }
        return true
    }
}