package com.ruineko.evori.common.utils

object PlaceholderFormatter {
    private val colorCodes = mapOf(
        "0" to '0', "1" to '1', "2" to '2', "3" to '3',
        "4" to '4', "5" to '5', "6" to '6', "7" to '7',
        "8" to '8', "9" to '9', "a" to 'a', "b" to 'b',
        "c" to 'c', "d" to 'd', "e" to 'e', "f" to 'f',
        "r" to 'r'
    )

    fun randomId(length: Int = 6): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length).map { chars.random() }.joinToString("")
    }

    fun format(input: String): String {
        var text = input

        colorCodes.forEach { (key, value) ->
            text = text.replace("<$key>", "§$value")
        }

        val hexPattern = "<([A-Fa-f0-9]{6})>".toRegex()
        text = hexPattern.replace(text) { matchResult -> "<#${matchResult.groupValues[1]}>" }

        return text
    }
}