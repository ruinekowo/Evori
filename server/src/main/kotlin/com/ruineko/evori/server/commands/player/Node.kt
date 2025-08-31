package com.ruineko.evori.server.commands.player

import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.commands.PlayerCommand
import com.ruineko.evori.server.extensions.success
import com.ruineko.evori.server.extensions.warning
import org.bukkit.command.Command
import org.bukkit.entity.Player

class Node(private val plugin: EvoriServer) : PlayerCommand() {
    override fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        val nodeId = plugin.redis.nodeId

        if (nodeId != null) {
            player.success("You are currently on server $nodeId.")
        } else {
            player.warning("This server is running independently (no node ID assigned).")
        }
        return true
    }
}