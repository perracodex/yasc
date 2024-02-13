/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/**
 * Extension function for NavController that navigates to a specified destination,
 * ensuring that it becomes the single instance of that route in the back stack.
 *
 * @param route The name of the destination route to navigate to.
 * @param navigatorExtras Optional Navigator.Extras to customize the transition.
 */
fun NavController.navigateSingleTop(route: Route, navigatorExtras: Navigator.Extras? = null) {
    val navOptions: NavOptions = NavOptions.Builder()
        .setLaunchSingleTop(singleTop = true)
        .setPopUpTo(route = route.name, inclusive = false)
        .build()

    this.navigate(
        route = route.name,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras
    )
}
