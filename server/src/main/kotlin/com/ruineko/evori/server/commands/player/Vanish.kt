package com.ruineko.evori.server.commands.player

import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.commands.PlayerCommand
import com.ruineko.evori.server.config.MessageConfig
import com.ruineko.evori.server.extensions.error
import com.ruineko.evori.server.objects.EvoriPlayerManager
import org.bukkit.command.Command
import org.bukkit.entity.Player

class Vanish(private val plugin: EvoriServer): PlayerCommand() {
    override fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        if (plugin.protocolLibAvailable) {
            player.error("${MessageConfig.PROTOCOLLIB_UNAVAILABLE} ${MessageConfig.COMMAND_DISABLED}")
            return true
        }

        EvoriPlayerManager.setVanished(player, !EvoriPlayerManager.get(player.uniqueId).isVanished)
        return true
    }
}