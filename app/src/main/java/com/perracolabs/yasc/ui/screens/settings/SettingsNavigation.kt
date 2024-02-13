/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.screens.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.perracolabs.yasc.ui.navigation.Route

/**
 * Adds [SettingsScreen] to NavGraph for easier reuse, readability, and maintainability.
 */
fun NavGraphBuilder.settingsScreen() {
    composable(route = Route.SETTINGS.name) {
        SettingsScreen()
    }
}
