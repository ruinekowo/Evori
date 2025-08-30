plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.2.0"
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    api("org.spongepowered:configurate-core:4.2.0")
    api("org.spongepowered:configurate-yaml:4.2.0")
    api("redis.clients:jedis:6.2.0")
}

tasks.jar {
    enabled = false
}