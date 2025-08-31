package com.ruineko.evori.server.commands

import com.ruineko.evori.server.extensions.severe
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class PlayerCommand : CommandExecutor {
    abstract fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.severe("This command can only be used by a player.")
            return true
        }
        return run(sender, command, label, args)
    }
}