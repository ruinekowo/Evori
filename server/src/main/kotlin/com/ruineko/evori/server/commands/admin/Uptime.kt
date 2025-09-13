package com.ruineko.evori.server.commands.admin

import com.ruineko.evori.common.utils.ComponentUtils
import com.ruineko.evori.server.EvoriServer
import com.ruineko.evori.server.commands.StaffCommand
import org.apache.commons.lang3.time.DurationFormatUtils
import org.bukkit.command.Command
import org.bukkit.entity.Player

class Uptime(private val plugin: EvoriServer): StaffCommand("uptime") {
    override fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        val duration = DurationFormatUtils.formatDurationWords(plugin.startTime, true, true)
        player.sendMessage(ComponentUtils.parse("<aqua>Server uptime: <yellow>$duration"))
        return true
    }
}