package com.ruineko.evori.server.extensions

import com.ruineko.evori.common.utils.ComponentUtils
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

fun CommandSender.success(message: String) {
    sendMessage(ComponentUtils.parseString("<green>$message"))
}

fun CommandSender.warning(message: String) {
    sendMessage(ComponentUtils.parseString("<yellow>$message"))
}

fun CommandSender.severe(message: String) {
    sendMessage(ComponentUtils.parseString("<red>$message"))
}

fun messageAll(message: String) {
    Bukkit.getOnlinePlayers().forEach { player -> player.sendMessage(ComponentUtils.parseString(message)) }
}