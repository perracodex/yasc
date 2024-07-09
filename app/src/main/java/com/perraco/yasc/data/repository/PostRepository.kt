/*
 * Copyright (c) 2024 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.data.repository

import com.perraco.yasc.data.api.ApiServiceProvider
import com.perraco.yasc.data.api.Routes
import com.perraco.yasc.data.domains.PostApiService
import com.perraco.yasc.data.models.Post
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(apiServiceProvider: ApiServiceProvider) {
    private val apiService: PostApiService = apiServiceProvider.create(
        route = Routes.BASE_POSTS,
        service = PostApiService::class.java
    )

    suspend fun getPosts(start: Int, limit: Int): List<Post> {
        val posts: List<Post> = apiService.getPosts(start = start, limit = limit)

        return posts.map { post ->
            post.copy(
                title = post.title.toTitleCase(),
                body = post.body
            )
        }
    }
}

private fun String.toTitleCase(locale: Locale = Locale.getDefault()): String {
    val words: List<String> = this.split(" ")

    val titleCasedWords: List<String> = words.map { word ->
        word.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase(locale) else char.toString()
        }
    }

    return titleCasedWords.joinToString(separator = " ")
}
