package com.ruineko.evori.server.commands.player

import com.ruineko.evori.server.commands.PlayerCommand
import com.ruineko.evori.server.extensions.success
import org.bukkit.command.Command
import org.bukkit.entity.Player

class Fly : PlayerCommand() {
    override fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        player.allowFlight = !player.allowFlight
        player.isFlying = player.allowFlight
        player.success("Turned ${if (player.allowFlight) "on" else "off"} flight!")
        return true
    }
}