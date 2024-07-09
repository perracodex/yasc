/*
 * Copyright (c) 2023-Present Perraco. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui

import androidx.activity.compose.BackHandler
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.perraco.yasc.ui.components.drawer.DrawerSheet
import com.perraco.yasc.ui.navigation.Route
import com.perraco.yasc.ui.screens.detail.detailScreen
import com.perraco.yasc.ui.screens.help.helpScreen
import com.perraco.yasc.ui.screens.home.homeScreen
import com.perraco.yasc.ui.screens.settings.settingsScreen
import com.perraco.yasc.ui.themes.ThemeLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A Composable function to serve as the main layout of the application.
 * It includes a drawer and a navigation host to manage screen transitions.
 */
@Composable
fun MainLayout(initialDrawerState: DrawerValue = DrawerValue.Closed) {
    val navController: NavHostController = rememberNavController()
    val drawerState: DrawerState = rememberDrawerState(initialValue = initialDrawerState)
    val scope: CoroutineScope = rememberCoroutineScope()
    val snackbarHostState: SnackbarHostState by remember { mutableStateOf(SnackbarHostState()) }

    CompositionLocalProvider(
        LocalNavHostController provides navController,
        LocalDrawerState provides drawerState,
        LocalSnackbarHostState provides snackbarHostState
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
val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> { error("No SnackbarHostState found.") }

@Preview(showBackground = true)
@Composable
fun PreviewMainApp() {
    MainLayout()
}
