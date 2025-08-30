package com.ruineko.evori.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

fun CommandSender.success(message: String) {
    sendMessage("§a$message")
}

fun CommandSender.warning(message: String) {
    sendMessage("§e$message")
}

fun CommandSender.severe(message: String) {
    sendMessage("§c$message")
}

fun CommandSender.message(message: String, color: NamedTextColor) {
    sendMessage(Component.text(message, color))
}

fun messageAll(message: String, color: NamedTextColor) {
    Bukkit.getOnlinePlayers().forEach { player -> player.sendMessage(Component.text(message, color)) }
}