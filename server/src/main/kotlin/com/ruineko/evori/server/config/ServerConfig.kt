package com.ruineko.evori.server.config

import com.ruineko.evori.common.config.Config

object ServerConfig: Config() {
    val SERVER_HOSTNAME: String
        get() = manager.getString("server", "hostname", default = "localhost")

    val SERVER_TYPE: String
        get() = manager.getString("server", "type", default = "mini")

    val REDIS_HOSTNAME: String
        get() = manager.getString("redis", "hostname", default = "localhost")

    val REDIS_PORT: Int
        get() = manager.getInt("redis", "port", default = 6379)
}