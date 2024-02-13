/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.components.drawer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.perracolabs.yasc.R
import com.perracolabs.yasc.ui.navigation.Route
import com.perracolabs.yasc.ui.navigation.navigateSingleTop
import com.perracolabs.yasc.ui.themes.ThemeLayout
import kotlinx.coroutines.launch

/**
 * Renders the content section of the drawer. This is the list of action items.
 *
 * @param navController The NavController used for navigation.
 * @param drawerState The DrawerState representing the state of the drawer (open/closed).
 */
@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: DrawerState
) {
    val selectedItem = remember { mutableStateOf(Route.HOME) }

    LaunchedEffect(navController) {
        // Keep the selected drawer item in sync with the current navigation destination.
        // This ensures accurate drawer item selection even when navigation occurs through
        // other methods, such as like the back button.
        navController.currentBackStackEntryFlow.collect { backStackEntry: NavBackStackEntry ->
            val currentRoute = Route.parse(name = backStackEntry.destination.route)
            selectedItem.value = currentRoute
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = dimensionResource(id = R.dimen.drawer_padding_vertical))
    ) {
        listOf(Route.HOME, Route.DETAIL).forEach { item ->
            DrawerItem(
                route = item,
                navController = navController,
                drawerState = drawerState,
                selectedItem = selectedItem
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        DrawerItem(
            route = Route.HELP,
            navController = navController,
            drawerState = drawerState,
            selectedItem = selectedItem
        )
    }
}

/**
 * A DrawerItem Composable to display individual navigation items.
 *
 * @param route The navigation route associated with this item.
 * @param navController The NavController used for navigation.
 * @param drawerState The DrawerState representing the state of the drawer (open/closed).
 * @param selectedItem The mutable state that tracks the currently selected item.
 */
@Composable
private fun DrawerItem(
    route: Route,
    navController: NavController,
    drawerState: DrawerState,
    selectedItem: MutableState<Route>,
) {
    val scope = rememberCoroutineScope()

    NavigationDrawerItem(
        icon = {
            Icon(
                painter = painterResource(id = route.icon),
                modifier = Modifier.size(dimensionResource(id = R.dimen.drawer_item_icon_size)),
                contentDescription = null
            )
        },
        label = { Text(stringResource(id = route.resourceId)) },
        selected = (selectedItem.value == route),
        onClick = {
            scope.launch {
                navController.navigateSingleTop(
                    route = route,
                    navigatorExtras = null
                )

                drawerState.close()
            }
            selectedItem.value = route
        },
        modifier = Modifier.padding(
            horizontal = dimensionResource(id = R.dimen.drawer_padding_horizontal),
            vertical = dimensionResource(id = R.dimen.drawer_padding_vertical)
        ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_radius)),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            unselectedContainerColor = MaterialTheme.colorScheme.secondary,
            selectedIconColor = MaterialTheme.colorScheme.surfaceTint,
            selectedTextColor = MaterialTheme.colorScheme.surfaceTint,
            unselectedIconColor = MaterialTheme.colorScheme.tertiary,
            unselectedTextColor = MaterialTheme.colorScheme.tertiary,
            selectedBadgeColor = MaterialTheme.colorScheme.surfaceVariant,
            unselectedBadgeColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDrawerContent() {
    ThemeLayout {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        DrawerContent(navController = navController, drawerState = drawerState)
    }
}
