package com.ruineko.evori.server.config

import com.ruineko.evori.common.config.Config

object ChatConfig: Config() {
    val FORMAT: String
        get() = manager.getString("format", default = "<gray><player_name>: <message>")
}