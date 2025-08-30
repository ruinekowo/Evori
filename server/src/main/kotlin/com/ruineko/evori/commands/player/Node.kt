package com.ruineko.evori.commands.player

import com.ruineko.evori.EvoriServer
import com.ruineko.evori.commands.PlayerCommand
import com.ruineko.evori.extensions.success
import org.bukkit.command.Command
import org.bukkit.entity.Player

class Node(private val plugin: EvoriServer) : PlayerCommand() {
    override fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        player.success("You are currently on node ${plugin.redis.nodeId}.")
        return true
    }
}