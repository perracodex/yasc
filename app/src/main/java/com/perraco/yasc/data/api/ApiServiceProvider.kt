/*
 * Copyright (c) 2024 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.data.api

import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

/**
 * Provides REST API services.
 */
class ApiServiceProvider {

    /**
     * Marker interface to signify classes that can be used as API services.
     * Used to enforce type constraints when creating API services instances.
     */
    interface ApiService

    /**
     * Creates an API service with the given access token and service class.
     *
     * @param route The base URL of the API service.
     * @param service The target API service class to be created.
     * @param converters Optional list of [Converter.Factory] instances to be used by the service.
     * @return An instance of the API service.
     */
    fun <T : ApiService> create(
        route: String,
        service: Class<T>,
        converters: List<Converter.Factory> = emptyList()
    ): T {
        val client: OkHttpClient = getBaselineClient()
            .newBuilder()
            .build()

        val builder = Retrofit.Builder()
            .baseUrl(route)
            .client(client)

        for (converter in converters) {
            builder.addConverterFactory(converter)
        }

        return builder
            .build()
            .create(service)
    }

    /**
     * Creates an [OkHttpClient] with an access token for authentication.
     *
     * @return Configured OkHttpClient.
     */
    private fun getBaselineClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain
                    .request()
                    .newBuilder()
                    .build()

                // Proceed with the new request.
                chain.proceed(request)
            }.build()
    }
}