package com.ruineko.evori.proxy.config

import com.ruineko.evori.common.Config

object ProxyConfig: Config() {
    val REDIS_HOSTNAME: String
        get() = manager.getString("redis", "hostname", default = "localhost")

    val REDIS_PORT: Int
        get() = manager.getInt("redis", "port", default = 6379)
}