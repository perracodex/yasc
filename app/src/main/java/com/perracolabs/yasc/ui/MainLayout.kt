/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui

import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.perracolabs.yasc.ui.components.drawer.DrawerSheet
import com.perracolabs.yasc.ui.navigation.Route
import com.perracolabs.yasc.ui.screens.detail.detailScreen
import com.perracolabs.yasc.ui.screens.help.helpScreen
import com.perracolabs.yasc.ui.screens.home.homeScreen
import com.perracolabs.yasc.ui.screens.settings.settingsScreen
import com.perracolabs.yasc.ui.themes.ThemeLayout
import kotlinx.coroutines.launch

/**
 * A Composable function to serve as the main layout of the application.
 * It includes a drawer and a navigation host to manage screen transitions.
 */
@Composable
fun MainLayout(initialDrawerState: DrawerValue = DrawerValue.Closed) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = initialDrawerState)
    val scope = rememberCoroutineScope()

    CompositionLocalProvider(
        LocalNavHostController provides navController,
        LocalDrawerState provides drawerState
    ) {
        ThemeLayout {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = { DrawerSheet() }
            ) {
                NavHost(navController = navController, startDestination = Route.HOME.name) {
                    homeScreen()
                    detailScreen()
                    settingsScreen()
                    helpScreen()
                }
            }
        }
    }

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }
}

val LocalDrawerState = compositionLocalOf<DrawerState> { error("No DrawerState found.") }
val LocalNavHostController = compositionLocalOf<NavHostController> { error("No NavHostController found.") }

@Preview(showBackground = true)
@Composable
fun PreviewMainApp() {
    MainLayout()
}
