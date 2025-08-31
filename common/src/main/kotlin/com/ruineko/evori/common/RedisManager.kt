package com.ruineko.evori.common

import kotlinx.serialization.Serializable
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPubSub

@Serializable
data class RegisterMessage(val hostname: String, val port: Int, val type: String, val mode: String)

@Serializable
data class UnregisterMessage(val serverName: String, val type: String)

@Serializable
data class AckMessage(val serverName: String, val hostname: String, val port: Int)

@Serializable
data class StaffChatMessage(val uuid: String, val node: String?, val message: String)

class RedisManager(
    host: String,
    port: Int
) {
    val pool = JedisPool(host, port)

    fun publish(channel: String, message: String) {
        pool.resource.use { jedis ->
            jedis.publish(channel, message)
        }
    }

    fun subscribe(channel: String, handler: (String) -> Unit) {
        Thread {
            pool.resource.use { jedis ->
                jedis.subscribe(object : JedisPubSub() {
                    override fun onMessage(ch: String, msg: String) {
                        handler(msg)
                    }
                }, channel)
            }
        }.start()
    }

    fun hset(key: String, map: Map<String, String>) {
        pool.resource.use { jedis ->
            jedis.hset(key, map)
        }
    }

    fun del(key: String) {
        pool.resource.use { jedis ->
            jedis.del(key)
        }
    }

    fun close() {
        pool.close()
    }
}