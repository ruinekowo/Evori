package com.ruineko.evori.common.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

object ComponentUtils {
    private val miniMessage: MiniMessage = MiniMessage.miniMessage()

    fun parse(input: String): Component {
        return miniMessage.deserialize(input)
    }

    fun parseWithPlaceholder(input: String, vararg resolvers: TagResolver): Component {
        return miniMessage.deserialize(input, TagResolver.resolver(*resolvers))
    }
}