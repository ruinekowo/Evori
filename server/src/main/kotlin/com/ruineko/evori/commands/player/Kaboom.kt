package com.ruineko.evori.commands.player

import com.ruineko.evori.EvoriServer
import com.ruineko.evori.commands.PlayerCommand
import com.ruineko.evori.extensions.success
import org.bukkit.command.Command
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

class Kaboom(private val plugin: EvoriServer) : PlayerCommand() {
    override fun run(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        player.world.players.forEach {
            it.velocity = it.velocity.setY(64)
            it.setMetadata("kaboom", FixedMetadataValue(plugin, true))
            player.world.strikeLightningEffect(it.location)
        }
        player.success("Kaboom!")
        return true
    }
}