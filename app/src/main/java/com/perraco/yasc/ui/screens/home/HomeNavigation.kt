/*
 * Copyright (c) 2023-Present Perraco. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui.screens.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.perraco.yasc.ui.navigation.Route

/**
 * Adds [HomeScreen] to NavGraph for easier reuse, readability, and maintainability.
 */
fun NavGraphBuilder.homeScreen() {
    composable(route = Route.HOME.name) {
        HomeScreen()
    }
}
