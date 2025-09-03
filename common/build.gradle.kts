plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.2.0"
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-reflect:2.2.10")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    api("net.kyori:adventure-api:4.24.0")
    api("net.kyori:adventure-text-minimessage:4.24.0")
    api("org.spongepowered:configurate-core:4.2.0")
    api("org.spongepowered:configurate-yaml:4.2.0")
    api("redis.clients:jedis:6.2.0")
}