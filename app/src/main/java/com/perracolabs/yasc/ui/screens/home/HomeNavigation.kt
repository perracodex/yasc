/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.screens.home

import androidx.compose.material3.DrawerState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.perracolabs.yasc.ui.navigation.Route

/**
 * Adds [HomeScreen] to NavGraph for easier reuse, readability, and maintainability.
 *
 * @param drawerState Reference to the drawer [DrawerState].
 */
fun NavGraphBuilder.homeScreen(drawerState: DrawerState) {
    composable(route = Route.HOME.name) {
        HomeScreen(drawerState = drawerState)
    }
}
