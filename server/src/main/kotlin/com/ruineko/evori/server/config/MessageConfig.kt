package com.ruineko.evori.server.config

import com.ruineko.evori.common.config.Config

object MessageConfig: Config() {
    val PLUGIN_ENABLE: String
        get() = manager.getString("plugin", "enable", default = "Plugin ignition successful.")

    val PLUGIN_DISABLE: String
        get() = manager.getString("plugin", "disable", default = "Plugin powered down. See you in the void.")
}