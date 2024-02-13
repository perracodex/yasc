/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.themes.attributes

import androidx.compose.ui.graphics.vector.ImageVector
import com.perracolabs.yasc.R

/**
 * The different themes available in the application.
 *
 * @param id The identifier for each theme.
 */
enum class Theme(val id: Int) {
    SYSTEM(id = 0),
    LIGHT(id = 1),
    DARK(id = 2);

    companion object {
        /**
         * Parses a theme identifier to its corresponding [Theme].
         * Defaults to [SYSTEM] if the identifier doesn't match any existing theme.
         *
         * @param id The identifier of the theme to parse.
         * @return The parsed [Theme].
         */
        fun parse(id: Int?): Theme = entries.find { it.id == id } ?: SYSTEM
    }

    /**
     * Determines the icon for each theme. Note that this is the reverse as
     * a theme icon must reflect to which theme will change, so Dark -> Light, and so on.
     *
     * @param isSystemDark Flag indicating whether the system theme is dark.
     * @return The resolved [ImageVector] icon.
     */
    fun icon(isSystemDark: Boolean): Int {
        return when (this) {
            DARK -> R.drawable.ic_theme_light
            LIGHT -> R.drawable.ic_theme_dark
            SYSTEM -> if (isSystemDark) R.drawable.ic_theme_light else R.drawable.ic_theme_dark
        }
    }

    /**
     * Determines the reverse theme based on the current one. So for Dark -> Light, and so on.
     *
     * @param isSystemDark Flag indicating whether the system theme is dark.
     * @return The [Theme] that represents the reverse theme.
     */
    fun reverseTheme(isSystemDark: Boolean): Theme {
        return when (this) {
            DARK -> LIGHT
            LIGHT -> DARK
            SYSTEM -> if (isSystemDark) LIGHT else DARK
        }
    }
}
