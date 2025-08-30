package com.ruineko.evori.commands.player

import com.ruineko.evori.EvoriServer
import com.ruineko.evori.commands.PlayerCommand
import org.bukkit.command.Command
import org.bukkit.entity.Player

class StaffChat(private val plugin: EvoriServer) : PlayerCommand() {
    override fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        val message = args.joinToString(" ")

        val data = mapOf(
            "uuid" to player.uniqueId,
            "node" to plugin.redis.nodeId,
            "message" to message
        )

        val payload = plugin.gson.toJson(data)

        plugin.redis.publish("sc", payload)
        return true
    }
}