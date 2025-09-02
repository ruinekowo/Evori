package com.ruineko.evori.server.extensions

import com.ruineko.evori.common.utils.ComponentUtils
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

fun CommandSender.success(message: String) {
    sendMessage(ComponentUtils.parse("<green>$message"))
}

fun CommandSender.warning(message: String) {
    sendMessage(ComponentUtils.parse("<yellow>$message"))
}

fun CommandSender.severe(message: String) {
    sendMessage(ComponentUtils.parse("<red>$message"))
}

fun messageAll(message: String) {
    Bukkit.getOnlinePlayers().forEach { player -> player.sendMessage(ComponentUtils.parse(message)) }
}