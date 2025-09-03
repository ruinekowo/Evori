package com.ruineko.evori.common.utils

object StringUtils {
    fun randomId(length: Int = 6): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length).map { chars.random() }.joinToString("")
    }
}