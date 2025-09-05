package com.ruineko.evori.server.commands.player

import com.ruineko.evori.common.redis.StaffChatMessage
import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.commands.PlayerCommand
import org.bukkit.command.Command
import org.bukkit.entity.Player

class StaffChat(private val plugin: EvoriServer) : PlayerCommand() {
    override fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        val payload = StaffChatMessage(player.uniqueId.toString(), plugin.serverName, args.joinToString(" "))
        plugin.redisManager.publish("node:sc", plugin.json.encodeToString(payload))
        return true
    }
}