package com.ruineko.evori.server.listeners

import com.ruineko.evori.common.utils.ComponentUtils
import com.ruineko.evori.server.config.ChatConfig
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatListener: Listener {
    @EventHandler
    fun onAsyncChat(event: AsyncChatEvent) {
        val formatted = ComponentUtils.parseWithPlaceholder(
            ChatConfig.FORMAT,
            Placeholder.unparsed("player_name", event.player.name),
            Placeholder.component("message", event.message())
        )

        event.renderer { _, _, _, _ -> formatted }
    }
}