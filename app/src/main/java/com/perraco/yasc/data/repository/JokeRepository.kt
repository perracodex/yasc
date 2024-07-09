/*
 * Copyright (c) 2024 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.data.repository

import com.google.gson.*
import com.perraco.yasc.data.api.ApiServiceProvider
import com.perraco.yasc.data.api.Routes
import com.perraco.yasc.data.domains.JokeApiService
import com.perraco.yasc.data.models.Joke
import com.perraco.yasc.data.models.JokeResponse
import com.perraco.yasc.system.debug.Tracer
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeRepository @Inject constructor(apiServiceProvider: ApiServiceProvider) {
    private val jokeTypeGson: Gson = GsonBuilder()
        .registerTypeAdapter(Joke.Type::class.java, JokeTypeAdapter())
        .create()

    private val apiService: JokeApiService = apiServiceProvider.create(
        route = Routes.BASE_JOKES,
        service = JokeApiService::class.java,
        converters = listOf(GsonConverterFactory.create(jokeTypeGson))
    )

    suspend fun getJokes(startId: Int, endId: Int, amount: Int): List<Joke> {
        val idRange = "$startId-$endId"

        try {
            val response: JokeResponse = apiService.getJokes(idRange = idRange, amount = amount)

            if (response.error && response.code != NO_CONTENT_IN_RANGE) {
                Tracer.e(TAG, "API error: $response")
                throw Exception("API error: Unable to fetch jokes. (${response.code})")
            }

            return response.jokes ?: emptyList()
        } catch (e: Exception) {
            throw Exception("Network error or invalid response.", e)
        }
    }

    companion object {
        private const val TAG = "JokeRepository"
        private const val NO_CONTENT_IN_RANGE = 106
    }
}

/**
 * Custom JSON adapter for [Joke.Type].
 */
private class JokeTypeAdapter : JsonDeserializer<Joke.Type>, JsonSerializer<Joke.Type> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Joke.Type {
        return Joke.Type.fromString(json!!.asString)
    }

    override fun serialize(src: Joke.Type, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.value.lowercase())
    }
}
