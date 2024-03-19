/*
 * Copyright (c) 2023-Present Perraco. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perraco.yasc.ui.themes

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.perraco.yasc.ui.themes.attributes.DarkColorScheme
import com.perraco.yasc.ui.themes.attributes.LightColorScheme
import com.perraco.yasc.ui.themes.attributes.Theme
import com.perraco.yasc.ui.themes.attributes.Typography
import com.perraco.yasc.ui.themes.transition.ThemeMonitor
import com.perraco.yasc.ui.themes.viewmodel.obtainThemeViewModel

/**
 * A Composable function to apply theming based on the current settings.
 *
 * @param content The Composable content that this theme will be applied to.
 */
@Composable
fun ThemeLayout(
    content: @Composable () -> Unit
) {
    val themeViewModel = obtainThemeViewModel()
    val currentTheme = themeViewModel.theme

    // Check if the system is currently in dark mode.
    val isSystemDark = isSystemInDarkTheme()

    // Determine the color scheme based on the selected theme.
    val colorScheme = when (currentTheme.value) {
        Theme.DARK -> DarkColorScheme
        Theme.LIGHT -> LightColorScheme
        Theme.SYSTEM -> if (isSystemDark) DarkColorScheme else LightColorScheme
    }

    // Apply statusBar color and appearance based on the theme, if not in edit mode.
    LocalView.current.let { view ->
        if (!LocalInspectionMode.current) {
            SideEffect {
                (view.context as Activity).window.let { window ->
                    window.statusBarColor = colorScheme.primary.toArgb()
                    val setLiteBars = (currentTheme.value == Theme.LIGHT) || (currentTheme.value == Theme.SYSTEM && !isSystemDark)
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = setLiteBars
                }
            }
        }
    }

    // Apply the material theme.
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )

    // Performs the theme transition animation.
    if (!LocalInspectionMode.current) {
        ThemeMonitor()
    }
}
