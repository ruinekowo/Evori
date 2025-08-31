package com.ruineko.evori.common.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object ComponentUtils {
    private val miniMessage: MiniMessage = MiniMessage.miniMessage()

    fun parseString(string: String): Component {
        return miniMessage.deserialize(string)
    }
}