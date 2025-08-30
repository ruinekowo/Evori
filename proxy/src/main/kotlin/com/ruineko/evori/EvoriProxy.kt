package com.ruineko.evori

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger
import javax.inject.Inject

class EvoriProxy @Inject constructor(
    private val server: ProxyServer,
    private val logger: Logger
) {
    @Subscribe
    fun onInit(event: ProxyInitializeEvent) {
        logger.info("Proxy enabled!")
    }
}