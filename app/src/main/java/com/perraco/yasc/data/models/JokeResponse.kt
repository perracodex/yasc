/*
 * Copyright (c) 2024 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.data.models

/**
 * Data class representing a joke.
 *
 * @property amount The amount of jokes in the response.
 * @property jokes The list of jokes. Empty if [error] is true.
 * @property error Indicates if the response contains an error.
 * @property code The error code. Default is 0 (no error).
 * @property causedBy The error cause. Null if [error] is false.
 * @property additionalInfo Additional information about the error. Null if [error] is false.
 */
data class JokeResponse(
    val amount: Int = 0,
    val jokes: List<Joke>? = null,
    val error: Boolean,
    val code: Int = 0,
    val causedBy: String? = null,
    val additionalInfo: String? = null,
)
