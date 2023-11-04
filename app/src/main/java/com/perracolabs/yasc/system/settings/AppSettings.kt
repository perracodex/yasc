/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.system.settings

import android.content.Context
import com.perracolabs.yasc.system.settings.groups.UiSettings

/**
 * Singleton application settings provider work as a facade for all the related application settings.
 *
 * Added to the context as an extension property.
 */
class AppSettings private constructor(context: Context) {

    val uiSettings: UiSettings = UiSettings(context)

    companion object {
        private var instance: AppSettings? = null

        operator fun invoke(context: Context): AppSettings {
            return instance ?: synchronized(this) {
                instance ?: AppSettings(context).also { instance = it }
            }
        }
    }
}

val Context.appSettings: AppSettings
    get() = AppSettings(this)
