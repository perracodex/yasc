/*
 * Copyright (c) 2023-Present Perraco. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui.screens.help

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.perraco.yasc.ui.navigation.Route

/**
 * Adds [HelpScreen] to NavGraph for easier reuse, readability, and maintainability.
 */
fun NavGraphBuilder.helpScreen() {
    composable(route = Route.HELP.name) {
        HelpScreen()
    }
}
