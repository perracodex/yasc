/*
 * Copyright (c) 2024 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.di

import com.perraco.yasc.data.api.ApiServiceProvider
import com.perraco.yasc.data.repository.JokeRepository
import com.perraco.yasc.data.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApiServiceProvider(): ApiServiceProvider {
        return ApiServiceProvider()
    }

    @Provides
    fun providePostRepository(apiServiceProvider: ApiServiceProvider): PostRepository {
        return PostRepository(apiServiceProvider)
    }

    @Provides
    fun provideJokeRepository(apiServiceProvider: ApiServiceProvider): JokeRepository {
        return JokeRepository(apiServiceProvider)
    }
}
