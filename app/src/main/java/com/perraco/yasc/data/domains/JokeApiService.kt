/*
 * Copyright (c) 2024 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.data.domains

import com.perraco.yasc.data.api.ApiServiceProvider
import com.perraco.yasc.data.api.Routes
import com.perraco.yasc.data.models.JokeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JokeApiService : ApiServiceProvider.ApiService {
    @GET(Routes.JOKES)
    suspend fun getJokes(@Query("idRange") idRange: String, @Query("amount") amount: Int): JokeResponse
}
