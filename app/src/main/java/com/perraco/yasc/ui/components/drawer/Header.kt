/*
 * Copyright (c) 2023-Present Perraco. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui.components.drawer

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.perraco.yasc.R
import com.perraco.yasc.ui.LocalDrawerState
import com.perraco.yasc.ui.LocalNavHostController
import com.perraco.yasc.ui.components.buttons.ImageActionButton
import com.perraco.yasc.ui.navigation.Route
import com.perraco.yasc.ui.navigation.navigateSingleTop
import com.perraco.yasc.ui.themes.ThemeLayout
import com.perraco.yasc.ui.themes.viewmodel.IThemeViewModel
import com.perraco.yasc.ui.themes.viewmodel.obtainThemeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Renders the header section of the drawer.
 */
@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.drawer_header_height))
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(dimensionResource(id = R.dimen.drawer_header_padding))
    ) {
        Logo()

        Row(modifier = Modifier.align(alignment = Alignment.TopEnd)) {
            ThemeButton()
            SettingsButton()
        }
    }
}

/**
 * Renders the header icon and label.
 */
@Composable
private fun Logo() {
    val iconPainter: VectorPainter = rememberVectorPainter(image = Icons.Default.AccountCircle)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(all = dimensionResource(id = R.dimen.drawer_header_padding))
    ) {
        Image(
            painter = iconPainter,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.surfaceTint),
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.drawer_logo_size))
                .clip(CircleShape)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier.offset(x = 0.dp, y = 8.dp)
        )
    }
}

/**
 * Renders the settings button.
 */
@Composable
private fun SettingsButton() {
    val scope: CoroutineScope = rememberCoroutineScope()
    val icon: VectorPainter = rememberVectorPainter(image = ImageVector.vectorResource(R.drawable.ic_settings))
    val navController: NavHostController = LocalNavHostController.current
    val drawerState: DrawerState = LocalDrawerState.current

    ImageActionButton(icon = icon) {
        scope.launch {
            delay(timeMillis = 150L)

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
    val scope: CoroutineScope = rememberCoroutineScope()
    val themeViewModel: IThemeViewModel = obtainThemeViewModel()
    val currentTheme by themeViewModel.theme
    val isSystemDark: Boolean = isSystemInDarkTheme()
    val icon: VectorPainter = rememberVectorPainter(image = ImageVector.vectorResource(id = currentTheme.icon(isSystemDark)))
    val view: View = LocalView.current.rootView

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
        DrawerHeader()
    }
}
