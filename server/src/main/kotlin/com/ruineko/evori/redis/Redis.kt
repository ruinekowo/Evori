package com.ruineko.evori.redis

import com.ruineko.evori.EvoriServer
import com.ruineko.evori.extensions.int
import com.ruineko.evori.extensions.string
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.json.JSONObject
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub
import java.net.InetAddress
import java.util.UUID
import kotlin.concurrent.thread

class Redis(private val plugin: EvoriServer) {
    var jedis: Jedis? = null
    var subscriber: Jedis? = null
    private var pubSub: JedisPubSub? = null

    lateinit var nodeId: String
    lateinit var nodeKey: String

    fun connect() {
        nodeId = randomId()
        nodeKey = "nodes:$nodeId"

        jedis = Jedis(plugin.configNode.string("redis.host"), plugin.configNode.int("redis.port"))
        subscriber = Jedis(plugin.configNode.string("redis.host"), plugin.configNode.int("redis.port"))

        pubSub = object : JedisPubSub() {
            override fun onMessage(channel: String?, message: String?) {
                if (message != null) {
                    plugin.logger.info("[Redis] $channel: $message")

                    if (channel == "sc") {
                        val payload = JSONObject(message)
                        val uuid = payload.getString("uuid")
                        val node = payload.getString("node")
                        val message = payload.getString("message")

                        val player = Bukkit.getPlayer(UUID.fromString(uuid))
                        val name = player?.name ?: uuid

                        Bukkit.getOnlinePlayers().forEach {
                            it.sendMessage("§b[STAFF] §e[CHAT] §7[$node] §c$name§f: $message")
                        }
                    }

                    if (channel == "node:$nodeId") {
                        val payload = JSONObject(message)
                        val action = payload.getString("action")
                        val requestId = payload.getString("requestId")
                        val replyTo = payload.getString("replyTo")

                        if (action == "getPlayers") {
                            val players = Bukkit.getOnlinePlayers().map { it.name }
                            val response = JSONObject()
                            response.put("requestId", requestId)
                            response.put("players", players)

                            publish(replyTo, response.toString())
                        }
                    }
                }
            }
        }

        thread(start = true, isDaemon = true) {
            try {
                subscriber?.subscribe(pubSub, "sc", "node:$nodeId")
            } catch (e: Exception) {
                plugin.logger.severe(e.message)
            } finally {
                subscriber?.close()
            }
        }
    }

    fun publish(channel: String, message: String) {
        Jedis(plugin.configNode.string("redis.host"), plugin.configNode.int("redis.port")).use { jedis ->
            jedis.publish(channel, message)
        }
    }

    fun registerServer() {
        val ip = InetAddress.getLocalHost().hostAddress
        val port = plugin.server.port

        jedis?.hset(nodeKey, mapOf(
            "id" to nodeId,
            "ip" to ip,
            "port" to port.toString(),
            "maxPlayers" to plugin.server.maxPlayers.toString(),
            "currentPlayers" to "0",
            "status" to "ONLINE",
            "timestamp" to System.currentTimeMillis().toString(),
            "lastUpdate" to System.currentTimeMillis().toString()
        ))

        plugin.logger.info("Registered server $nodeKey at $ip:$port")

        object : BukkitRunnable() {
            override fun run() {
                val online = Bukkit.getOnlinePlayers().size
                jedis?.hset(nodeKey, mapOf(
                    "currentPlayers" to online.toString(),
                    "lastUpdate" to System.currentTimeMillis().toString(),
                ))
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L * 5)
    }

    fun disconnect() {
        pubSub?.unsubscribe()
        subscriber?.close()
        jedis?.del(nodeKey)
        jedis?.close()

        plugin.logger.info("Unregistered server $nodeId")
    }

    private fun randomId(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6).map { chars.random() }.joinToString("")
    }
}