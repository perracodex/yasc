/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.components.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.perracolabs.yasc.R
import com.perracolabs.yasc.ui.components.buttons.ImageActionButton
import com.perracolabs.yasc.ui.navigation.Route
import com.perracolabs.yasc.ui.navigation.navigateSingleTop
import com.perracolabs.yasc.ui.themes.ThemeLayout
import com.perracolabs.yasc.ui.themes.viewmodel.obtainThemeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Renders the header section of the drawer.
 *
 * @param navController The NavController used for navigation.
 * @param drawerState The DrawerState representing the state of the drawer (open/closed).
 */
@Composable
fun DrawerHeader(navController: NavController, drawerState: DrawerState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.drawer_header_height))
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(dimensionResource(id = R.dimen.drawer_header_padding))
    ) {
        Logo()

        Row(modifier = Modifier.align(Alignment.TopEnd)) {
            ThemeButton()
            SettingsButton(navController = navController, drawerState = drawerState)
        }
    }
}

/**
 * Renders the header icon and label.
 */
@Composable
private fun Logo() {
    val iconPainter = rememberVectorPainter(image = Icons.Default.AccountCircle)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(dimensionResource(id = R.dimen.drawer_header_padding))
    ) {
        Image(
            painter = iconPainter,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceTint),
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.drawer_logo_size))
                .clip(CircleShape)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier.offset(0.dp, 8.dp)
        )
    }
}

/**
 * Renders the settings button.
 *
 * @param navController The NavController used for navigation.
 * @param drawerState The DrawerState representing the state of the drawer (open/closed).
 */
@Composable
private fun SettingsButton(
    navController: NavController,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    val icon = rememberVectorPainter(image = ImageVector.vectorResource(R.drawable.ic_settings))

    ImageActionButton(icon = icon) {
        scope.launch {
            delay(150)

            navController.navigateSingleTop(
                route = Route.SETTINGS,
                navigatorExtras = null
            )

            drawerState.close()
        }
    }
}

@Composable
private fun ThemeButton() {
    val scope = rememberCoroutineScope()
    val themeViewModel = obtainThemeViewModel()
    val currentTheme by themeViewModel.theme
    val isSystemDark = isSystemInDarkTheme()
    val icon = rememberVectorPainter(image = ImageVector.vectorResource(currentTheme.icon(isSystemDark)))
    val view = LocalView.current.rootView

    ImageActionButton(icon = icon, showIndication = false) { centerOffset ->
        scope.launch {
            val newTheme = currentTheme.reverseTheme(isSystemDark)
            themeViewModel.request(
                newTheme = newTheme,
                themeLayoutView = view,
                transitionOrigin = centerOffset
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDrawerHeader() {
    ThemeLayout {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        DrawerHeader(navController, drawerState)
    }
}
