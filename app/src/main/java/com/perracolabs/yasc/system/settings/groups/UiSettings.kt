/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.system.settings.groups

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.perracolabs.yasc.system.settings.AbsSettingsGroup
import com.perracolabs.yasc.ui.themes.attributes.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * UI settings provider.
 *
 * @param context The application context.
 */
class UiSettings(context: Context) : AbsSettingsGroup(context = context, settingsName = SETTINGS_NAME) {
    val theme: Flow<Theme> = readOperation(settingKeyName=KEY_THEME.name) {
        Theme.parse(dataStore.data.first()[KEY_THEME])
    }

    fun theme(theme: Theme) {
        writeOperation(settingKeyName=KEY_THEME.name) { dataStore.edit { it[KEY_THEME] = theme.id } }
    }

    companion object {
        private const val SETTINGS_NAME = "ui_settings"
        private val KEY_THEME = intPreferencesKey(name="theme")
    }
}
