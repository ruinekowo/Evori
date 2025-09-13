package com.ruineko.evori.server.config

import com.ruineko.evori.common.config.Config

object MessageConfig: Config() {
    val PLUGIN_ENABLED: String
        get() = manager.getString("plugin", "enable", default = "Plugin ignition successful.")

    val PLUGIN_DISABLED: String
        get() = manager.getString("plugin", "disable", default = "Plugin powered down. See you in the void.")

    val COMMAND_DISABLED: String
        get() = manager.getString("command", "disabled", default = "This command have been disabled.")

    val PROTOCOLLIB_UNAVAILABLE: String
        get() = manager.getString("dependencies", "protocollib", "unavailable", default = "ProtocolLib is not available.")

    val MOUNT_LIMIT_REACHED: String
        get() = manager.getString("mount", "limit", default = "The limit of players who can ride has been reached.")
}