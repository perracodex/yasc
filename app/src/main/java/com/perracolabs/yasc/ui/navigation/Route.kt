/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.perracolabs.yasc.R

/**
 * The different main navigation routes within the application.
 *
 * @param resourceId The resource ID corresponding to the string description of the route.
 * @param icon The ImageVector that represents the icon for this route.
 */
enum class Route(@StringRes val resourceId: Int, @DrawableRes val icon: Int) {
    HOME(resourceId = R.string.menu_home, icon = R.drawable.ic_home),
    DETAIL(resourceId = R.string.menu_detail, icon = R.drawable.ic_detail),
    SETTINGS(resourceId = R.string.menu_settings, icon = R.drawable.ic_settings),
    HELP(resourceId = R.string.menu_help, icon = R.drawable.ic_help);

    companion object {
        /**
         * Parses a route name to its corresponding [Route].
         * If the route name does not match any existing route, it defaults to [HOME].
         *
         * @param name The name of the route to parse.
         * @return The parsed [Route].
         */
        fun parse(name: String?): Route = Route.entries.find { it.name == name } ?: HOME
    }
}
