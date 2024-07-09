/*
 * Copyright (c) 2024 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.data.models

/**
 * Data class representing a post.
 *
 * @property id The post unique identifier.
 * @property userId The user identifier that created the post.
 * @property title The post title.
 * @property body The post content body.
 */
data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)
