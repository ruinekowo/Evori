package com.ruineko.evori.server.commands.player

import com.ruineko.evori.common.utils.ComponentUtils
import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.commands.PlayerCommand
import org.bukkit.command.Command
import org.bukkit.entity.Player

class About(private val plugin: EvoriServer) : PlayerCommand() {
    override fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        player.sendMessage(ComponentUtils.parseString("<aqua>This server is running ${plugin.name} [${plugin.pluginMeta.version}]"))
        return true
    }
}