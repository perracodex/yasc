/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.perracolabs.yasc.R
import com.perracolabs.yasc.ui.LocalDrawerState
import com.perracolabs.yasc.ui.components.bars.AppBarAction
import com.perracolabs.yasc.ui.components.bars.MainAppBar
import com.perracolabs.yasc.ui.navigation.Route
import com.perracolabs.yasc.ui.themes.ThemeLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val onAction = remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar(onAction = onAction) },
        content = { innerPadding -> Content(innerPadding) }
    )

    if (onAction.value) {
        ShowMessage(onDismiss = {
            onAction.value = false
        })
    }
}

@Composable
private fun TopBar(onAction: MutableState<Boolean>) {
    val drawerState: DrawerState = LocalDrawerState.current
    val scope: CoroutineScope = rememberCoroutineScope()

    val navigationAction: AppBarAction = remember {
        AppBarAction(iconId = R.drawable.ic_menu, callback = {
            scope.launch {
                drawerState.open()
            }
        })
    }

    val overflowAction: AppBarAction = remember(drawerState.isClosed, onAction.value) {
        AppBarAction(iconId = R.drawable.ic_overflow, callback = {
            if (drawerState.isClosed)
                onAction.value = true
        })
    }

    MainAppBar(
        title = stringResource(id = Route.HOME.resourceId),
        leadingAction = navigationAction,
        trailingAction = overflowAction
    )
}

@Composable
private fun Content(innerPadding: PaddingValues) {
    var selectedItem: Int by remember { mutableIntStateOf(0) }
    val items: List<String> = listOf("Songs", "Artists", "Playlists")

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        NavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        }
    }
}

@Composable
private fun ShowMessage(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_radius)),
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_radius))
            ) {
                Text(text = "Close")
            }
        },
        title = { Text(text = stringResource(id = R.string.app_name)) },
        text = { Text(text = "Hello World") }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    ThemeLayout {
        HomeScreen()
    }
}
