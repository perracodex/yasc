/*
 * Copyright (c) 2024 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.data.models

/**
 * Data class representing a joke.
 *
 * @property id The joke unique identifier.
 * @property type The [Type] of joke.
 * @property joke The joke content, if [type] is [Type.SINGLE].
 * @property setup The joke setup, if [type] is [Type.TWO_PART].
 * @property delivery The joke punchline, if [type] is [Type.TWO_PART].
 */
data class Joke(
    var id: Int,
    val type: Type,
    var joke: String? = null,
    var setup: String? = null,
    var delivery: String? = null
) {
    enum class Type(val value: String) {
        SINGLE(value = "single"),
        TWO_PART(value = "twopart");

        companion object {
            fun fromString(value: String): Type = entries.firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Unexpected type value: $value")
        }
    }
}
