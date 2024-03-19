/*
 * Copyright (c) 2023-Present Perraco. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui.screens.home

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.perraco.yasc.R
import com.perraco.yasc.ui.LocalDrawerState
import com.perraco.yasc.ui.components.bars.AppBarAction
import com.perraco.yasc.ui.components.bars.MainAppBar
import com.perraco.yasc.ui.navigation.Route
import com.perraco.yasc.ui.themes.ThemeLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val items: List<String> = listOf("Songs", "Artists", "Playlists")
    val onAction = remember { mutableStateOf(false) }

    // These should be moved to a dedicated ViewModel instead, left here just for the example simplicity.
    val selectedItem: MutableState<Int> = remember { mutableIntStateOf(0) }
    val selectedItemText: MutableState<String> = remember { mutableStateOf(items[0]) }

    // Observe changes in selectedItem and update selectedItemText accordingly.
    LaunchedEffect(selectedItem.value) {
        selectedItemText.value = items[selectedItem.value]
    }

    Scaffold(
        topBar = { TopBar(onAction = onAction) },
        content = { innerPadding -> Content(innerPadding, text=selectedItemText) },
        bottomBar = { BottomBar(selectedItem = selectedItem, items = items) }
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
private fun Content(innerPadding: PaddingValues, text: MutableState<String>) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Text(
            modifier = Modifier.align(alignment = androidx.compose.ui.Alignment.Center),
            text = text.value
        )
    }
}

@Composable
private fun BottomBar(selectedItem: MutableState<Int>, items: List<String>) {

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem.value == index,
                onClick = { selectedItem.value = index }
            )
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
        text = { Text(text = "Hello Yasc") }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    ThemeLayout {
        HomeScreen()
    }
}
