/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.themes.transition

import androidx.compose.runtime.*
import com.perracolabs.yasc.ui.themes.viewmodel.obtainThemeViewModel
import kotlinx.coroutines.launch

/**
 * Monitors when the theme transition must occur, and starts a transition animation accordingly.
 */
@Composable
fun ThemeMonitor() {
    val themeViewModel = obtainThemeViewModel()
    val hasTransition by themeViewModel.hasTransition
    val hasTransitionState by rememberUpdatedState(newValue = hasTransition)
    val scope = rememberCoroutineScope()

    SideEffect {
        if (hasTransitionState) {
            scope.launch {
                themeViewModel.start()
            }
        }
    }

    // Composable where the actual transition rendering occurs.
    if (themeViewModel.inProgress.value) {
        ThemeRenderer()
    }
}
