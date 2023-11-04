/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.themes.attributes

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4E708B),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFEBF0F5),
    tertiary = Color(0xFF727272),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF303030),
    surfaceVariant = Color(0xFF303030),
    surfaceTint = Color(0xFFFFFFFF),
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF505160),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF3A3A3A),
    tertiary = Color(0xFFE2E2E2),
    surface = Color(0xFF303030),
    onSurface = Color(0xFFE2E2E2),
    surfaceVariant = Color(0xFFE2E2E2),
    surfaceTint = Color(0xFFFFFFFF),
)