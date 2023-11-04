/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.screens.help

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.perracolabs.yasc.ui.navigation.Route

/**
 * Adds [HelpScreen] to NavGraph for easier reuse, readability, and maintainability.
 *
 * @param navController Reference to the navigation [NavController].
 */
fun NavGraphBuilder.helpScreen(navController: NavController) {
    composable(route = Route.HELP.name) {
        HelpScreen(navController = navController)
    }
}
