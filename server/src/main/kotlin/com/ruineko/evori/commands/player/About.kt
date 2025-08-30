package com.ruineko.evori.commands.player

import com.ruineko.evori.EvoriServer
import com.ruineko.evori.commands.PlayerCommand
import com.ruineko.evori.extensions.message
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.entity.Player

class About(private val plugin: EvoriServer) : PlayerCommand() {
    override fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        player.message("This server is running ${plugin.name} [${plugin.pluginMeta.version}]", NamedTextColor.AQUA)
        return true
    }
}