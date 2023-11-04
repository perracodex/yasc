/*
 * Copyright (c) 2023 Perraco Labs. All rights reserved.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>
 */

package com.perracolabs.yasc.ui.themes.viewmodel

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.viewmodel.compose.viewModel
import com.perracolabs.yasc.ui.themes.attributes.Theme
import kotlinx.coroutines.flow.StateFlow

interface IThemeViewModel {
    val theme: State<Theme>
    val hasTransition: State<Boolean>
    val inProgress: State<Boolean>
    val transitionFactor: StateFlow<Float>
    suspend fun request(newTheme: Theme, themeLayoutView: View, transitionOrigin: Offset)
    suspend fun start()
    fun getAttributes(): ThemeViewModel.Attributes?
}

/**
 * Obtains an instance of IThemeViewModel based on the environment.
 *
 * This function provides a workaround for the limitation that Composable previews cannot
 * work directly with ViewModels. When in preview (LocalInspectionMode), a mock version
 * of the ViewModel is returned. In the runtime environment, the actual real ViewModel is used.
 *
 * @return An instance of IThemeViewModel.
 */
@Composable
fun obtainThemeViewModel(): IThemeViewModel {
    return if (LocalInspectionMode.current) {
        // In design mode, so provide a mock ViewModel to make Composable previews work.
        ThemeViewModelMock()
    } else {
        // Provide the actual ViewModel at runtime.
        viewModel<ThemeViewModel>()
    }
}
