package com.ruineko.evori.server.config

import com.ruineko.evori.common.Config

object ChatConfig: Config() {
    val FORMAT: String
        get() = manager.getString("pattern", default = "<gray><player_name>: <message>")
}