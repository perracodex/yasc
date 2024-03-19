/*
 * Copyright (c) 2023-Present Perraco. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.perraco.yasc.R
import com.perraco.yasc.ui.LocalNavHostController
import com.perraco.yasc.ui.components.bars.AppBarAction
import com.perraco.yasc.ui.components.bars.MainAppBar
import com.perraco.yasc.ui.navigation.Route
import com.perraco.yasc.ui.themes.ThemeLayout

@Composable
fun SettingsScreen() {
    Scaffold(
        topBar = { TopBar() },
        content = { innerPadding -> Content(innerPadding) }
    )
}

@Composable
private fun TopBar() {
    val navController: NavController = LocalNavHostController.current

    val navigationAction: AppBarAction = remember {
        AppBarAction(iconId = R.drawable.ic_arrow_back, callback = {
            navController.popBackStack()
        })
    }

    MainAppBar(
        title = stringResource(id = Route.SETTINGS.resourceId),
        leadingAction = navigationAction,
        trailingAction = null
    )
}

@Composable
private fun Content(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Text(
            modifier = Modifier.align(alignment = Alignment.Center),
            text = "Settings Screen"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    ThemeLayout {
        SettingsScreen()
    }
}
