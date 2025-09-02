package com.ruineko.evori.server.commands.admin

import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.commands.StaffCommand
import com.ruineko.evori.server.extensions.success
import org.bukkit.command.Command
import org.bukkit.entity.Player

class Reload(private val plugin: EvoriServer): StaffCommand("evori.reload") {
    override fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        plugin.reloadConfigurations()
        player.success("Reloaded configurations!")
        return true
    }
}